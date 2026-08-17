package me.znotchill.localtrack.api

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.cinterop.ExperimentalForeignApi
import me.znotchill.localtrack.api.v1.mapRoutesV1
import me.znotchill.localtrack.api.v1.scoreRoutesV1
import me.znotchill.localtrack.json

class LocalTrackAPI {

    fun Application.apiModule() {
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
        embeddedServer(
            factory = CIO,
            port = 1727,
            host = "127.0.0.1",
        ) {
            apiModule()
        }.start(wait = true)
    }
}