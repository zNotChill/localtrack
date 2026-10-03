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
import kotlinx.cinterop.ExperimentalForeignApi
import me.znotchill.localtrack.api.v1.mapRoutesV1
import me.znotchill.localtrack.api.v1.scoreRoutesV1
import me.znotchill.localtrack.json

class LocalTrackAPI {
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
            }
        }
    }

    fun start() {
        server = embeddedServer(
            factory = CIO,
            port = 1727,
            host = "127.0.0.1",
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