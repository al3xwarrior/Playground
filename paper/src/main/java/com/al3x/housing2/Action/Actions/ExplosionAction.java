package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;
import static com.al3x.housing2.Utils.Color.colorize;

public class ExplosionAction extends HTSLImpl {
    private String customLocation;
    private Locations location;
    private double power; //Ints are not storeable anywhere

    public static HashMap<UUID, Integer> amountDone = new HashMap<>();

    public ExplosionAction() {
        super("Explosion Action");
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
        this.power = 4;
    }

    @Override
    public String toString() {
        return "Explosion Action (Location: " + (location == CUSTOM ? customLocation : location) + ", Power: " + power + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT_MINECART);
        builder.name("&eExplosion");
        builder.info("&eSettings", "");
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));
        builder.info("Power", "&a" + power);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT_MINECART);
        builder.name("&aExplosion");
        builder.description("Create an explosion at a set location.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (location == CUSTOM ? customLocation : location))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                        (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                                (coords, location) -> {
                                    customLocation = coords;
                                    this.location = location;
                                    if (location == PLAYER_LOCATION) {
                                        Location loc = player.getLocation();
                                        this.customLocation = loc.getX() + " " + loc.getY() + " " + loc.getZ();
                                    }
                                    backMenu.open();
                                }
                        )
                ),
                new ActionEditor.ActionItem("power",
                        ItemBuilder.create(Material.BOOK)
                                .name("&aPower")
                                .info("&7Current Value", "")
                                .info(null, power)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT, 0, 20
                )
        );

        return new ActionEditor(4, "&eExplosion Action Settings", items);
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        data.put("power", power);
        return data;
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
        return " ".repeat(indent) + keyword() + " " + loc + " " + power;
    }

    @Override
    public String syntax() {
        return keyword() + " <location> <power>";
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

    @Override
    public String keyword() {
        return "explosion";
    }
}
