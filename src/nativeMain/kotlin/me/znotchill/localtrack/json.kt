package me.znotchill.localtrack

import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}