package com.al3x.housing2.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum Locations implements EnumMaterial {
    CUSTOM(Material.COMPASS),
    HOUSE_SPAWN(Material.GRASS_BLOCK),
    PLAYER_LOCATION(Material.LIGHTNING_ROD),
    INVOKERS_LOCATION(Material.PLAYER_HEAD),
    ;

    private final Material material;
    public static Locations fromString(String string) {
        for (Locations location : values()) {
            if (location.name().equalsIgnoreCase(string)) {
                return location;
            }
        }
        return null;
    }
}
