package com.al3x.housing2.Utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

//Copied from Hysentials

fun CharSequence.startsWithAny(sequences: Iterable<CharSequence>): Boolean = sequences.any { contains(it) }
fun CharSequence?.containsAny(vararg sequences: CharSequence?): Boolean {
    if (this == null) return false
    return sequences.any { it != null && this.contains(it) }
}

//shift a list of strings to the right and return the String
fun List<String>.shiftRight(): String {
    val last = this.last()
    val list = this.dropLast(1)
    return last + list.joinToString("")
}


fun String.toDashedUUID(): String {
    if (this.length != 32) return this
    return buildString {
        append(this@toDashedUUID)
        insert(20, "-")
        insert(16, "-")
        insert(12, "-")
        insert(8, "-")
    }
}
fun String.substringBefore(delimiter: String): String {
    return this.substringBefore(delimiter, this)
}

fun String.substringAfter(delimiter: String): String {
    return this.substringAfter(delimiter, this)
}



fun String.toTitleCase(): String = this.lowercase().replaceFirstChar { c -> c.titlecase() }
fun String.splitToWords(): String = this.split('_', ' ').joinToString(" ") { it.toTitleCase() }
fun String.isInteger(): Boolean = this.toIntOrNull() != null
fun String.formatCapitalize(): String = this.replace("_", " ").split(" ").joinToString(" ") { it.toTitleCase() }