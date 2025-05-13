package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.Serialization;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

@ToString
public class LaunchProjectileAction extends HTSLImpl {
    public LaunchProjectileAction() {
        super(
                ActionEnum.LAUNCH_PROJECTILE,
                "Launch Projectile",
                "Launches a projectile in a direction.",
                Material.ARROW,
                List.of("launchProjectile")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "projectile",
                        "Projectile",
                        "The projectile to launch.",
                        Projectile.class
                ).setValue(Projectile.ARROW),
                new EnumProperty<>(
                        "direction",
                        "Direction",
                        "The direction to launch the projectile.",
                        PushDirection.class
                ).setValue(PushDirection.FORWARD),
                new NumberProperty(
                        "velocity",
                        "Velocity",
                        "The velocity of the projectile.",
                        0.1, 5
                ).setValue("1.5"),
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to launch."
                ).showIf(() -> getValue("projectile", Projectile.class) == Projectile.SPLASH_POTION).setValue(new ItemStack(Material.SPLASH_POTION, 1))
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        org.bukkit.entity.Projectile proj = player.launchProjectile(getValue("projectile", Projectile.class).getProjectile());
        proj.setMetadata("projectile", new FixedMetadataValue(Main.getInstance(), 10));

        double amount = getProperty("velocity", NumberProperty.class).parsedValue(house, player);

        Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(amount);
        switch (getValue("direction", PushDirection.class)) {
            case UP:
                velocity.setY(amount);
                break;
            case DOWN:
                velocity.setY(-amount);
                break;
            case FORWARD:
                break;
            case BACKWARD:
                velocity.multiply(-1);
                break;
            case LEFT:
                velocity = new Vector(velocity.getZ(), 0, -velocity.getX());
                break;
            case RIGHT:
                velocity = new Vector(-velocity.getZ(), 0, velocity.getX());
                break;
            case EAST:
                velocity = new Vector(amount, 0, 0);
                break;
            case WEST:
                velocity = new Vector(-amount, 0, 0);
                break;
            case NORTH:
                velocity = new Vector(0, 0, -amount);
                break;
            case SOUTH:
                velocity = new Vector(0, 0, amount);
                break;
        }

        if (proj instanceof ThrownPotion potion) {
            potion.setItem(getProperty("item", ItemStackProperty.class).getValue());
            potion.setVelocity(velocity);
            potion.setShooter(player);
            return OutputType.SUCCESS;
        }

        proj.setVelocity(velocity);
        proj.setShooter(player);
        return OutputType.SUCCESS;
    }

    @Override
    public int limit() {
        return 5;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
