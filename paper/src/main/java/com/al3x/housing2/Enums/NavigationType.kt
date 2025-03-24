package com.al3x.housing2.Enums;

import org.bukkit.Material;

enum class NavigationType(override val material: Material) : EnumMaterial {
    PLAYER(Material.PLAYER_HEAD),
    PATH(Material.GRASS_BLOCK),
    WANDER(Material.COMPASS),
    STATIONARY(Material.BARRIER);
}
