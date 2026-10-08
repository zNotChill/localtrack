package me.znotchill.localtrack.storage.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var setup: SetupConfig = SetupConfig(),
    var process: ProcessConfig = ProcessConfig()
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