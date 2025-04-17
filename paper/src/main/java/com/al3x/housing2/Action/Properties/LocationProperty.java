package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.HandlePlaceholders;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

import static com.al3x.housing2.Utils.Color.colorize;

public class LocationProperty extends ActionProperty<String> {
    public LocationProperty(String id, String name, String description) {
        super(id, name, description, Material.ENDER_PEARL);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new EnumMenu<>(main,
                "Select Location",
                Locations.values(),
                Material.BARRIER, player, house, menu,
                (location) -> {
                    if (location == Locations.CUSTOM) {
                        player.sendMessage(colorize("&ePlease enter the custom location in the chat. (x,y,z) or (x,y,z,yaw,pitch)"));
                        menu.openChat(main, getValue(), (message) -> {
                            String customLocation = message;
                            message = HandlePlaceholders.parsePlaceholders(player, house, message);
                            String[] split = message.split(",");
                            if (split.length != 3 && split.length != 5) {
                                player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
                                return;
                            }

                            try {
                                for (int i = 0; i < split.length; i++) {
                                    if (split[i].startsWith("~")) {
                                        split[i] = split[i].substring(1);
                                    }

                                    if (split[i].startsWith("^")) {
                                        split[i] = split[i].substring(1);
                                    }

                                    if (split[i].isEmpty()) continue;
                                    if (split[i].equalsIgnoreCase("null")) {
                                        split[i] = "0";
                                    }

                                    Float.parseFloat(split[i]);
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
                                return;
                            }
                            setValue(customLocation);
                        });
                    } else if (location == Locations.PLAYER_LOCATION) {
                        Location loc = player.getLocation();
                        setValue(loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch());
                    } else {
                        setValue(location.name());
                    }
                }
        ).open();
    }

    public Location getLocation(Player player, HousingWorld house) {
        return getLocation(player, house, player.getLocation(), player.getEyeLocation());
    }

    public Location getLocation(Player player, HousingWorld house, Location baseLocation, Location eyeLocation) {
        if (getValue() == null) return null;
        if (getValue().equalsIgnoreCase("null") || getValue().equals("0")) return null;

        String value = Placeholder.handlePlaceholders(getValue(), house, player);

        if (Locations.fromString(value) != null) {
            return switch (Locations.fromString(value)) {
                case CUSTOM -> null;
                case HOUSE_SPAWN -> house.getSpawn();
                case PLAYER_LOCATION -> null;
                case INVOKERS_LOCATION -> player.getLocation();
            };
        }

        String[] split = value.split(",");
        if (split.length != 3 && split.length != 5) return null;
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

                return baseLocation.clone().add(forward).add(right).add(up);
            }

            double x = 0, y = 0, z = 0, yaw = baseLocation.getYaw(), pitch = baseLocation.getPitch();

            for (int i = 0; i < split.length; i++) {
                double handledValue = handleRelative(i, split[i], baseLocation);
                switch (i) {
                    case 0 -> x = handledValue;
                    case 1 -> y = handledValue;
                    case 2 -> z = handledValue;
                    case 3 -> yaw = handledValue;
                    default -> pitch = handledValue;
                }
            }

            return new Location(baseLocation.getWorld(), x, y, z, (float) yaw, (float) pitch);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
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
            case 3 -> baseLocation.getYaw();
            case 4 -> baseLocation.getPitch();
            default -> 0;
        };

        if (part.startsWith("~")) {
            return base + value;
        }
        return value;
    }
}
