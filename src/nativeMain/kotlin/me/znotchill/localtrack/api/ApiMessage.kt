package me.znotchill.localtrack.api

import io.ktor.http.HttpStatusCode

enum class ApiMessage(
    val response: ApiResponse
) {
    SUCCESS(
        ApiResponse(
            code = HttpStatusCode.OK,
            type = "SUCCESS",
            success = true
        )
    ),
    INVALID_REQUEST_BODY(
        ApiResponse(
            code = HttpStatusCode.BadRequest,
            type = "INVALID_REQUEST_BODY",
            message = "Invalid request body for this endpoint.",
            success = false
        )
    ),
}