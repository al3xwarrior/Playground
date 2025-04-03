package com.al3x.housing2.Enums;

import org.bukkit.Material;

public enum ProtoolsType implements EnumMaterial {
    SELECT(Material.STICK),
    SET(Material.GRASS_BLOCK),
    REPLACE(Material.DIRT),
    COPY(Material.BUCKET),
    PASTE(Material.WATER_BUCKET),
    UNDO(Material.BARRIER),
    ;

    Material material;
    ProtoolsType(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
