package com.al3x.housing2.Enums;

import org.bukkit.Material;

public enum ParticleType implements EnumMaterial{
    LINE(Material.STICK),
    CIRCLE(Material.SNOWBALL),
    SQUARE(Material.PAINTING);

    Material material;

    ParticleType(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
