package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;

public class TeleportAction extends HTSLImpl {
    private String customLocation;
    private Locations location;

    public TeleportAction() {
        super("Teleport Action");
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
    }

    @Override
    public String toString() {
        return "Teleport Action (Location: " + (location == CUSTOM ? customLocation : location) + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_PEARL);
        builder.name("&eTeleport Player");
        builder.info("&eSettings", "");
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_PEARL);
        builder.name("&aTeleport Player");
        builder.description("Apply this action to teleport the player.");
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
                                    this.location = CUSTOM;
                                    if (location == PLAYER_LOCATION) {
                                        Location loc = player.getLocation();
                                        this.customLocation = loc.getX() + " " + loc.getY() + " " + loc.getZ();
                                    }
                                }
                        )
                )
        );

        return new ActionEditor(4, "&eTeleport Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        switch (location) {
            case INVOKERS_LOCATION ->
                    player.teleport(player.getLocation());
            case HOUSE_SPAWN ->
                    player.teleport(house.getSpawn());
            case CUSTOM, PLAYER_LOCATION -> {
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc == null) {
                    return true;
                }
                player.teleport(loc);
            }

        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("location", location.name());
        data.put("customLocation", customLocation);
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
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? customLocation : location.name();
        return " ".repeat(indent) + keyword() + " " + loc;
    }

    @Override
    public String keyword() {
        return "teleport";
    }
}
