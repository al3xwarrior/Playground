package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.LocationData;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.NpcItems;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import com.google.gson.Gson;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.triggers.WaypointTrigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.al3x.housing2.Action.ActionEditor.ActionItem.ActionType.CUSTOM;

public class NpcPathAction extends Action implements NPCAction {
    int npcId;
    NavigationType mode;
    Double speed = null;
    Double delay = null;
    Double xRange = null;
    Double yRange = null;
    List<LocationData> path = null;
    boolean loop = false;
    boolean pauseUntilComplete = false;

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
        builder.info("&eSettings", "");
        builder.info("NPC", (npcId == -1 ? "&cNone" : "&6" + (house.getNPC(npcId) == null ? "Unknown NPC" : house.getNPC(npcId).getName())));
        builder.info("Mode", mode.name());
        if (mode != NavigationType.STATIONARY) {
            if (speed == null) {
                speed = 1.0;
            }
            builder.info("Speed", speed);
        }
        if (mode == NavigationType.WANDER) {
            if (delay == null) {
                delay = -1.0;
            }
            if (xRange == null) {
                xRange = 25.0;
            }
            if (yRange == null) {
                yRange = 3.0;
            }
            builder.info("Delay", delay);
            builder.info("X Range", xRange);
            builder.info("Y Range", yRange);
        }
        if (mode == NavigationType.PATH) {
            if (path == null) {
                path = new ArrayList<>();
            }
            builder.info("Path", path.size() + " points");
            builder.info("Loop", loop ? "&aYes" : "&cNo");
            builder.info("Pause Until Complete", pauseUntilComplete ? "&aYes" : "&cNo");
        }
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DIAMOND_AXE);
        builder.name("&aChange NPC Navigation");
        builder.description("Change the navigation of a given NPC.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        HousingNPC npc = house.getNPC(npcId);

        ItemBuilder itemBuilder = ItemBuilder.create(Material.PLAYER_HEAD)
                .name("&aNPC")
                .description("Select a NPC to change the navigation of\n\nIf you are running this within a run as npc action, the npc will be the npc that the action is running as")
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
            if (xRange == null) {
                xRange = 25.0;
            }
            if (yRange == null) {
                yRange = 3.0;
            }
            items.add(new ActionEditor.ActionItem("delay",
                    ItemBuilder.create(Material.CLOCK)
                            .name("&eDelay")
                            .info("&7Current Value", "")
                            .info(null, delay)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.DOUBLE, -1.0, 100.0
            ));

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
            items.add(new ActionEditor.ActionItem("loop",
                    ItemBuilder.create(Material.REDSTONE_TORCH)
                            .name("&eLoop")
                            .info("&7Current Value", "")
                            .info(null, loop ? "&aYes" : "&cNo")
                            .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW),
                    ActionEditor.ActionItem.ActionType.BOOLEAN
            ));
            items.add(new ActionEditor.ActionItem("pauseUntilComplete",
                    ItemBuilder.create(Material.REDSTONE_TORCH)
                            .name("&ePause Until Complete")
                            .info("&7Current Value", "")
                            .info(null, pauseUntilComplete ? "&aYes" : "&cNo")
                            .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW),
                    ActionEditor.ActionItem.ActionType.BOOLEAN
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
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return execute(house.getNPC(npcId), player, house, event, executor);
    }

    public OutputType execute(HousingNPC npc, Player player, HousingWorld house, CancellableEvent cancellable, ActionExecutor actionExecutor) {
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
                Waypoint waypoint = new Waypoint(locationData.toLocation());
                if (!this.loop) {
                    waypoint.addTrigger(new WaypointTrigger() {
                        @Override
                        public String description() {
                            return "remove this waypoint when reached";
                        }

                        @Override
                        public void onWaypointReached(NPC npc, Location location) {
                            Waypoints waypoints = npc.getOrAddTrait(Waypoints.class);
                            LinearWaypointProvider provider = (LinearWaypointProvider) waypoints.getCurrentProvider();
                            List<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
                            path.remove(waypoint);

                            if (path.isEmpty()) {
                                if (pauseUntilComplete) {
                                    actionExecutor.setPaused(false);
                                }
                            }
                        }
                    });
                }
                path.add(waypoint);
            }
            if (!path.isEmpty()) {
                if (pauseUntilComplete) {
                    actionExecutor.setPaused(true);

                    return OutputType.PAUSE;
                }
            }
        }
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        execute(house.getNPC(npc.getId()), player, house, event, executor);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("npcId", npcId);
        data.put("mode", mode.name());
        if (mode != NavigationType.STATIONARY) {
            data.put("speed", speed);
        }
        if (mode == NavigationType.WANDER) {
            data.put("delay", delay);
            data.put("xRange", xRange);
            data.put("yRange", yRange);
        }
        if (mode == NavigationType.PATH) {
            data.put("path", path);
            data.put("loop", loop);
            data.put("pauseUntilComplete", pauseUntilComplete);
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
        if (data.containsKey("loop")) {
            loop = (boolean) data.get("loop");
        }
        if (data.containsKey("pauseUntilComplete")) {
            pauseUntilComplete = (boolean) data.get("pauseUntilComplete");
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
