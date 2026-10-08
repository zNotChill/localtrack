package me.znotchill.localtrack.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.posix.getenv
import kotlin.experimental.ExperimentalNativeApi

object StorageManager {
    private val fs = FileSystem.SYSTEM

    private val root: Path by lazy {
        val dir = appDataDir().toPath()
        fs.createDirectories(dir)
        fs.canonicalize(dir)
    }

    val tosuDir: String
        get() = appDataDir() + "/tosu"

    @OptIn(ExperimentalNativeApi::class)
    fun isOSSupported() =
        when (Platform.osFamily) {
            OsFamily.WINDOWS, OsFamily.LINUX -> {
                true
            }
            else -> false
        }

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
            // tosu does not ship macos binaries
//            OsFamily.MACOSX -> "$home/Library/Application Support/$appName"
            else -> error("Unsupported platform: ${Platform.osFamily}")
        }
    }

    private fun isInsideRoot(path: Path): Boolean =
        path.root == root.root &&
                path.segments.size >= root.segments.size &&
                path.segments.take(root.segments.size) == root.segments

    private fun resolve(relative: String): Path {
        if (relative.isEmpty() || relative == ".") return root

        require('\u0000' !in relative) { "Invalid path" }

        val rel = relative.toPath()

        require(rel.isRelative && rel.volumeLetter == null) {
            "Absolute paths are not allowed"
        }
        require(rel.segments.none { it == ".." }) {
            "Path traversal is not allowed"
        }

        val target = (root / rel).normalized()
        require(isInsideRoot(target)) { "Path escapes storage directory" }

        var existing: Path? = target
        while (existing != null && !fs.exists(existing))
            existing = existing.parent
        if (existing != null) {
            require(isInsideRoot(fs.canonicalize(existing))) {
                "Path escapes storage directory"
            }
        }

        return target
    }

    fun exists(path: String): Boolean = fs.exists(resolve(path))

    fun isDirectory(path: String): Boolean =
        fs.metadataOrNull(resolve(path))?.isDirectory == true

    fun fileSize(path: String): Long? = fs.metadataOrNull(resolve(path))?.size

    fun writeFile(path: String, content: String) {
        val target = resolve(path)
        target.parent?.let { fs.createDirectories(it) }
        fs.write(target) { writeUtf8(content) }
    }

    fun createDirectory(path: String) = fs.createDirectories(resolve(path))

    fun readFile(path: String): String =
        fs.read(resolve(path)) { readUtf8() }

    fun deleteFile(path: String): Boolean {
        val target = resolve(path)
        require(target != root) { "Cannot delete the storage root" }
        if (!fs.exists(target)) return false
        fs.delete(target)
        return true
    }

    fun deleteDirectory(path: String): Boolean {
        val target = resolve(path)
        require(target != root) { "Cannot delete the storage root" }
        if (!fs.exists(target)) return false
        fs.deleteRecursively(target)
        return true
    }

    fun copyFile(from: String, to: String) {
        val dest = resolve(to)
        dest.parent?.let { fs.createDirectories(it) }
        fs.copy(resolve(from), dest)
    }

    fun renameFile(from: String, to: String) {
        val dest = resolve(to)
        dest.parent?.let { fs.createDirectories(it) }
        fs.atomicMove(resolve(from), dest)
    }
}