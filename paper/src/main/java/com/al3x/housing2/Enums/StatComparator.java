package com.al3x.housing2.Enums;


import org.bukkit.Material;

public enum StatComparator implements EnumMaterial, EnumHTSLAlternative {
    LESS_THAN(Material.YELLOW_STAINED_GLASS, "<"),
    LESS_THAN_OR_EQUAL(Material.ORANGE_STAINED_GLASS, "<="),
    EQUALS(Material.LIME_STAINED_GLASS, "=="),
    GREATER_THAN_OR_EQUAL(Material.ORANGE_STAINED_GLASS, ">="),
    GREATER_THAN(Material.YELLOW_STAINED_GLASS, ">"),

    CONTAINS(Material.LIME_STAINED_GLASS, "contains"),
    ;

    public Material material;
    public String alternative;
    StatComparator(Material material, String alternative) {
        this.material = material;
        this.alternative = alternative;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getAlternative() {
        return alternative;
    }

    public static StatComparator getComparator(String part) {
        for (StatComparator comparator : values()) {
            if (comparator.alternative.equalsIgnoreCase(part)) return comparator;
            if (comparator.name().equalsIgnoreCase(part)) return comparator;
        }
        return null;
    }
}
