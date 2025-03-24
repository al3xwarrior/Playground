package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Enums.VelocityOperation;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.util.Vector;

import java.util.*;

public class SetVelocityAction extends HTSLImpl implements NPCAction {

    private PushDirection direction;
    private VelocityOperation operation;
    private String customDirection;
    private String amount;

    public SetVelocityAction() {
        super("Set Velocity Action");
        this.direction = PushDirection.FORWARD;
        this.operation = VelocityOperation.SET;
        this.amount = "1.5";
    }

    public SetVelocityAction(String amount, PushDirection direction, VelocityOperation operation) {
        super("Set Velocity Action");
        this.direction = direction;
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "SetVelocityAction (Operation: " + operation.toString() + ", Direction: " + direction.toString() + ", Amount: " + amount + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON)
                .name("&eSet Velocity")
                .info("&eSettings", "")
                .info("Operation", "&a" + operation)
                .info("Direction", "&a" + (direction == PushDirection.CUSTOM ? customDirection : direction))
                .info("Power", "&a" + amount)

                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PISTON)
                .name("&aSet Velocity")
                .description("Propel a player in a direction.")
                .lClick(ItemBuilder.ActionType.ADD_YELLOW)
                .extraLore("&8&oThank you Home Depot"); //Yes I did add #extraLore just for this joke
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
                        ActionEditor.ActionItem.ActionType.STRING
                )
        );

        return new ActionEditor(4, "&eSet Velocity Action Settings", items);
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            npc.getEntity().setVelocity(execute(npc.getEntity().getVelocity(), le.getEyeLocation(), player, house));
            return;
        }
        npc.getEntity().setVelocity(execute(npc.getEntity().getVelocity(), npc.getEntity().getLocation(), player, house));
    }

    public Vector execute(Vector playerVelocity, Location eyeLocation, Player player, HousingWorld house) {
        Vector velocityAdjustment = new Vector();

        double amount = 1.5;
        try {
            amount = Double.parseDouble(Placeholder.handlePlaceholders(this.amount, house, player));
        } catch (NumberFormatException e) {
            return null;
        }

        if (amount == 0) return null;
        if (amount > 100) amount = 100;
        if (amount < 0) amount = 0;

        switch (direction) {
            case FORWARD -> velocityAdjustment.add(eyeLocation.getDirection().multiply(amount));
            case BACKWARD -> velocityAdjustment.add(eyeLocation.getDirection().multiply(-amount));
            case UP -> velocityAdjustment.add(new Vector(0, 1, 0).multiply(amount));
            case DOWN -> velocityAdjustment.add(new Vector(0, -1, 0).multiply(amount));
            case NORTH -> velocityAdjustment.add(new Vector(0, 0, -1).multiply(amount));
            case SOUTH -> velocityAdjustment.add(new Vector(0, 0, 1).multiply(amount));
            case EAST -> velocityAdjustment.add(new Vector(1, 0, 0).multiply(amount));
            case WEST -> velocityAdjustment.add(new Vector(-1, 0, 0).multiply(amount));
            case LEFT -> {
                Vector direction = eyeLocation.getDirection();
                // Rotate the direction 90 degrees counterclockwise (left)
                Vector left = new Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(amount);
                velocityAdjustment.add(left);
            }
            case RIGHT -> {
                Vector direction = eyeLocation.getDirection();
                // Rotate the direction 90 degrees clockwise (right)
                Vector right = new Vector(direction.getZ(), 0, -direction.getX()).normalize().multiply(amount);
                velocityAdjustment.add(right);
            }
            case CUSTOM -> {
                if (customDirection == null) return null;

                String[] split = customDirection.split(",");
                if (split.length != 2) return null;

                try {
                    double pitch = Math.toRadians(Float.parseFloat(Placeholder.handlePlaceholders(split[0], house, player)));
                    double yaw = Math.toRadians(Float.parseFloat(Placeholder.handlePlaceholders(split[1], house, player)));

                    velocityAdjustment.add(new Vector()
                            .setX(Math.cos(pitch) * Math.sin(yaw))
                            .setY(Math.sin(pitch))
                            .setZ(Math.cos(pitch) * Math.cos(yaw))
                            .multiply(amount));
                } catch (NumberFormatException e) {
                    return null;
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
        }
        return playerVelocity;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Vector playerVelocity = execute(player.getVelocity(), player.getEyeLocation(), player, house);
        player.setVelocity(playerVelocity);
        return OutputType.SUCCESS;
    }

    @Override
    public int limit() {
        return 10;
    }

    public String getAmount() {
        return amount;
    }

    public PushDirection getDirection() {
        return direction;
    }

    //Won't do anything since it doesn't use this function
    public void setAmount(String amount) {
        this.amount = amount;
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
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        this.amount = data.get("amount").toString();
        this.customDirection = (String) data.get("customDirection");
        this.direction = PushDirection.valueOf(data.get("direction").toString());
        this.operation = VelocityOperation.valueOf(data.get("operation").toString());
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String syntax() {
        return "velocity <operation> <direction> <amount>";
    }

    @Override
    public String export(int indent) {
        String dir = direction == PushDirection.CUSTOM ? customDirection : direction.name();
        String operation = this.operation.asString();
        return " ".repeat(indent) + keyword() + " " + operation + " " + dir + " " + amount;
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] split = action.split(" ");

        operation = VelocityOperation.fromString(split[0]);
        Duple<String[], String> directionArg = handleArg(split, 1);
        if (PushDirection.fromString(directionArg.getSecond()) == null) {
            customDirection = directionArg.getSecond();
            direction = PushDirection.CUSTOM;
        } else {
            direction = PushDirection.fromString(directionArg.getSecond());
        }
        split = directionArg.getFirst();

        if (split.length == 0) {
            return nextLines;
        }

        Duple<String[], String> amountArg = handleArg(directionArg.getFirst(), 0);
        amount = amountArg.getSecond();
        split = amountArg.getFirst();

        return new ArrayList<>(Arrays.asList(split));
    }

    @Override
    public String keyword() {
        return "velocity";
    }
}
