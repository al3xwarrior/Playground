package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class LaunchProjectileAction extends HTSLImpl {
    private Projectile projectile;
    private PushDirection direction;
    private String customDirection;
    private double amount;

    public LaunchProjectileAction() {
        super("Launch Projectile Action");
        this.projectile = Projectile.ARROW;
        this.direction = PushDirection.FORWARD;
        this.amount = 1.5;
    }

    public LaunchProjectileAction(Projectile projectile, Double amount, PushDirection direction) {
        super("Launch Projectile Action");
        this.projectile = projectile;
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "LaunchProjectileAction (Projectile: " + projectile + ", Direction: " + direction + ", Amount: " + amount + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ARROW);
        builder.name("&eLaunch Projectile Action");
        builder.info("&eSettings", "");
        builder.info("Projectile", "&a" + projectile);
        builder.info("Direction", "&a" + (direction == PushDirection.CUSTOM ? customDirection : direction));
        builder.info("Velocity", "&a" + amount);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ARROW);
        builder.name("&aLaunch Projectile Action");
        builder.description("Launches a projectile in a direction.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("projectile",
                        ItemBuilder.create(Material.ARROW)
                                .name("&eProjectile")
                                .info("&7Current Value", "")
                                .info(null, "&a" + projectile)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Projectile.values(), null
                ),
                new ActionEditor.ActionItem("direction",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eDirection")
                                .info("&7Current Value", "")
                                .info(null, "&a" + direction)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, PushDirection.values(), Material.COMPASS,
                        (event, obj) -> getDirection(event, obj, house, editMenu, (str, dir) -> {
                            customDirection = str;
                            direction = dir;
                        })
                ),
                new ActionEditor.ActionItem("amount",
                        ItemBuilder.create(Material.SLIME_BALL)
                                .name("&eVelocity")
                                .info("&7Current Value", "")
                                .info(null, "&a" + amount)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 0.1, 100.0 //Pretty easy to change the max value
                )
        );

        return new ActionEditor(4, "&eLaunch Projectile Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        org.bukkit.entity.Projectile proj = player.launchProjectile(projectile.getProjectile());
        proj.setMetadata("projectile", new FixedMetadataValue(Main.getInstance(), true));
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
                    return true;
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
                    return true;
                }
                break;
        }
        proj.setVelocity(velocity);
        proj.setShooter(player);
        return true;
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("projectile", projectile);
        data.put("amount", amount);
        data.put("direction", direction);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "launchProjectile";
    }
}
