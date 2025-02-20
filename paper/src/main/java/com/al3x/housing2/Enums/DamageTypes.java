package com.al3x.housing2.Enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;

public enum DamageTypes implements EnumMaterial {
    IN_FIRE(Material.FIRE_CHARGE, DamageType.IN_FIRE),
    CAMPFIRE(Material.CAMPFIRE, DamageType.CAMPFIRE),
    LIGHTNING_BOLT(Material.LIGHTNING_ROD, DamageType.LIGHTNING_BOLT),
    ON_FIRE(Material.FIRE_CHARGE, DamageType.ON_FIRE),
    LAVA(Material.LAVA_BUCKET, DamageType.LAVA),
    HOT_FLOOR(Material.MAGMA_BLOCK, DamageType.HOT_FLOOR),
    IN_WALL(Material.TORCH, DamageType.IN_WALL),
    CRAMMING(Material.ARMOR_STAND, DamageType.CRAMMING),
    DROWN(Material.WATER_BUCKET, DamageType.DROWN),
    STARVE(Material.ROTTEN_FLESH, DamageType.STARVE),
    CACTUS(Material.CACTUS, DamageType.CACTUS),
    FALL(Material.LADDER, DamageType.FALL),
    ENDER_PEARL(Material.ENDER_PEARL, DamageType.ENDER_PEARL),
    FLY_INTO_WALL(Material.ELYTRA, DamageType.FLY_INTO_WALL),
    OUT_OF_WORLD(Material.BARRIER, DamageType.OUT_OF_WORLD),
    GENERIC(Material.BARRIER, DamageType.GENERIC),
    MAGIC(Material.BARRIER, DamageType.MAGIC),
    WITHER(Material.WITHER_ROSE, DamageType.WITHER),
    DRAGON_BREATH(Material.DRAGON_BREATH, DamageType.DRAGON_BREATH),
    DRY_OUT(Material.SAND, DamageType.DRY_OUT),
    SWEET_BERRY_BUSH(Material.SWEET_BERRIES, DamageType.SWEET_BERRY_BUSH),
    FREEZE(Material.ICE, DamageType.FREEZE),
    STALAGMITE(Material.STONE, DamageType.STALAGMITE),
    FALLING_BLOCK(Material.SAND, DamageType.FALLING_BLOCK),
    FALLING_ANVIL(Material.ANVIL, DamageType.FALLING_ANVIL),
    FALLING_STALACTITE(Material.STONE, DamageType.FALLING_STALACTITE),
    STING(Material.BEE_SPAWN_EGG, DamageType.STING),
    MOB_ATTACK(Material.ZOMBIE_HEAD, DamageType.MOB_ATTACK),
    MOB_ATTACK_NO_AGGRO(Material.WHITE_WOOL, DamageType.MOB_ATTACK_NO_AGGRO),
    PLAYER_ATTACK(Material.IRON_SWORD, DamageType.PLAYER_ATTACK),
    ARROW(Material.ARROW, DamageType.ARROW),
    TRIDENT(Material.TRIDENT, DamageType.TRIDENT),
    MOB_PROJECTILE(Material.SKELETON_SKULL, DamageType.MOB_PROJECTILE),
    SPIT(Material.SNOWBALL, DamageType.SPIT),
    FIREWORKS(Material.FIREWORK_ROCKET, DamageType.FIREWORKS),
    FIREBALL(Material.FIRE_CHARGE, DamageType.FIREBALL),
    UNATTRIBUTED_FIREBALL(Material.FIRE_CHARGE, DamageType.UNATTRIBUTED_FIREBALL),
    WITHER_SKULL(Material.WITHER_SKELETON_SKULL, DamageType.WITHER_SKULL),
    THROWN(Material.SPLASH_POTION, DamageType.THROWN),
    INDIRECT_MAGIC(Material.SPLASH_POTION, DamageType.INDIRECT_MAGIC),
    THORNS(Material.CACTUS, DamageType.THORNS),
    EXPLOSION(Material.TNT, DamageType.EXPLOSION),
    PLAYER_EXPLOSION(Material.TNT, DamageType.PLAYER_EXPLOSION),
    SONIC_BOOM(Material.WARDEN_SPAWN_EGG, DamageType.SONIC_BOOM),
    BAD_RESPAWN_POINT(Material.RESPAWN_ANCHOR, DamageType.BAD_RESPAWN_POINT),
    OUTSIDE_BORDER(Material.BARRIER, DamageType.OUTSIDE_BORDER),
    GENERIC_KILL(Material.BARRIER, DamageType.GENERIC_KILL),
    WIND_CHARGE(Material.WITHER_ROSE, DamageType.WIND_CHARGE),
    MACE_SMASH(Material.WITHER_ROSE, DamageType.MACE_SMASH),
    ;

    private final Material material;
    private final DamageType damageType;
    
    DamageTypes(Material material, DamageType damageType) {
        this.material = material;
        this.damageType = damageType;
    }
    
    @Override
    public Material getMaterial() {
        return material;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public Component getTranslation() {
        return Component.translatable(damageType.getTranslationKey()).color(NamedTextColor.GREEN);
    }

    public static DamageTypes fromDamageType(DamageType damageType) {
        for (DamageTypes type : values()) {
            if (type.damageType == damageType) {
                return type;
            }
        }
        return null;
    }
}
