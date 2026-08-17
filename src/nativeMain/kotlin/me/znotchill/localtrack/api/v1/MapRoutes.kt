package me.znotchill.localtrack.api.v1

import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import me.znotchill.localtrack.api.ApiMessage
import me.znotchill.localtrack.api.json
import me.znotchill.localtrack.api.respond
import me.znotchill.localtrack.api.v1.queries.MapQuery
import me.znotchill.localtrack.db.BeatmapEntry
import me.znotchill.localtrack.search.map.MapSearch

fun Route.mapRoutesV1() {
    route("/map") {
        query {
            val request = try {
                call.receive<MapQuery>()
            } catch (_: Exception) {
                return@query call.respond(
                    ApiMessage.INVALID_REQUEST_BODY
                )
            }

            val response = MapSearch.queryBeatmaps(request)
            call.json(
                ApiMessage.SUCCESS,
                response,
                ListSerializer(BeatmapEntrySerializer)
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BeatmapEntry::class)
object BeatmapEntrySerializer