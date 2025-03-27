package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class LaunchProjectileAction extends HTSLImpl {
    private Projectile projectile;
    private PushDirection direction;
    private String customDirection;
    private String amount;

    //Custom Data
    private ItemStack item = null;

    public LaunchProjectileAction() {
        super("Launch Projectile Action");
        this.projectile = Projectile.ARROW;
        this.direction = PushDirection.FORWARD;
        this.amount = "1.5";
    }

    public LaunchProjectileAction(Projectile projectile, String amount, PushDirection direction) {
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

        if (item != null) {
            builder.info("Item", "&6" + item.getType());
        }

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
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.addAll(Arrays.asList(
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
                        ActionEditor.ActionItem.ActionType.STRING
                )
        ));

        if (projectile.getProjectile() == ThrownPotion.class) {
            items.add(new ActionEditor.ActionItem("item",
                    ItemBuilder.create(Material.POTION)
                            .name("&eItem")
                            .info("&7Current Value", "")
                            .info(null, (item == null ? "§cNone" : "&6" + StackUtils.getDisplayName(item)))
                            .lClick(ItemBuilder.ActionType.SELECT_YELLOW),
                    ActionEditor.ActionItem.ActionType.ITEM
            ));
        }

        return new ActionEditor(4, "&eLaunch Projectile Settings", items);
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("projectile", projectile);
        data.put("amount", amount);
        data.put("direction", direction);
        if (projectile == Projectile.SPLASH_POTION) {
            data.put("item", Serialization.itemStackToBase64(item));
        }
        return data;
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

    @Override
    public String keyword() {
        return "launchProjectile";
    }
}
