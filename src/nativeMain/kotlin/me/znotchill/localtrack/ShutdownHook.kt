package me.znotchill.localtrack

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import me.znotchill.localtrack.api.LocalTrackAPI
import platform.posix.SIGINT
import platform.posix.SIGTERM
import platform.posix.signal

object ShutdownHook {
    @OptIn(ExperimentalForeignApi::class)
    fun register() {
        signal(
            SIGINT,
            staticCFunction<Int, Unit> {
                LocalTrackAPI.stop()
            }
        )

        signal(
            SIGTERM,
            staticCFunction<Int, Unit> {
                LocalTrackAPI.stop()
            }
        )
    }
}