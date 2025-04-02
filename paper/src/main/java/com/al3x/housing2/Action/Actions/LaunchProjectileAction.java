package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
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
    private Projectile projectile = Projectile.ARROW;
    private PushDirection direction = PushDirection.FORWARD;
    private String amount = "1.5";

    private String customDirection;
    private ItemStack item = null;

    public LaunchProjectileAction() {
        super(
                "launch_projectile_action",
                "Launch Projectile",
                "Launches a projectile in a direction.",
                Material.ARROW,
                List.of("launchProjectile")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "projectile",
                        "Projectile",
                        "The projectile to launch.",
                        ActionProperty.PropertyType.ENUM,
                        Projectile.class
                ),
                new ActionProperty(
                        "direction",
                        "Direction",
                        "The direction to launch the projectile.",
                        ActionProperty.PropertyType.ENUM,
                        PushDirection.class,
                        this::directionConsumer
                ),
                new ActionProperty(
                        "customDirection",
                        "Custom Direction",
                        "The custom direction to launch the projectile.",
                        ActionProperty.PropertyType.ITEM
                ).showIf(projectile.getProjectile() == ThrownPotion.class),
                new ActionProperty(
                        "velocity",
                        "Velocity",
                        "The velocity of the projectile.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "item",
                        "Item",
                        "The item to launch.",
                        ActionProperty.PropertyType.ITEM
                )
        ));
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> directionConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, obj) -> getDirection(event, obj, house, backMenu, (str, dir) -> {
            customDirection = str;
            direction = dir;
        });
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        org.bukkit.entity.Projectile proj = player.launchProjectile(projectile.getProjectile());
        proj.setMetadata("projectile", new FixedMetadataValue(Main.getInstance(), 10));

        String amountParsed = HandlePlaceholders.parsePlaceholders(player, house, this.amount);

        if (!NumberUtilsKt.isDouble(amountParsed)) {
            return OutputType.ERROR;
        }
        double amount = Double.parseDouble(amountParsed);

        if (amount < 0) {
            return OutputType.ERROR;
        }

        if (amount > 10) {
            amount = 10;
        }

        Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(amount);
        switch (direction) {
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
            case CUSTOM:
                String[] split = customDirection.split(",");
                if (split.length != 2) {
                    return OutputType.ERROR;
                }

                try {
                    float pitch = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[0]));
                    float yaw = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[1]));
                    Vector custom = player.getEyeLocation().getDirection().setY(0).normalize().multiply(amount);
                    custom = custom.setX(custom.getX() * Math.cos(Math.toRadians(yaw)));
                    custom = custom.setY(Math.sin(Math.toRadians(pitch)) * amount);
                    custom = custom.setZ(custom.getZ() * Math.sin(Math.toRadians(yaw)));
                    velocity = custom;
                } catch (NumberFormatException e) {
                    return OutputType.ERROR;
                }
                break;
        }
        if (proj instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) proj;
            if (item != null) {
                potion.setItem(item);
            }
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
    public boolean mustBeSync() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        projectile = (data.get("projectile") instanceof String) ? Projectile.valueOf((String) data.get("projectile")) : (Projectile) data.get("projectile");
        amount = data.get("amount").toString();
        direction = (data.get("direction") instanceof String) ? PushDirection.valueOf((String) data.get("direction")) : (PushDirection) data.get("direction");
        if (projectile == Projectile.SPLASH_POTION) {
            try {
                item = Serialization.itemStackFromBase64((String) data.get("item"));
            } catch (Exception e) {
                e.printStackTrace();
                Main.getInstance().getLogger().warning("Failed to load item from base64 string");
            }
        }
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] split = action.split(" ");
        if (split.length < 3) {
            return nextLines;
        }

        try {
            projectile = Projectile.getProjectile(split[0]);
            Duple<String[], String> arg = handleArg(split, 1);
            if (arg == null) {
                return nextLines;
            }
            amount = arg.getSecond();
            split = arg.getFirst();
            if (split.length < 1) {
                return nextLines;
            }
            direction = PushDirection.valueOf(split[0]);
        } catch (IllegalArgumentException e) {
            return nextLines;
        }

        return nextLines;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
