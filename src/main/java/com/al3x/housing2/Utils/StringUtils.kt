package com.al3x.housing2.Utils

import com.al3x.housing2.Instances.HousingWorld
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player

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

fun String.housingStringFormatter(): Component {
    val mm = MiniMessage.miniMessage()
    return mm.deserialize(this.oldToNew())
}

fun String.removeStringFormatting(): String {
    return Color.removeColor(LegacyComponentSerializer.legacySection().serialize(this.housingStringFormatter()))
}

fun String.removeStringFormatting(house: HousingWorld, player: Player): String {
    return Color.removeColor(LegacyComponentSerializer.legacySection().serialize(this.housingStringFormatter(house, player)))
}

fun String.housingStringFormatter(house: HousingWorld, player: Player): Component {
    val mm = MiniMessage.miniMessage()
    return mm.deserialize(HandlePlaceholders.parsePlaceholders(player, house, this).oldToNew().removeBlacklisted(house))
}

fun String.oldToNew(): String {
    var s: String = this.replace("§", "&")

    val old = arrayListOf("&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&k", "&l", "&m", "&n", "&o", "&r")
    val new = arrayListOf("<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<dark_red>", "<dark_purple>", "<gold>", "<gray>", "<dark_gray>", "<blue>", "<green>", "<aqua>", "<red>", "<light_purple>", "<yellow>", "<white>", "<obfuscated>", "<bold>", "<strikethrough>", "<underline>", "<italic>", "<reset>")

    for (i in 0 until old.size) {
        s = s.replace(old[i], new[i])
    }
    return "<!italic>$s";
}

fun String.removeBlacklisted(house: HousingWorld): String {
    if (!this.contains("<click:run_command:")) return this
    val command = this.substringAfter("<click:run_command:").substringBefore(">")
    val newString = StringBuilder()
    if (!house.commands.any { it.name == command }) {
        newString.append(this.substringBefore("<click:run_command:$command>").substringAfter("</click>"))
    } else {
        val after = this.substringAfter("<click:run_command:$command>").substringAfter("</click>")
        if (after.contains("<click:run_command:")) {
            newString.append(this.removeBlacklisted(house))
        } else {
            newString.append(this)
        }
    }
    return newString.toString()
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