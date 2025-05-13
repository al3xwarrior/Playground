package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

import java.util.function.BiFunction;

import static com.al3x.housing2.Utils.Color.colorize;

public class ProtoolsCoordsProperty extends ActionProperty<String> {
    public ProtoolsCoordsProperty(String id, String name, String description) {
        super(id, name, description, Material.GRASS_BLOCK);
    }

    public Location getLocation(Player player, HousingWorld house) {
        return getLocation(player, house, player.getLocation(), player.getEyeLocation());
    }

    public Location getLocation(Player player, HousingWorld house, Location baseLocation, Location eyeLocation) {
        if (getValue() == null) return null;
        if (getValue().equalsIgnoreCase("null") || getValue().equals("0")) return null;

        String value = Placeholder.handlePlaceholders(getValue(), house, player);

        String[] split = value.split(",");
        if (split.length != 3) return null;
        try {
            if (value.contains("^") && eyeLocation != null) {
                String x = split[0];
                String y = split[1];
                String z = split[2];

                double xV = (x.startsWith("^")) ? Double.parseDouble(x.substring(1)) : Double.parseDouble(x);
                double yV = (y.startsWith("^")) ? Double.parseDouble(y.substring(1)) : Double.parseDouble(y);
                double zV = (z.startsWith("^")) ? Double.parseDouble(z.substring(1)) : Double.parseDouble(z);

                //forward
                Vector forward = eyeLocation.getDirection().clone().multiply(zV);
                //right
                Vector right = eyeLocation.getDirection().clone().crossProduct(new Vector(0, 1, 0)).normalize().multiply(xV);
                //up
                Vector up = new Vector(0, yV, 0);

                return eyeLocation.clone().add(forward).add(right).add(up);
            }

            double x = 0, y = 0, z = 0;

            for (int i = 0; i < split.length; i++) {
                double handledValue = handleRelative(i, split[i], baseLocation);
                switch (i) {
                    case 0 -> x = handledValue;
                    case 1 -> y = handledValue;
                    case 2 -> z = handledValue;
                }
            }

            return new Location(baseLocation.getWorld(), x, y, z);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double handleRelative(int index, String part, Location baseLocation) {
        //Sorry if this looks like a mess :(
        double value = (part.startsWith("~")) ?
                (part.length() == 1 ? 0 : Double.parseDouble(part.substring(1))) :
                Double.parseDouble(part);

        double base = switch (index) {
            case 0 -> baseLocation.getX();
            case 1 -> baseLocation.getY();
            case 2 -> baseLocation.getZ();
            default -> 0;
        };

        if (part.startsWith("~")) {
            return base + value;
        }
        return value;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        boolean isA = getId().equals("posA");
        String value = getValue();
        if (event.isLeftClick()) {
            menu.openChat(Main.getInstance(), value, (s) -> {
                setValue(s, player);
                Bukkit.getScheduler().runTask(Main.getInstance(), menu::open);
            });
        } else if (event.getClick() == ClickType.MIDDLE) {
            Duple<Location, Location> selection = Main.getInstance().getProtoolsManager().getSelection(player);
            if (selection == null || selection.getFirst() == null) {
                player.sendMessage("&cYou must select a region first!");
                return;
            }
            if (isA) {
                setValue(String.format(
                        "%s,%s,%s",
                        selection.getFirst().getBlockX(),
                        selection.getFirst().getBlockY(),
                        selection.getFirst().getBlockZ()
                ), player);
            } else {
                setValue(String.format(
                        "%s,%s,%s",
                        selection.getSecond().getBlockX(),
                        selection.getSecond().getBlockY(),
                        selection.getSecond().getBlockZ()
                ), player);
            }
        }
    }
}
