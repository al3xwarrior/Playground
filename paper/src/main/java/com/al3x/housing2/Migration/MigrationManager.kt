package com.al3x.housing2.Migration

import java.io.File

fun main(lines: String): String {
    val lines = lines.lines()
    val lookFor = OldActionType.entries.map { it.actionName }
    val convertTo = NewActionType.entries.map { it.name }
    val newLines = mutableListOf<String>()
    for (line in lines) {
        var newLine = line
        for ((i, look) in lookFor.withIndex()) {
            if (line.contains(look)) {
                newLine = line.replace(look, convertTo[i])
                println("Old: $line")
                println("New: $newLine")
                println()
            }
        }

        if (newLine.contains("\"data\": {")) {
            newLine = newLine.replace("\"data\": {", "\"properties\": {")
            println("Old: $line")
            println("New: $newLine")
        }

        newLines.add(newLine)
    }

    return newLines.joinToString("\n")
}