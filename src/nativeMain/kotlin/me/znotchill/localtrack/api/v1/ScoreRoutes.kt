package me.znotchill.localtrack.api.v1

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.query
import io.ktor.server.routing.route
import me.znotchill.localtrack.LocalTrack
import me.znotchill.localtrack.api.ApiMessage
import me.znotchill.localtrack.api.respond
import me.znotchill.localtrack.api.v1.queries.ScoreQuery

fun Route.scoreRoutesV1() {
    route("/scores") {
        get {
            call.respond(
                LocalTrack.instance.trackRepo.getRecentScores(50)
            )
        }
        query {
            val request = try {
                call.receive<ScoreQuery>()
            } catch (_: Exception) {
                return@query call.respond(
                    ApiMessage.INVALID_REQUEST_BODY
                )
            }
            call.respond(
                LocalTrack.instance.trackRepo.getRecentScores(request.limit ?: 50)
            )
        }
    }
}