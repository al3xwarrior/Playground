package com.al3x.housing2.Enums;

import org.bukkit.Material;

enum class VoiceGroupTypes(override val material: Material) : EnumMaterial {
    ISOLATED(Material.WHITE_WOOL),
    NORMAL(Material.OAK_PLANKS),
    OPEN(Material.GLASS),
}
