package me.znotchill.localtrack

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.posix.SIGINT
import platform.posix.SIGTERM
import platform.posix.atexit
import platform.posix.signal

object ShutdownHook {
    @OptIn(ExperimentalForeignApi::class)
    fun register() {
        signal(
            SIGINT,
            staticCFunction<Int, Unit> {
                LocalTrack.instance.api.stop()
            }
        )

        signal(
            SIGTERM,
            staticCFunction<Int, Unit> {
                LocalTrack.instance.api.stop()
            }
        )
    }
}