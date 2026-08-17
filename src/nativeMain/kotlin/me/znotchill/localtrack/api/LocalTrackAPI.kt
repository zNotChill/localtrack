package me.znotchill.localtrack.api

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
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
            host = "127.0.0.1"
        ) {
            apiModule()
        }.start(true)
    }
}