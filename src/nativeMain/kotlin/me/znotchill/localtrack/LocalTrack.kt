package me.znotchill.localtrack

import io.github.smyrgeorge.sqlx4k.sqlite.SQLite
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.*
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.znotchill.kelp.Database
import me.znotchill.kelp.dialects.SqliteDialect
import me.znotchill.localtrack.api.LocalTrackAPI
import me.znotchill.localtrack.db.BeatmapEntryModel
import me.znotchill.localtrack.db.ProfileSnapshotModel
import me.znotchill.localtrack.db.ScoreModel
import me.znotchill.localtrack.db.TrackRepo
import me.znotchill.localtrack.events.GameStateChange
import me.znotchill.localtrack.payload.GameState
import me.znotchill.localtrack.payload.TosuState
import me.znotchill.localtrack.storage.StorageManager
import kotlin.time.Duration.Companion.milliseconds

class LocalTrack {
    companion object {
        lateinit var instance: LocalTrack
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    lateinit var previousState: TosuState
    lateinit var currentState: TosuState

    lateinit var previousGameState: GameState
    lateinit var currentGameState: GameState

    lateinit var repo: TrackRepo
    lateinit var db: Database

    lateinit var api: LocalTrackAPI

    private var nextState: CompletableDeferred<TosuState>? = null
    suspend fun start() {
        StorageManager.create()

        var delay = 1_000L

        while (true) {
            try {
                println("Connecting to tosu!...")

                client.webSocket(
                    method = HttpMethod.Get,
                    host = "127.0.0.1",
                    port = 24050,
                    path = "/websocket/v2"
                ) {
                    println("WebSocket connected")
                    delay = 1_000L

                    while (true) {
                        val msg = incoming.receive()

                        if (msg !is Frame.Text)
                            continue

                        val text = msg.readText()

                        try {
                            val decoded = json.decodeFromString<TosuState>(text)
                            nextState?.complete(decoded)
                            nextState = null

                            if (::currentState.isInitialized) {
                                previousState = currentState
                            }

                            currentState = decoded

                            if (::currentGameState.isInitialized) {
                                previousGameState = currentGameState

                                if (decoded.state != previousGameState) {
                                    nextState = CompletableDeferred()

                                    scope.launch {
                                        GameStateChange(
                                            decoded.state,
                                            previousGameState,
                                            decoded,
                                            previousState,
                                            nextState!!
                                        ).onFire()
                                    }
                                }
                            }

                            currentGameState = decoded.state
                        } catch (e: Exception) {
                            println("decode failed: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket disconnected: ${e.message}")
            }

            println("Reconnecting in ${delay}ms...")

            kotlinx.coroutines.delay(delay.milliseconds)

            delay = (delay * 2).coerceAtMost(30_000L)
        }
    }
}

fun main() = runBlocking {
    val track = LocalTrack()
    LocalTrack.instance = track
    val api = LocalTrackAPI()
    LocalTrack.instance.api = api

    val database = Database(
        driver = SQLite(
            url = "sqlite://localtrack.db"
        ),
        dialect = SqliteDialect
    )

    try {
        database.createTable(ScoreModel)
        database.createTable(BeatmapEntryModel)
        database.createTable(ProfileSnapshotModel)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("DB opened")

    track.db = database
    track.repo = TrackRepo(database)

    println("ALL SCORES:")
    println(
        ScoreModel.getAll(database)
    )

    LocalTrack.scope.launch {
        track.start()
    }

    LocalTrack.scope.launch {
        ShutdownHook.register()
    }
    api.start()
}