package com.al3x.housing2.Utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*

open class JsonConfig(private val main: Plugin, val file: File) {
    val config = HashMap<String, Any>()

    init {
        reload()
    }

    fun reload() {
        if(!file.exists()) {
            main.saveResource(file.name, false)
        }
        val file = FileReader(file)
        config.putAll(prettyGson.fromJson(file, config.javaClass))
    }

    fun save() {
        val json = prettyGson.toJson(config)
        file.delete()
        Files.write(file.toPath(), json.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }
}

val prettyGson = GsonBuilder().setPrettyPrinting().create()