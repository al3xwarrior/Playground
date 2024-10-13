package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class PushPlayerAction extends Action {
    private PushDirection direction;
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
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.PISTON);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&ePush Player Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Propel a player in a direction."),
                "",
                colorize("&eSettings:"),
                colorize("&fValue: " + getAmount()),
                colorize("&fDirection: " + getDirection().name()),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order."),
                "",
                colorize("&8&oThank you Home Depot")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        switch (direction) {
            case FORWARD -> player.setVelocity(player.getLocation().getDirection().multiply(amount));
            case BACKWARD -> player.setVelocity(player.getLocation().getDirection().multiply(-amount));
            case UP -> player.setVelocity(new Vector(0, amount, 0));
            case DOWN -> player.setVelocity(new Vector(0, -amount, 0));
            case NORTH -> player.setVelocity(new Vector(0, 0, -amount));
            case SOUTH -> player.setVelocity(new Vector(0, 0, amount));
            case EAST -> player.setVelocity(new Vector(amount, 0, 0));
            case WEST -> player.setVelocity(new Vector(-amount, 0, 0));
            case LEFT -> {
                Vector direction = player.getLocation().getDirection();
                // Rotate the direction 90 degrees counterclockwise (left)
                Vector left = new Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(amount);
                player.setVelocity(left);
            }
            case RIGHT -> {
                Vector direction = player.getLocation().getDirection();
                // Rotate the direction 90 degrees clockwise (right)
                Vector right = new Vector(direction.getZ(), 0, -direction.getX()).normalize().multiply(amount);
                player.setVelocity(right);
            }

        }

        return true;
    }

    public double getAmount() {
        return amount;
    }
    public PushDirection getDirection() {
        return direction;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setDirection(PushDirection direction) {
        this.direction = direction;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("direction", direction);
        return data;
    }

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("amount") || !data.containsKey("direction")) {
//            return;
//        }
//        this.amount = (double) data.get("amount");
//        this.direction = (PushDirection) data.get("direction");
//    }
}
