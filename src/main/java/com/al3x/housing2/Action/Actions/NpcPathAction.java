package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousingData.LocationData;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Listeners.NpcItems;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import com.google.gson.Gson;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Action.ActionEditor.ActionItem.ActionType.CUSTOM;

public class NpcPathAction extends Action {
    int npcId;
    NavigationType mode;
    Double speed = null;
    Double delay = null;
    Double xRange = null;
    Double yRange = null;
    List<LocationData> path = null;

    public NpcPathAction() {
        super("Change Npc Navigation Action");
        npcId = -1;
        mode = NavigationType.STATIONARY;
    }

    @Override
    public String toString() {
        return "NpcPathAction{" +
                "npcId=" + npcId +
                ", mode=" + mode +
                ", speed=" + speed +
                ", delay=" + delay +
                ", xRange=" + xRange +
                ", yRange=" + yRange +
                ", path=" + path +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        //Do nothing
    }

    public List<LocationData> getPath() {
        return path;
    }

    @Override
    public void createDisplayItem(ItemBuilder builder, HousingWorld house) {
        builder.material(Material.DIAMOND_AXE);
        builder.name("&eChange NPC Navigation");
        builder.description("Change the navigation of a given NPC");
        builder.info("&eSettings", "");
        builder.info("NPC", (npcId == -1 ? "&cNone" : "&6" + (house.getNPC(npcId) == null ? "Unknown NPC" : house.getNPC(npcId).getName())));
        builder.info("Mode", mode.name());
        if (speed != null) {
            builder.info("Speed", speed + "");
        }
        if (delay != null) {
            builder.info("Delay", delay + "");
        }
        if (xRange != null) {
            builder.info("X Range", xRange + "");
        }
        if (yRange != null) {
            builder.info("Y Range", yRange + "");
        }
        if (path != null) {
            builder.info("Path", path.size() + " points");
        }
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DIAMOND_AXE);
        builder.name("&aChange NPC Navigation");
        builder.description("Change the navigation of a given NPC");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        HousingNPC npc = house.getNPC(npcId);

        ItemBuilder itemBuilder = ItemBuilder.create(Material.PLAYER_HEAD)
                .name("&aNPC")
                .description("Select a NPC to change the navigation of")
                .info("&7Current Value", "")
                .info(null, (npc == null ? "&cNone" : "&6" + npc.getName()));

        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.add(new ActionEditor.ActionItem("npcId", itemBuilder,
                ActionEditor.ActionItem.ActionType.NPC
        ));

        items.add(new ActionEditor.ActionItem("mode",
                ItemBuilder.create(Material.COMPASS)
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + mode)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, NavigationType.values(), null
        ));

        if (mode != NavigationType.STATIONARY) {
            if (speed == null) {
                speed = 1.0;
            }
            items.add(new ActionEditor.ActionItem("speed",
                    ItemBuilder.create(Material.SUGAR)
                            .name("&eSpeed")
                            .info("&7Current Value", "")
                            .info(null, speed)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.DOUBLE, 0.1, 10.0
            ));
        }

        if (mode == NavigationType.WANDER) {
            if (delay == null) {
                delay = -1.0;
            }
            items.add(new ActionEditor.ActionItem("delay",
                    ItemBuilder.create(Material.CLOCK)
                            .name("&eDelay")
                            .info("&7Current Value", "")
                            .info(null, delay)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.DOUBLE, -1.0, 100.0
            ));

            if (xRange == null) {
                xRange = 25.0;
            }
            if (yRange == null) {
                yRange = 3.0;
            }

            items.add(new ActionEditor.ActionItem("xRange",
                    ItemBuilder.create(Material.COMPASS)
                            .name("&eX Range")
                            .info("&7Current Value", "")
                            .info(null, xRange)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.DOUBLE, 0.0, 100.0
            ));

            items.add(new ActionEditor.ActionItem("yRange",
                    ItemBuilder.create(Material.COMPASS)
                            .name("&eY Range")
                            .info("&7Current Value", "")
                            .info(null, yRange)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.DOUBLE, 0.0, 20.0
            ));
        }

        if (mode == NavigationType.PATH) {
            if (path == null) {
                path = new ArrayList<>();
            }
            items.add(new ActionEditor.ActionItem("path",
                    ItemBuilder.create(Material.DIAMOND_AXE)
                            .name("&aPath")
                            .description("&bRight click &7a block too add a point\n\n&bRight click &7air to remove last point\n\n&bShift right click &7to clear the path\n\n&bShift left click &7to cancel")
                            .lClick(ItemBuilder.ActionType.GIVE_ITEM),
                    (e, o) -> {
                        ItemBuilder pathItem = ItemBuilder.create(Material.DIAMOND_AXE)
                                .name("&aPath")
                                .description("&bRight click &7a block too add a point\n\n&bRight click &7air to remove last point\n\n&bShift right click &7to clear the path\n\n&bShift left click &7to cancel");
                        ItemStack itemStack = pathItem.build();
                        NbtItemBuilder nbtItem = new NbtItemBuilder(itemStack);
                        nbtItem.setBoolean("isPathForAction", true);
                        nbtItem.setInt("npcId", npcId);
                        nbtItem.build();
                        player.getInventory().addItem(itemStack);
                        NpcItems.npcPathActionHashMap.put(player.getUniqueId(), this);
                        return true;
                    }
            ));
            ItemBuilder paths = ItemBuilder.create(Material.PAPER)
                    .name("&aPath Locations")
                    .description("All the locations in the path")
                    .rClick(ItemBuilder.ActionType.CLEAR);
            for (int i = 0; i < path.size(); i++) {
                LocationData locationData = path.get(i);
                paths.info(null, "&8" + (i + 1) + ". &a" + locationData.getX() + ", " + locationData.getY() + ", " + locationData.getZ());
            }
            items.add(new ActionEditor.ActionItem(paths, CUSTOM, 32, (e, o) -> {
                if (e.getClick().isRightClick()) {
                    path.clear();
                    return true;
                }
                return false;
            }));
        }

        return new ActionEditor(4, "&eNpc Path Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        HousingNPC npc = house.getNPC(npcId);
        if (npc == null) {
            return true;
        }

        npc.setNavigationType(mode);
        if (mode == NavigationType.WANDER) {
            npc.setSpeed(speed);
            npc.getCitizensNPC().getNavigator().getDefaultParameters().speedModifier(speed.floatValue());

            Waypoints waypoints = npc.getCitizensNPC().getOrAddTrait(Waypoints.class);
            if (npc.getPreviousNavigationType() != NavigationType.WANDER) {
                npc.setNavigationType(NavigationType.WANDER);
                waypoints.setWaypointProvider("wander");
                waypoints.getCurrentProvider().setPaused(false);
            }

            WanderWaypointProvider wander = (WanderWaypointProvider) waypoints.getCurrentProvider();
            wander.setDelay(delay.intValue());
            wander.setXYRange(xRange.intValue(), yRange.intValue());
        }

        if (mode == NavigationType.STATIONARY) {
            if (npc.getPreviousNavigationType() != NavigationType.STATIONARY) {
                npc.setNavigationType(NavigationType.STATIONARY);
                Waypoints waypoints = npc.getCitizensNPC().getOrAddTrait(Waypoints.class);
                waypoints.getCurrentProvider().setPaused(true);
            }
        }

        if (mode == NavigationType.PATH) {
            if (npc.getPreviousNavigationType() != NavigationType.PATH) {
                npc.setNavigationType(NavigationType.PATH);
                Waypoints waypoints = npc.getCitizensNPC().getOrAddTrait(Waypoints.class);
                waypoints.setWaypointProvider("linear");
                waypoints.getCurrentProvider().setPaused(false);
            }

            Waypoints waypoints = npc.getCitizensNPC().getOrAddTrait(Waypoints.class);
            LinearWaypointProvider provider = (LinearWaypointProvider) waypoints.getCurrentProvider();
            List<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
            path.clear();
            for (LocationData locationData : this.path) {
                path.add(new Waypoint(locationData.toLocation()));
            }
        }
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("npcId", npcId);
        data.put("mode", mode.name());
        if (speed != null) {
            data.put("speed", speed);
        }
        if (delay != null) {
            data.put("delay", delay);
        }
        if (xRange != null) {
            data.put("xRange", xRange);
        }
        if (yRange != null) {
            data.put("yRange", yRange);
        }
        if (path != null) {
            data.put("path", path);
        }
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        npcId = ((Double)data.get("npcId")).intValue();
        mode = NavigationType.valueOf((String) data.get("mode"));
        if (data.containsKey("speed")) {
            speed = (Double) data.get("speed");
        }
        if (data.containsKey("delay")) {
            delay = (Double) data.get("delay");
        }
        if (data.containsKey("xRange")) {
            xRange = (Double) data.get("xRange");
        }
        if (data.containsKey("yRange")) {
            yRange = (Double) data.get("yRange");
        }
        if (data.containsKey("path")) {
            for (Object locationData : (List<?>) data.get("path")) {
                if (path == null) {
                    path = new ArrayList<>();
                }
                path.add(new Gson().fromJson(locationData.toString(), LocationData.class));
            }
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
