package me.znotchill.localtrack

import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    isLenient = false
    ignoreUnknownKeys = true
    coerceInputValues = true
}