package me.znotchill.localtrack.api

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ApiResponse(
    @Transient
    val code: HttpStatusCode = HttpStatusCode.OK,
    val type: String,
    val message: String? = null,
    val success: Boolean = true,
)

val ApiResponse.messageType: ApiMessage?
    get() = ApiMessage.entries.find { it.response.type == this.type }