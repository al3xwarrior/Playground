package com.al3x.housing2.Enums;

import org.bukkit.Material;

public enum AttackEntityEnum implements EnumMaterial{
    NEAREST(Material.IRON_SWORD),
    CONDITION(Material.REDSTONE),
    PLAYER(Material.PLAYER_HEAD),
    NPC(Material.WOLF_SPAWN_EGG),
    MOB(Material.ZOMBIE_HEAD),
    ALL(Material.BARRIER);

    private final Material material;
    AttackEntityEnum(Material material) {
        this.material = material;
    }

    public static AttackEntityEnum fromString(String string) {
        for (AttackEntityEnum attackEntityEnum : values()) {
            if (attackEntityEnum.name().equalsIgnoreCase(string)) {
                return attackEntityEnum;
            }
        }
        return null;
    }


    @Override
    public Material getMaterial() {
        return material;
    }
}
