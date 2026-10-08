package me.znotchill.localtrack.storage.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.znotchill.localtrack.storage.StorageManager

object ConfigManager {
    var config: Config = Config()
    val toml = Toml()

    fun init() {
        if (!StorageManager.exists("config.toml"))
            save()
        else load()

        println(config)
    }

    fun save() {
        StorageManager.writeFile("config.toml", toml.encodeToString(config))
    }

    fun load() {
        if (!StorageManager.exists("config.toml"))
            throw Exception("Config does not exist")
        config = toml.decodeFromString(StorageManager.readFile("config.toml"))
    }
}