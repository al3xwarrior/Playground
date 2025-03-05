package com.al3x.housing2.Enums;

import org.bukkit.Material;

public enum VoiceGroupTypes implements EnumMaterial {
    ISOLATED(Material.WHITE_WOOL),
    NORMAL(Material.OAK_PLANKS),
    OPEN(Material.GLASS),
    ;

    Material material;

    VoiceGroupTypes(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return this.material;
    }

    public static VoiceGroupTypes fromString(String string) {
        return switch (string) {
            case "ISOLATED" -> ISOLATED;
            case "NORMAL" -> NORMAL;
            case "OPEN" -> OPEN;
            default -> null;
        };
    }
}
