package me.znotchill.localtrack.api.v1.queries

import kotlinx.serialization.Serializable
import me.znotchill.localtrack.search.map.FilterPredicate

@Serializable
data class MapQuery(
    val predicates: List<FilterPredicate> = emptyList(),
    val sortBy: String? = null,
    val sortDescending: Boolean = false,
    val limit: Long = 50,
    val offset: Long = 0
)