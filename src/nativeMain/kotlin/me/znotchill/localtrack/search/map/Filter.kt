package me.znotchill.localtrack.search.map

import kotlinx.serialization.Serializable

@Serializable
enum class FilterOp {
    EQ, NEQ, GT, GTE, LT, LTE, CONTAINS
}

@Serializable
data class FilterPredicate(
    val field: String,
    val op: FilterOp,
    val value: String
)