package com.al3x.housing2.Enums;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;

public enum Gamemodes implements EnumMaterial {
    SURVIVAL(GameMode.SURVIVAL, Material.IRON_SWORD),
    CREATIVE(GameMode.CREATIVE, Material.GRASS_BLOCK),
    ADVENTURE(GameMode.ADVENTURE, Material.DIAMOND_SWORD),
    SPECTATOR(GameMode.SPECTATOR, Material.ENDER_EYE)
    ;

    private final GameMode gameMode;
    private final Material material;
    private Gamemodes(GameMode gameMode, Material material) {
        this.gameMode = gameMode;
        this.material = material;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }
    public Material getMaterial() {
        return this.material;
    }
}
