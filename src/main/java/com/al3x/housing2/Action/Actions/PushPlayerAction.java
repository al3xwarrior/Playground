package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class PushPlayerAction extends Action {
    private PushDirection direction;
    private String customDirection;
    private double amount;

    public PushPlayerAction() {
        super("Push Player Action");
        this.direction = PushDirection.FORWARD;
        this.amount = 1.5;
    }

    public PushPlayerAction(Double amount, PushDirection direction) {
        super("Push Player Action");
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PushPlayerAction (Amount: " + amount + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON);
        builder.name("&ePush Player Action");
        builder.info("&eSettings", "");
        builder.info("Direction", "&a" + (direction == PushDirection.CUSTOM ? customDirection : direction));
        builder.info("Velocity", "&a" + amount);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON);
        builder.name("&aPush Player Action");
        builder.description("Propel a player in a direction.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
        builder.extraLore("&8&oThank you Home Depot"); //Yes I did add #extraLore just for this joke
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
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

        return new ActionEditor(4, "&ePush Player Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Vector playerVelocity = new Vector();
        switch (direction) {
            case FORWARD -> playerVelocity.add(player.getEyeLocation().getDirection().multiply(amount));
            case BACKWARD -> playerVelocity.add(player.getEyeLocation().getDirection().multiply(-amount));
            case UP -> playerVelocity.add(new Vector(0, amount, 0));
            case DOWN -> playerVelocity.add(new Vector(0, -amount, 0));
            case NORTH -> playerVelocity.add(new Vector(0, 0, -amount));
            case SOUTH -> playerVelocity.add(new Vector(0, 0, amount));
            case EAST -> playerVelocity.add(new Vector(amount, 0, 0));
            case WEST -> playerVelocity.add(new Vector(-amount, 0, 0));
            case LEFT -> {
                Vector direction = player.getEyeLocation().getDirection();
                // Rotate the direction 90 degrees counterclockwise (left)
                Vector left = new Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(amount);
                playerVelocity.add(left);
            }
            case RIGHT -> {
                Vector direction = player.getEyeLocation().getDirection();
                // Rotate the direction 90 degrees clockwise (right)
                Vector right = new Vector(direction.getZ(), 0, -direction.getX()).normalize().multiply(amount);
                playerVelocity.add(right);
            }
            case CUSTOM -> {
                if (customDirection == null) {
                    return true;
                }
                String[] split = customDirection.split(",");
                if (split.length != 2) {
                    return true;
                }

                try {
                    float pitch = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[0]));
                    float yaw = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[1]));
                    Vector custom = player.getEyeLocation().getDirection().setY(0).normalize().multiply(amount);
                    custom = custom.setY(Math.sin(Math.toRadians(pitch)) * amount);
                    custom = custom.setX(custom.getX() * Math.cos(Math.toRadians(yaw)));
                    custom = custom.setZ(custom.getZ() * Math.sin(Math.toRadians(yaw)));
                    playerVelocity.add(custom);
                } catch (NumberFormatException e) {
                    return true;
                }
            }
        }
        //This way it can get two different velocities at the same time
        //ex. UP and FORWARD
        player.setVelocity(playerVelocity);
        return true;
    }

    public double getAmount() {
        return amount;
    }

    public PushDirection getDirection() {
        return direction;
    }

    //Won't do anything since it doesn't use this function
    public void setAmount(double amount) {
        this.amount = Math.max(amount, 100.0); // cant go over 100
    }

    public void setDirection(PushDirection direction) {
        this.direction = direction;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("customDirection", customDirection);
        data.put("direction", direction);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
