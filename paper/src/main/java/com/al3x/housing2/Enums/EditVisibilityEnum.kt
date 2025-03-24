package com.al3x.housing2.Enums;
import org.bukkit.Material;

public enum EditVisibilityEnum implements EnumMaterial{
    NEAREST(Material.IRON_SWORD),
    CONDITION(Material.REDSTONE),
    ALL(Material.BARRIER);

    private final Material material;
    EditVisibilityEnum(Material material) {
        this.material = material;
    }

    public static EditVisibilityEnum fromString(String string) {
        for (EditVisibilityEnum editVisibilityEnum : values()) {
            if (editVisibilityEnum.name().equalsIgnoreCase(string)) {
                return editVisibilityEnum;
            }
        }
        return null;
    }


    @Override
    public Material getMaterial() {
        return material;
    }
}
