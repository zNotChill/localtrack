package me.znotchill.localtrack.api.v1

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.query
import io.ktor.server.routing.route
import me.znotchill.localtrack.LocalTrack

fun Route.scoreRoutesV1() {
    route("/scores") {
        get {
            call.respond(
                LocalTrack.instance.trackRepo.getRecentScores(50)
            )
        }
        query {
            call.respond(
                LocalTrack.instance.trackRepo.getRecentScores(50)
            )
        }
    }
}