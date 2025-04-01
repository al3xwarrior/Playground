package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.function.BiFunction;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;
import static com.al3x.housing2.Utils.Color.colorize;

public class ExplosionAction extends HTSLImpl {
    private String customLocation = null;
    private Locations location = Locations.INVOKERS_LOCATION;
    private double power = 4; //Ints are not storeable anywhere

    public static HashMap<UUID, Integer> amountDone = new HashMap<>();

    public ExplosionAction() {
        super(
                "explosion_action",
                "Explosion",
                "Creates an explosion at a set location.",
                Material.TNT_MINECART,
                List.of("explosion")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "location",
                        "Location",
                        "The location to create the explosion at.",
                        ActionProperty.PropertyType.ENUM, Locations.class,
                        this::locationConsumer
                ),
                new ActionProperty(
                        "power",
                        "Power",
                        "The power of the explosion.",
                        ActionProperty.PropertyType.DOUBLE, 0, 20
                )
        ));
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> locationConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                (coords, location) -> {
                    customLocation = coords;
                    this.location = location;
                    if (location == PLAYER_LOCATION) {
                        Location loc = player.getLocation();
                        this.customLocation = loc.getX() + " " + loc.getY() + " " + loc.getZ();
                    }
                    backMenu.open();
                }
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        World world = house.getWorld();

        if (player != null) {
            if (amountDone.containsKey(player.getUniqueId())) {
                int amount = amountDone.get(player.getUniqueId());
                if (amount >= 5) {
                    if (amount == 5) {
                        player.sendMessage(colorize("&cYou have reached the limit of 5 explosions per tick!"));
                    }
                    amountDone.put(player.getUniqueId(), amount + 1);
                    return OutputType.SUCCESS;
                }
                amountDone.put(player.getUniqueId(), amount + 1);
            } else {
                amountDone.put(player.getUniqueId(), 1);
            }
        } else {
            if (amountDone.containsKey(house.getHouseUUID())) {
                int amount = amountDone.get(house.getHouseUUID());
                if (amount >= 5) {
                    return OutputType.SUCCESS;
                }
                amountDone.put(house.getHouseUUID(), amount + 1);
            } else {
                amountDone.put(house.getHouseUUID(), 1);
            }
        }

        if (power < 0) {
            power = 0;
        }

        if (power > 20) {
            power = 20;
        }

        switch (location) {
            case INVOKERS_LOCATION -> {
                if (player == null) return OutputType.ERROR;
                world.createExplosion(player.getLocation(), (float) power, false, false);
            }
            case HOUSE_SPAWN -> {
                if (house.getSpawn() == null) return OutputType.ERROR;
                world.createExplosion(house.getSpawn(), (float) power, false, false);
            }
            case CUSTOM, PLAYER_LOCATION -> {
                if (customLocation == null) return OutputType.ERROR;
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc != null) {
                    world.createExplosion(loc, (float) power, false, false);
                }
            }
        }
        return OutputType.ERROR;
    }

    @Override
    public int limit() {
        return 1;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        customLocation = (String) data.get("customLocation");
        location = Locations.valueOf((String) data.get("location"));
        power = (double) data.get("power");
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? customLocation : location.name();
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + loc + " " + power;
    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <location> <power>";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] args = action.split(" ");
        if (args.length < 2) return nextLines;

        Duple<String[], String> locationArg = handleArg(args, 0);
        if (Locations.fromString(locationArg.getSecond()) == null) {
            location = CUSTOM;
            customLocation = locationArg.getSecond();
        } else {
            location = Locations.fromString(locationArg.getSecond());
        }
        args = locationArg.getFirst();

        if (args.length == 0) return nextLines;

        try {
            power = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            return nextLines;
        }

        return nextLines;
    }
}
