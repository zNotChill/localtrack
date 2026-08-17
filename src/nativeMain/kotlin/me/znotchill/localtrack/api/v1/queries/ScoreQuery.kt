package me.znotchill.localtrack.api.v1.queries

import kotlinx.serialization.Serializable

// TODO: advanced filters like pp>=500
@Serializable
data class ScoreQuery(
    val limit: Long? = null
)