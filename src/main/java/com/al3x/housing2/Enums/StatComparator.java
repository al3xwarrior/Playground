package com.al3x.housing2.Enums;


import org.bukkit.Material;

public enum StatComparator implements EnumMaterial {
    LESS_THAN(Material.YELLOW_STAINED_GLASS),
    LESS_THAN_OR_EQUAL(Material.ORANGE_STAINED_GLASS),
    EQUALS(Material.LIME_STAINED_GLASS),
    GREATER_THAN_OR_EQUAL(Material.ORANGE_STAINED_GLASS),
    GREATER_THAN(Material.YELLOW_STAINED_GLASS),
    ;

    public Material material;
    StatComparator(Material material) {
        this.material = material;
    }

    public String asString() {
        return switch (this) {
            case LESS_THAN -> "<";
            case LESS_THAN_OR_EQUAL -> "<=";
            case EQUALS -> "=";
            case GREATER_THAN -> ">";
            case GREATER_THAN_OR_EQUAL -> ">=";
        };
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
