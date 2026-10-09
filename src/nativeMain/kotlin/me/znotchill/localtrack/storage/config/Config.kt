package me.znotchill.localtrack.storage.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var version: Int? = null,
    var setup: SetupConfig = SetupConfig(),
    var process: ProcessConfig = ProcessConfig(),
    var website: WebsiteConfig = WebsiteConfig()
)

@Serializable
data class SetupConfig(
    var firstTimeRunning: Boolean = true,
    /**
     * whether we should create our own tosu process
     * if one cannot be found on port 24050
     */
    var spinUpProcess: Boolean = false,
    var tosuBinaryPath: String = "",
)

@Serializable
data class ProcessConfig(
    var startup: Boolean = false
)

@Serializable
data class WebsiteConfig(
    /**
     * Whether we should allow options to be updated
     * directly from the API
     */
    var allowApiOptionsUpdates: Boolean = true,
    var options: WebsiteOptionsConfig = WebsiteOptionsConfig()
)

@Serializable
data class WebsiteOptionsConfig(
    val showDecimalPPValues: Boolean = true,
    val renderMapBackgrounds: Boolean = true,
)