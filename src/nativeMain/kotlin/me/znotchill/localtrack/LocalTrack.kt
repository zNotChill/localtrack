package me.znotchill.localtrack

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.*
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.znotchill.localtrack.api.LocalTrackAPI
import me.znotchill.localtrack.db.LocalTrackDatabase
import me.znotchill.localtrack.db.LocalTrackRepository
import me.znotchill.localtrack.events.GameStateChange
import me.znotchill.localtrack.payload.GameState
import me.znotchill.localtrack.payload.TosuState

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

    lateinit var trackRepo: LocalTrackRepository
    lateinit var db: LocalTrackDatabase

    lateinit var api: LocalTrackAPI

    suspend fun start() = run {
        try {
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 24050,
                path = "/websocket/v2"
            ) {
//            val timeSource = TimeSource.Monotonic
//            var windowStart = timeSource.markNow()
//            var count = 0

                while (true) {
                    val msg = incoming.receive() as Frame.Text
                    val text = msg.readText()
                    try {
                        val decoded = json.decodeFromString<TosuState>(text)

                        if (::currentState.isInitialized) {
                            previousState = currentState
                        }
                        currentState = decoded

                        if (::currentGameState.isInitialized) {
                            previousGameState = currentGameState
                            if (decoded.state != previousGameState) {
                                GameStateChange(
                                    decoded.state,
                                    previousGameState,

                                    decoded,
                                    previousState
                                ).onFire()
                            }
                        }
                        currentGameState = decoded.state

//                    count++
//                    if (windowStart.elapsedNow().inWholeMilliseconds >= 1000) {
//                        println("Messages/sec: $count")
//                        count = 0
//                        windowStart = timeSource.markNow()
//                    }
                    } catch (e: Exception) {
                        println("decode failed: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
//            e.printStackTrace()
        }
    }
}

fun main() = runBlocking {
    val track = LocalTrack()
    LocalTrack.instance = track
    val api = LocalTrackAPI()
    LocalTrack.instance.api = api

    val driver: SqlDriver = NativeSqliteDriver(LocalTrackDatabase.Schema, "localtrack.db")
    val database = LocalTrackDatabase(driver)
    val json = Json { ignoreUnknownKeys = true }
    val repo = LocalTrackRepository(database, json)
    println("DB opened! Recent scores: ${repo.getRecentScores(5)}")

    track.db = database
    track.trackRepo = repo

    LocalTrack.scope.launch {
        track.start()
    }

    ShutdownHook.register()

//    while (1==1) {}
    api.start()
}