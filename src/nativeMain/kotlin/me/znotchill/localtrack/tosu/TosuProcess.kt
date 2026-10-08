package me.znotchill.localtrack.tosu

import me.znotchill.localtrack.storage.StorageManager
import platform.posix.system
import kotlin.experimental.ExperimentalNativeApi

object TosuProcess {
    fun hasPtraceCap(): Boolean {
        val bin = "${StorageManager.tosuDir}/${TosuSetup.expectedTosuBinary()}"
        return system("getcap '$bin' | grep -q cap_sys_ptrace") == 0
    }

    @OptIn(ExperimentalNativeApi::class)
    fun start(): Boolean {
        val binary = TosuSetup.expectedTosuBinary() ?: return false
        if (!StorageManager.exists("tosu/$binary")) return false

        val dir = StorageManager.tosuDir
        val cmd = when (Platform.osFamily) {
            OsFamily.WINDOWS ->
                "cd /d \"$dir\" && start \"\" /B \"$binary\" > tosu.log 2>&1"
            else ->
                "cd '$dir' && chmod +x './$binary' && nohup './$binary' > tosu.log 2>&1 &"
        }
        return system(cmd) == 0
    }

    @OptIn(ExperimentalNativeApi::class)
    fun stop() {
        val binary = TosuSetup.expectedTosuBinary() ?: return
        when (Platform.osFamily) {
            OsFamily.WINDOWS -> system("taskkill /IM $binary /F > nul 2>&1")
            else -> system("pkill -x $binary")
        }
    }
}