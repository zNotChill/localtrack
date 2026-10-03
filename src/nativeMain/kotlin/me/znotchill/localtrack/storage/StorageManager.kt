package me.znotchill.localtrack.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.posix.getenv
import kotlin.experimental.ExperimentalNativeApi

object StorageManager {
    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    fun appDataDir(): String {
        val home = getenv("HOME")?.toKString()
        val appName = "localtrack"

        return when (Platform.osFamily) {
            OsFamily.WINDOWS -> {
                val base = getenv("LOCALAPPDATA")?.toKString()
                    ?: getenv("APPDATA")?.toKString()
                    ?: error("Neither LOCALAPPDATA nor APPDATA set")
                "$base\\$appName"
            }
            OsFamily.LINUX -> {
                val base = getenv("XDG_DATA_HOME")?.toKString()
                    ?: "$home/.local/share"
                "$base/$appName"
            }
            OsFamily.MACOSX -> {
                "$home/Library/Application Support/$appName"
            }
            else -> error("Unsupported platform: ${Platform.osFamily}")
        }
    }

    fun create() {
        writeFile(
            appDataDir(), "app.conf", ""
        )
    }

    fun writeFile(dirPath: String, fileName: String, content: String) {
        val fs = FileSystem.SYSTEM
        val dir = dirPath.toPath()
        fs.createDirectories(dir)
        val filePath = dir / fileName
        fs.write(filePath) {
            writeUtf8(content)
        }
    }

    fun readFile(dirPath: String, fileName: String): String {
        val fs = FileSystem.SYSTEM
        val filePath = dirPath.toPath() / fileName
        return fs.read(filePath) {
            readUtf8()
        }
    }
}