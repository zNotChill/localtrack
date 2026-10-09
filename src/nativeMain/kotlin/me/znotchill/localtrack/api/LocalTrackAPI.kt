package me.znotchill.localtrack.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import me.znotchill.localtrack.api.v1.mapRoutesV1
import me.znotchill.localtrack.api.v1.optionsRoutesV1
import me.znotchill.localtrack.api.v1.scoreRoutesV1
import me.znotchill.localtrack.json

object LocalTrackAPI {
    lateinit var server: EmbeddedServer<*, *>

    fun Application.apiModule() {
        install(CORS) {
            allowMethod(HttpMethod.Query)
            allowHost("localhost:3000")
            allowHeader(HttpHeaders.ContentType)
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(IgnoreTrailingSlash)

        routing {
            route("/api/v1") {
                scoreRoutesV1()
                mapRoutesV1()
                optionsRoutesV1()
            }
        }
    }

    fun start() {
        server = embeddedServer(
            factory = CIO,
            configure = {
                connector {
                    host = "127.0.0.1"
                    port = 1727
                }
                reuseAddress = true
            }
        ) {
            apiModule()
        }
        server.start(wait = true)
    }

    fun stop() {
        println("gracefully stopping server")
        server.stop(5000, 5000)
    }
}