package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

import static com.al3x.housing2.Enums.Locations.*;

public class SpawnGhostBlock extends HTSLImpl {

    private String customLocation;
    private Locations location;
    ItemStack item;

    public SpawnGhostBlock() {
        super("Spawn Ghost Block");
        this.location = Locations.INVOKERS_LOCATION;
        this.customLocation = "";

    }

    @Override
    public String keyword() {
        return "spawnGhostBlock";
    }

    @Override
    public String toString() {
        return "SpawnGhostBlock (" + location + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHAIN_COMMAND_BLOCK);
        builder.name("&eSpawn Ghost Block");
        builder.info("&eSettings", "");
        builder.info("Location", (customLocation.isEmpty() ? "&6" + location.name() : "&6" + customLocation));
        builder.info("Item", (item == null ? "&cNone" : "&6" + item.getType()));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHAIN_COMMAND_BLOCK);
        builder.name("&aSpawn Ghost Block");
        builder.description("Spawns a client-side block for the player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionItem> items = List.of(
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
                ),
                new ActionEditor.ActionItem("item", ItemBuilder.create((item == null ? Material.STONE : item.getType()))
                        .name("&aItem")
                        .description("Select a block to set")
                        .info("&7Current Value", "")
                        .info(null, (item == null ? "&cNone" : "&6" + StackUtils.getDisplayName(item))),
                        ActionEditor.ActionItem.ActionType.ITEM
                )
        );
        return new ActionEditor(4, "&eSpawn Ghost Block Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Location loc = null;

        switch (location) {
            case INVOKERS_LOCATION -> loc = player.getLocation();
            case HOUSE_SPAWN -> loc = house.getSpawn();
            case CUSTOM, PLAYER_LOCATION -> {
                loc = getLocationFromString(player, house, customLocation);

                System.out.println(loc);

                if (loc == null) {
                    return OutputType.SUCCESS;
                }
                loc.setX(Math.max(-255, Math.min(255, loc.getX())));
                loc.setZ(Math.max(-255, Math.min(255, loc.getZ())));
                loc.setY(Math.max(-64, Math.min(320, loc.getY())));
                loc.setYaw(((loc.getYaw() + 180) % 360 + 360) % 360 - 180);
                loc.setPitch(Math.max(-90, Math.min(90, loc.getPitch())));
            }
        }

        if (loc == null) {
            return OutputType.SUCCESS;
        }

        if (item == null) {
            return OutputType.SUCCESS;
        }

        Material material = item.getType();
        if (!material.isBlock()) {
            return OutputType.SUCCESS;
        }

        player.sendBlockChange(loc, material.createBlockData());

        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        data.put("item", Serialization.itemStackToBase64(item));
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        try {
            location = valueOf((String) data.get("location"));
        } catch (IllegalArgumentException e) {
            location = INVOKERS_LOCATION;
        }
        if (data.get("customLocation") == null && location == CUSTOM) {
            customLocation = null;
            location = INVOKERS_LOCATION;
            return;
        }
        customLocation = (String) data.get("customLocation");

        try {
            item = Serialization.itemStackFromBase64((String) data.get("item"));
        } catch (IOException e) {
            e.printStackTrace();
            Main.getInstance().getLogger().warning("Failed to load item from base64 string");
        }
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\""  : location.name();
        return " ".repeat(indent) + keyword() + " " + loc + " " + item.getItemMeta().getDisplayName();
    }

    @Override
    public String syntax() {
        return "spawnGhostBlock <location> <item>";
    }
}
