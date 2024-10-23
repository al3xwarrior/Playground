package com.al3x.housing2.Utils

import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

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