package com.al3x.housing2.Enums;

import org.bukkit.Color;
import org.bukkit.Material;

public enum Colors implements EnumMaterial{
    DARK_BLUE("§1", Material.LAPIS_LAZULI),
    DARK_GREEN("§2", Material.GREEN_DYE),
    DARK_AQUA("§3", Material.LAPIS_BLOCK),
    DARK_RED("§4", Material.REDSTONE),
    DARK_PURPLE("§5", Material.PURPLE_DYE),
    GOLD("§6", Material.GOLD_INGOT),
    GRAY("§7", Material.LIGHT_GRAY_DYE),
    DARK_GRAY("§8", Material.GRAY_DYE),
    BLUE("§9", Material.LIGHT_BLUE_DYE),
    GREEN("§a", Material.LIME_DYE),
    AQUA("§b", Material.CYAN_DYE),
    RED("§c", Material.RED_DYE),
    LIGHT_PURPLE("§d", Material.MAGENTA_DYE),
    YELLOW("§e", Material.YELLOW_DYE),
    WHITE("§f", Material.BONE_MEAL),
    ;

    private final Material material;
    private final String colorcode;

    Colors(String colorcode, Material material) {
        this.material = material;
        this.colorcode = colorcode;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public String getColorcode() {
        return colorcode;
    }

    public static Colors fromColorcode(String colorcode) {
        for (Colors color : values()) {
            if (color.getColorcode().equals(colorcode)) {
                return color;
            }
        }
        return null;
    }
}
