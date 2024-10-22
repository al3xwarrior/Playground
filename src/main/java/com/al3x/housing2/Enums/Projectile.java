package com.al3x.housing2.Enums;

import org.bukkit.Material;
import org.bukkit.entity.*;

public enum Projectile implements EnumMaterial {
    ARROW(Arrow.class, Material.ARROW),
    WIND_CHARGE(WindCharge.class, Material.WIND_CHARGE),
    DRAGON_FIREBALL(DragonFireball.class, Material.DRAGON_BREATH),
    EGG(Egg.class, Material.EGG),
    ENDER_PEARL(EnderPearl.class, Material.ENDER_PEARL),
    FIREBALL(Fireball.class, Material.FIRE_CHARGE),
    FIREWORK(Firework.class, Material.FIREWORK_ROCKET),
    FISH_HOOK(FishHook.class, Material.FISHING_ROD),
    LLAMA_SPIT(LlamaSpit.class, Material.LLAMA_SPAWN_EGG),
    SHULKER_BULLET(ShulkerBullet.class, Material.SHULKER_SHELL),
    SMALL_FIREBALL(SmallFireball.class, Material.FIRE_CHARGE),
    SNOWBALL(Snowball.class, Material.SNOWBALL),
    SPECTRAL_ARROW(SpectralArrow.class, Material.SPECTRAL_ARROW),
    EXPERIENCE_BOTTLE(ThrownExpBottle.class, Material.EXPERIENCE_BOTTLE),
    SPLASH_POTION(ThrownPotion.class, Material.SPLASH_POTION),
    TRIDENT(Trident.class, Material.TRIDENT),
    WITHER_SKULL(WitherSkull.class, Material.WITHER_SKELETON_SKULL),;

    private final Class<? extends org.bukkit.entity.Projectile> projectile;
    private final Material material;
    private Projectile(Class<? extends org.bukkit.entity.Projectile> projectile, Material material) {
        this.projectile = projectile;
        this.material = material;
    }

    public Class<? extends org.bukkit.entity.Projectile> getProjectile() {
        return this.projectile;
    }

    public Material getMaterial() {
        return this.material;
    }
}
