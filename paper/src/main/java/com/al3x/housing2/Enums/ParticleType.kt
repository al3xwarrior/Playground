package com.al3x.housing2.Enums;

import org.bukkit.Material;

enum class ParticleType(override val material: Material) : EnumMaterial {
    LINE(Material.STICK),
    CIRCLE(Material.SNOWBALL),
    CURVE(Material.EGG),
    SQUARE(Material.PAINTING);
}
