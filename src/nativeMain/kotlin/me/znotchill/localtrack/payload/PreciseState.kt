package me.znotchill.localtrack.payload

import kotlinx.serialization.Serializable

@Serializable
data class PreciseState(
    val currentTime: Long,
    val keys: Keys,
    val hitErrors: List<Int> = emptyList(),
    val tourney: List<TourneyPrecise> = emptyList()
)

@Serializable
data class Keys(
    val k1: KeyState,
    val k2: KeyState,
    val m1: KeyState,
    val m2: KeyState
)

@Serializable
data class KeyState(
    val isPressed: Boolean,
    val count: Long
)

@Serializable
data class TourneyPrecise(
    val ipcId: Long,
    val keys: Keys,
    val hitErrors: List<Int> = emptyList()
)