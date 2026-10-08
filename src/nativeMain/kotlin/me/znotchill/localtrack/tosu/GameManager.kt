package me.znotchill.localtrack.tosu

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import me.znotchill.localtrack.LocalTrack
import me.znotchill.localtrack.events.GameStateChange
import me.znotchill.localtrack.json
import me.znotchill.localtrack.payload.GameState
import me.znotchill.localtrack.payload.TosuState
import kotlin.time.Duration.Companion.milliseconds

object GameManager {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    lateinit var previousState: TosuState
    lateinit var currentState: TosuState

    lateinit var previousGameState: GameState
    lateinit var currentGameState: GameState
    private var nextState: CompletableDeferred<TosuState>? = null

    suspend fun isTosuRunning(): Boolean = try {
        val response: HttpResponse = client.get("http://127.0.0.1:24050/json/v2")
        response.status == HttpStatusCode.OK
    } catch (_: Exception) {
        // connection refused, timeout, etc
        false
    }

    suspend fun websocketLoop() {
        var delay = 1_000L

        while (true) {
            connect(
                onConnect = { delay = 1_000L }
            )

            Logger.e("Failed to connect to tosu, reconnecting in ${delay}ms...")

            kotlinx.coroutines.delay(delay.milliseconds)

            delay = (delay * 2).coerceAtMost(30_000L)
        }
    }

    suspend fun connect(
        onConnect: () -> Unit = {},
        onFailure: () -> Unit = {},
    ) {
        try {
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 24050,
                path = "/websocket/v2"
            ) {
                onConnect()

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

                                LocalTrack.scope.launch {
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
            onFailure()
        }
    }
}