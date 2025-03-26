package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.*;

public class TeleportAction extends HTSLImpl implements NPCAction {
    private String customLocation;
    private Locations location;

    public TeleportAction() {
        super("Teleport Action");
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
    }

    public TeleportAction(boolean houseSpawn) {
        super("Teleport Action");
        this.customLocation = null;
        this.location = Locations.HOUSE_SPAWN;
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
        builder.description("Teleports the player to a location.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (location == CUSTOM ? customLocation : location))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                        (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                                (coords, location) -> {
                                    if (location == CUSTOM) {
                                        customLocation = coords;
                                    } else {
                                        customLocation = null;
                                    }
                                    this.location = location;
                                    if (location == PLAYER_LOCATION) {
                                        customLocation = player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ();
                                    }
                                    Bukkit.getScheduler().runTask(Main.getInstance(), backMenu::open);
                                }
                        )
                )
        );

        return new ActionEditor(4, "&eTeleport Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        switch (location) {
            case INVOKERS_LOCATION ->
                    player.teleport(player.getLocation());
            case HOUSE_SPAWN ->
                    player.teleport(house.getSpawn());
            case CUSTOM, PLAYER_LOCATION -> {
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc == null) {
                    return OutputType.SUCCESS;
                }

                loc.setX(Math.max(-255, Math.min(255, loc.getX())));
                loc.setZ(Math.max(-255, Math.min(255, loc.getZ())));
                loc.setY(Math.max(-64, Math.min(320, loc.getY())));
                loc.setYaw(Math.max(-180, Math.min(180, loc.getYaw())));
                loc.setPitch(Math.max(-90, Math.min(90, loc.getPitch())));

                player.teleport(loc);
            }
        }
        return OutputType.SUCCESS;
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
        try {
            location = Locations.valueOf((String) data.get("location"));
        } catch (IllegalArgumentException e) {
            location = INVOKERS_LOCATION;
        }
        if (data.get("customLocation") == null && location == CUSTOM) {
            customLocation = null;
            location = INVOKERS_LOCATION;
            return;
        }
        customLocation = (String) data.get("customLocation");
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\""  : location.name();
        return " ".repeat(indent) + keyword() + " " + loc;
    }

    @Override
    public String keyword() {
        return "teleport";
    }

    @Override
    public String syntax() {
        return "teleport <location>";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        if (Locations.fromString(action) != null) {
            location = Locations.fromString(action);
            if (location == PLAYER_LOCATION) {
                customLocation = "0,0,0";
            }
        } else {
            location = CUSTOM;
            customLocation = action;
        }
        return nextLines;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        switch (location) {
            case INVOKERS_LOCATION ->
                    npc.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            case HOUSE_SPAWN ->
                    npc.teleport(house.getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            case CUSTOM, PLAYER_LOCATION -> {
                if (customLocation == null) {
                    return;
                }
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc == null) {
                    return;
                }

                loc.setX(Math.max(-255, Math.min(255, loc.getX())));
                loc.setZ(Math.max(-255, Math.min(255, loc.getZ())));
                loc.setY(Math.max(-64, Math.min(320, loc.getY())));
                loc.setYaw(Math.max(-180, Math.min(180, loc.getYaw())));
                loc.setPitch(Math.max(-90, Math.min(90, loc.getPitch())));

                npc.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }

        }
    }
}
