package me.znotchill.localtrack.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseWithDetails<T>(
    val type: String,
    val message: String?,
    val success: Boolean,
    val details: T
)