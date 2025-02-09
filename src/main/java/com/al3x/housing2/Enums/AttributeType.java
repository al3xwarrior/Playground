package com.al3x.housing2.Enums;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public enum AttributeType implements EnumMaterial {
    ARMOR(Attribute.ARMOR, Material.IRON_CHESTPLATE),
    ARMOR_TOUGHNESS(Attribute.ARMOR_TOUGHNESS, Material.DIAMOND_CHESTPLATE),
    ATTACK_DAMAGE(Attribute.ATTACK_DAMAGE, Material.DIAMOND_SWORD),
    ATTACK_KNOCKBACK(Attribute.ATTACK_KNOCKBACK, Material.WOODEN_SWORD),
    ATTACK_SPEED(Attribute.ATTACK_SPEED, Material.GOLDEN_SWORD),
    BLOCK_BREAK_SPEED(Attribute.BLOCK_BREAK_SPEED, Material.DIAMOND_PICKAXE),
    BLOCK_INTERACTION_RANGE(Attribute.BLOCK_INTERACTION_RANGE, Material.WOODEN_SHOVEL),
    BURNING_TIME(Attribute.BURNING_TIME, Material.FLINT_AND_STEEL),
    ENTITY_INTERACTION_RANGE(Attribute.ENTITY_INTERACTION_RANGE, Material.LEAD),
    EXPLOSION_KNOCKBACK_RESISTANCE(Attribute.EXPLOSION_KNOCKBACK_RESISTANCE, Material.TNT),
    FALL_DAMAGE_MULTIPLIER(Attribute.FALL_DAMAGE_MULTIPLIER, Material.FEATHER),
    FLYING_SPEED(Attribute.FLYING_SPEED, Material.ELYTRA),
    FOLLOW_RANGE(Attribute.FOLLOW_RANGE, Material.NAME_TAG),
    GRAVITY(Attribute.GRAVITY, Material.GRAVEL),
    JUMP_STRENGTH(Attribute.JUMP_STRENGTH, Material.SLIME_BLOCK),
    KNOCKBACK_RESISTANCE(Attribute.KNOCKBACK_RESISTANCE, Material.SHIELD),
    LUCK(Attribute.LUCK, Material.GOLD_NUGGET),
    MAX_ABSORPTION(Attribute.MAX_ABSORPTION, Material.GOLDEN_APPLE),
    MAX_HEALTH(Attribute.MAX_HEALTH, Material.GOLDEN_APPLE),
    MINING_EFFICIENCY(Attribute.MINING_EFFICIENCY, Material.DIAMOND_PICKAXE),
    MOVEMENT_SPEED(Attribute.MOVEMENT_SPEED, Material.DIAMOND_BOOTS),
    OXYGEN_BONUS(Attribute.OXYGEN_BONUS, Material.WATER_BUCKET),
    SAFE_FALL_DISTANCE(Attribute.SAFE_FALL_DISTANCE, Material.FEATHER),
    SCALE(Attribute.SCALE, Material.DIAMOND),
    SNEAKING_SPEED(Attribute.SNEAKING_SPEED, Material.LEATHER_BOOTS),
    STEP_HEIGHT(Attribute.STEP_HEIGHT, Material.IRON_LEGGINGS),
    SUBMERGED_MINING_SPEED(Attribute.SUBMERGED_MINING_SPEED, Material.TRIDENT),
    SWEEPING_DAMAGE_RATIO(Attribute.SWEEPING_DAMAGE_RATIO, Material.DIAMOND_SWORD),
    WATER_MOVEMENT_EFFICIENCY(Attribute.WATER_MOVEMENT_EFFICIENCY, Material.GOLDEN_BOOTS);

    final Attribute attribute;
    final Material material;

    AttributeType(Attribute attribute, Material material) {
        this.attribute = attribute;
        this.material = material;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public static AttributeType valueFrom(String name) {
        for (AttributeType type : values()) {
            if (type.name().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
