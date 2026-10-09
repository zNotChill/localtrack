package me.znotchill.localtrack.api.v1

import co.touchlab.kermit.Logger
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import me.znotchill.localtrack.api.ApiMessage
import me.znotchill.localtrack.api.respond
import me.znotchill.localtrack.api.v1.queries.OptionsQuery
import me.znotchill.localtrack.storage.config.ConfigManager
import me.znotchill.localtrack.storage.config.WebsiteOptionsConfig

private val optionsJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = false
}

private fun JsonObject.withValueAt(path: List<String>, value: JsonElement): JsonObject {
    val key = path.first()
    require(key in this) {
        "Unknown option: $key"
    }

    return if (path.size == 1) {
        JsonObject(this + (key to value))
    } else {
        val child = this[key] as? JsonObject
            ?: error("'$key' is not a nested option")
        JsonObject(
            this + (key to child.withValueAt(path.drop(1), value))
        )
    }
}

fun Route.optionsRoutesV1() {
    route("/options") {
        get {
            call.respond(ConfigManager.config.website.options)
        }
        patch {
            val request = try {
                call.receive<OptionsQuery>()
            } catch (_: Exception) {
                return@patch call.respond(ApiMessage.INVALID_REQUEST_BODY)
            }

            val updated = try {
                val serializer = WebsiteOptionsConfig.serializer()
                val tree = optionsJson
                    .encodeToJsonElement(serializer, ConfigManager.config.website.options)
                    .jsonObject

                val newTree = tree.withValueAt(request.path.split("."), request.value)
                optionsJson.decodeFromJsonElement(serializer, newTree)
            } catch (e: Exception) {
                if (e.message != null && e.message!!.startsWith("Failed to parse")) {
                    return@patch call.respond(
                        ApiMessage.INVALID_REQUEST_BODY,
                        "Invalid type provided for ${request.path}"
                    )
                }
                return@patch call.respond(
                    ApiMessage.INVALID_REQUEST_BODY,
                    e.message ?: "Invalid option"
                )
            }

            call.respond(updated)
            ConfigManager.config.website.options = updated
            ConfigManager.save()

            Logger.i("Config was updated through API, saved to disk!")

        }
    }
}