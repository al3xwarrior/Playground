package com.al3x.housing2.Enums;

import org.bukkit.Material;

public enum NavigationType implements EnumMaterial {
    PLAYER(Material.PLAYER_HEAD),
    PATH(Material.GRASS_BLOCK),
    WANDER(Material.COMPASS),
    STATIONARY(Material.BARRIER);

    Material material;
    NavigationType(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
