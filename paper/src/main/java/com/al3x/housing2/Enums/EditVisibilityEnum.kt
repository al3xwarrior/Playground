package com.al3x.housing2.Enums

import org.bukkit.Material

enum class EditVisibilityEnum(override val material: Material) : EnumMaterial {
    NEAREST(Material.IRON_SWORD),
    CONDITION(Material.REDSTONE),
    ALL(Material.BARRIER);
}