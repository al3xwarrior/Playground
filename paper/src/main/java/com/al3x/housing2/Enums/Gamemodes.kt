package com.al3x.housing2.Enums;

import org.bukkit.GameMode
import org.bukkit.Material

enum class Gamemodes(val gameMode: GameMode, override val material: Material) : EnumMaterial {
    SURVIVAL(GameMode.SURVIVAL, Material.IRON_SWORD),
    CREATIVE(GameMode.CREATIVE, Material.GRASS_BLOCK),
    ADVENTURE(GameMode.ADVENTURE, Material.DIAMOND_SWORD),
    SPECTATOR(GameMode.SPECTATOR, Material.ENDER_EYE);
}
