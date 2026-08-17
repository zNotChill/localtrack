package me.znotchill.localtrack.api

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

suspend fun RoutingCall.respond(message: ApiMessage) {
    val response = message.response
    respond(response.code, response)
}

suspend inline fun <reified T : Any> RoutingCall.respond(
    message: ApiMessage,
    details: T
) {
    val response = message.response
    val withDetails = ApiResponseWithDetails(
        type = response.type,
        message = response.message,
        success = response.success,
        details = details
    )
    respond(response.code, withDetails)
}

suspend inline fun <reified T : Any> RoutingCall.json(
    message: ApiMessage,
    details: T,
    serializer: KSerializer<T>,
    json: Json = me.znotchill.localtrack.json
) {
    val response = message.response
    val withDetails = ApiResponseWithDetails(
        type = response.type,
        message = response.message,
        success = response.success,
        details = details
    )
    val string = json.encodeToString(ApiResponseWithDetails.serializer(serializer), withDetails)
    respondText(string, ContentType.Application.Json, response.code)
}