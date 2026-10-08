package me.znotchill.localtrack.tosu

import kotlinx.serialization.Serializable

@Serializable
data class TosuPpResponse(
    val performance: Performance,
) {
    val pp: Double get() = performance.pp

    @Serializable
    data class Performance(
        val pp: Double,
        val difficulty: Difficulty? = null,
    )

    @Serializable
    data class Difficulty(
        val stars: Double? = null,
        val maxCombo: Long? = null,
    )
}