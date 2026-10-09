package me.znotchill.localtrack.api.v1.queries

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OptionsQuery(
    val path: String,
    val value: JsonElement
)