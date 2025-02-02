package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Enums.VelocityOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SetVelocityAction extends HTSLImpl {

    private PushDirection direction;
    private VelocityOperation operation;
    private String customDirection;
    private double amount;

    public SetVelocityAction() {
        super("Set Velocity Action");
        this.direction = PushDirection.FORWARD;
        this.operation = VelocityOperation.SET;
        this.amount = 1.5;
    }

    public SetVelocityAction(Double amount, PushDirection direction, VelocityOperation operation) {
        super("Set Velocity Action");
        this.direction = direction;
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "SetVelocityAction (Amount: " + amount + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON);
        builder.name("&eSet Velocity");
        builder.info("&eSettings", "");
        builder.info("Operation", "&a" + operation);
        builder.info("Direction", "&a" + (direction == PushDirection.CUSTOM ? customDirection : direction));
        builder.info("Power", "&a" + amount);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON);
        builder.name("&aSet Velocity");
        builder.description("Propel a player in a direction.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
        builder.extraLore("&8&oThank you Home Depot"); //Yes I did add #extraLore just for this joke
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("operation",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eOperation")
                                .description("How the player's current velocity will be adjusted.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + operation)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        (event, obj) -> {
                            List<Duple<VelocityOperation, ItemBuilder>> operations = new ArrayList<>();
                            for (VelocityOperation operation : VelocityOperation.values()) {
                                operations.add(new Duple<>(operation, ItemBuilder.create(operation.getMaterial()).name("&a" + operation)));
                            }
                            new PaginationMenu<>(Main.getInstance(), "&eSelect an operation", operations, editMenu.getOwner(), house, editMenu, (operation) -> {
                                this.operation = operation;
                                editMenu.open();
                            }).open();

                            return true;
                        }
                ),
                new ActionEditor.ActionItem("direction",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eDirection")
                                .description("The direction of the velocity adjustment.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + direction)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, PushDirection.values(), Material.COMPASS,
                        (event, obj) -> getDirection(event, obj, house, editMenu, (str, dir) -> {
                            customDirection = str;
                            direction = dir;
                            editMenu.open();
                        })
                ),
                new ActionEditor.ActionItem("amount",
                        ItemBuilder.create(Material.SLIME_BALL)
                                .name("&ePower")
                                .description("The amount of force to apply to the velocity adjustment.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + amount)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 0.0, 100.0 //Pretty easy to change the max value
                )
        );

        return new ActionEditor(4, "&eSet Velocity Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Vector playerVelocity = player.getVelocity();
        Vector velocityAdjustment = new Vector();

        switch (direction) {
            case FORWARD -> velocityAdjustment.add(player.getEyeLocation().getDirection().multiply(amount));
            case BACKWARD -> velocityAdjustment.add(player.getEyeLocation().getDirection().multiply(-amount));
            case UP -> velocityAdjustment.add(new Vector(0, amount, 0));
            case DOWN -> velocityAdjustment.add(new Vector(0, -amount, 0));
            case NORTH -> velocityAdjustment.add(new Vector(0, 0, -amount));
            case SOUTH -> velocityAdjustment.add(new Vector(0, 0, amount));
            case EAST -> velocityAdjustment.add(new Vector(amount, 0, 0));
            case WEST -> velocityAdjustment.add(new Vector(-amount, 0, 0));
            case LEFT -> {
                Vector direction = player.getEyeLocation().getDirection();
                // Rotate the direction 90 degrees counterclockwise (left)
                Vector left = new Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(amount);
                velocityAdjustment.add(left);
            }
            case RIGHT -> {
                Vector direction = player.getEyeLocation().getDirection();
                // Rotate the direction 90 degrees clockwise (right)
                Vector right = new Vector(direction.getZ(), 0, -direction.getX()).normalize().multiply(amount);
                velocityAdjustment.add(right);
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
                    velocityAdjustment.add(custom);
                } catch (NumberFormatException e) {
                    return true;
                }
            }
        }
        //This way it can get two different velocities at the same time
        //ex. UP and FORWARD
        switch (operation) {
            case VelocityOperation.SET -> playerVelocity = velocityAdjustment;
            case VelocityOperation.ADD -> playerVelocity.add(velocityAdjustment);
            case VelocityOperation.SUBTRACT -> playerVelocity.subtract(velocityAdjustment);
            case VelocityOperation.MULTIPLY -> playerVelocity.multiply(velocityAdjustment);
            case VelocityOperation.DIVIDE -> playerVelocity.divide(velocityAdjustment);
            case VelocityOperation.MOD -> playerVelocity = playerVelocity.crossProduct(velocityAdjustment);
        }
        player.setVelocity(playerVelocity);
        return true;
    }

    @Override
    public int limit() {
        return 10;
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("amount", amount);
        data.put("customDirection", customDirection);
        data.put("direction", direction);
        data.put("operation", operation);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String export(int indent) {
        String dir = direction == PushDirection.CUSTOM ? customDirection : direction.name();
        String operation = this.operation.name();
        return " ".repeat(indent) + keyword() + " " + operation + " " + dir + " " + amount;
    }

    @Override
    public String keyword() {
        return "propel";
    }
}
