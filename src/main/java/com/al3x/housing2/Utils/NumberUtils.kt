package com.al3x.housing2.Utils

import org.bukkit.Bukkit
import java.lang.Double.*
import java.lang.Float.parseFloat
import java.lang.Integer.*
import java.lang.Long.parseLong

fun Double.toInt(): Int {
    return this.toInt()
}

fun Double.toFloat(): Float {
    return this.toFloat()
}

fun String.isDouble(): Boolean {
    try {
        parseDouble(this)
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}

fun String.isInt(): Boolean {
    try {
        parseInt(this)
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}