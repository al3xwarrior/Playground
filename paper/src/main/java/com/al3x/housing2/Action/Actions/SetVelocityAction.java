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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.util.Vector;

import java.util.*;

@ToString
@Getter
@Setter
public class SetVelocityAction extends HTSLImpl implements NPCAction {

    private PushDirection direction = PushDirection.FORWARD;
    private VelocityOperation operation = VelocityOperation.SET;
    private String customDirection = null;
    private String amount = "1.5";

    public SetVelocityAction() {
        super(
                "set_velocity_action",
                "Set Velocity",
                "Sets the velocity of the player.",
                Material.PISTON,
                List.of("velocity")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "direction",
                        "Direction",
                        "The direction of the velocity adjustment.",
                        ActionProperty.PropertyType.ENUM, PushDirection.class
                ),
                new ActionProperty(
                        "operation",
                        "Operation",
                        "How the player's current velocity will be adjusted.",
                        ActionProperty.PropertyType.ENUM, VelocityOperation.class
                ),
                new ActionProperty(
                        "amount",
                        "Power",
                        "The amount of force to apply to the velocity adjustment.",
                        ActionProperty.PropertyType.NUMBER, 0.0, 100.0
                )
        ));
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

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <operation> <direction> <amount>";
    }

    @Override
    public String export(int indent) {
        String dir = direction.name();
        String operation = this.operation.asString();
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + operation + " " + dir + " " + amount;
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] split = action.split(" ");

        operation = VelocityOperation.fromString(split[0]);
        Duple<String[], String> directionArg = handleArg(split, 1);
        direction = PushDirection.fromString(directionArg.getSecond());

        split = directionArg.getFirst();

        if (split.length == 0) {
            return nextLines;
        }

        Duple<String[], String> amountArg = handleArg(directionArg.getFirst(), 0);
        amount = amountArg.getSecond();
        split = amountArg.getFirst();

        return new ArrayList<>(Arrays.asList(split));
    }
}
