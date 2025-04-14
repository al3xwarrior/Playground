package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.*;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.LocationData;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.NpcItems;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.triggers.WaypointTrigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

// TODO: look at old display item lore

@ToString
public class NpcPathAction extends Action implements NPCAction {
    public NpcPathAction() {
        super(
                "npc_navigation_action",
                "Change Npc Navigation",
                "Change the navigation of a given NPC.",
                Material.DIAMOND_AXE,
                List.of("npcNav")
        );

        getProperties().addAll(List.of(
                new NPCProperty(
                        "npcId",
                        "NPC",
                        "The NPC to change the navigation of."
                ),
                new EnumProperty<>(
                        "mode",
                        "Mode",
                        "The mode to use.",
                        NavigationType.class
                ).setValue(NavigationType.STATIONARY),
                new DoubleProperty(
                        "speed",
                        "Speed",
                        "The speed of the NPC.", 0.0, 10.0
                ).showIf(() -> getValue("mode", NavigationType.class) != NavigationType.STATIONARY).setValue(1.0),
                new DoubleProperty(
                        "delay",
                        "Delay",
                        "The stationary time between wandering.", -1.0, 100.0
                ).showIf(() -> getValue("mode", NavigationType.class) == NavigationType.WANDER).setValue(-1.0),
                new DoubleProperty(
                        "xRange",
                        "X Range",
                        "The x range of the NPC.", 0.0, 100.0
                ).showIf(() -> getValue("mode", NavigationType.class) == NavigationType.WANDER).setValue(42.0),
                new DoubleProperty(
                        "yRange",
                        "Y Range",
                        "The y range of the NPC.", 0.0, 20.0
                ).showIf(() -> getValue("mode", NavigationType.class) == NavigationType.WANDER).setValue(4.0),
                new CustomProperty<ActionProperty.Constant>(
                        "yes",
                        "Path",
                        "The path of the NPC.",
                        Material.DIAMOND_AXE
                ) {
                    @Override
                    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
                        ItemBuilder pathItem = ItemBuilder.create(Material.DIAMOND_AXE)
                                .name("&aPath")
                                .description("&bRight click &7a block too add a point\n\n&bRight click &7air to remove last point\n\n&bShift right click &7to clear the path\n\n&bShift left click &7to cancel");
                        ItemStack itemStack = pathItem.build();
                        NbtItemBuilder nbtItem = new NbtItemBuilder(itemStack);
                        nbtItem.setBoolean("isPathForAction", true);
                        nbtItem.setInt("npcId", NpcPathAction.this.getValue("npcId", Integer.class));
                        nbtItem.build();
                        player.getInventory().addItem(itemStack);
                        NpcItems.npcPathActionHashMap.put(player.getUniqueId(), NpcPathAction.this);
                    }
                }.showIf(() -> getValue("mode", NavigationType.class) == NavigationType.PATH),
                new BooleanProperty(
                        "loop",
                        "Loop",
                        "If true, the path will loop."
                ).showIf(() -> getValue("mode", NavigationType.class) == NavigationType.PATH),
                new BooleanProperty(
                        "pauseUntilComplete",
                        "Pause Until Complete",
                        "If true, the action will pause until the path is complete."
                ).showIf(() -> getValue("mode", NavigationType.class) == NavigationType.PATH),
                new ListProperty<LocationData>(
                        "path",
                        "Path",
                        "The path of the NPC.",
                        Material.PAPER,
                        36
                ).setValue(new ArrayList<>())
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Integer npcId = NpcPathAction.this.getValue("npcId", Integer.class);
        if (npcId == null) {
            return OutputType.ERROR;
        }
        return execute(house.getNPC(npcId), player, house, event, executor);
    }

    public OutputType execute(HousingNPC npc, Player player, HousingWorld house, CancellableEvent cancellable, ActionExecutor actionExecutor) {
        NavigationType mode = getValue("mode", NavigationType.class);
        npc.setNavigationType(mode);
        if (mode == NavigationType.WANDER) {
            Double speed = getValue("speed", Double.class);
            npc.setSpeed(speed);
            npc.getCitizensNPC().getNavigator().getDefaultParameters().speedModifier(speed.floatValue());
            Waypoints waypoints = npc.getCitizensNPC().getOrAddTrait(Waypoints.class);
            if (npc.getPreviousNavigationType() != NavigationType.WANDER) {
                npc.setNavigationType(NavigationType.WANDER);
                waypoints.setWaypointProvider("wander");
                waypoints.getCurrentProvider().setPaused(false);
            }

            Double delay = getValue("delay", Double.class);
            Double xRange = getValue("xRange", Double.class);
            Double yRange = getValue("yRange", Double.class);
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

            boolean pauseUntilComplete = getValue("pauseUntilComplete", Boolean.class);
            boolean loop = getValue("loop", Boolean.class);
            for (LocationData locationData: (List<LocationData>) getProperty("path", ListProperty.class).getValue()) {
                Waypoint waypoint = new Waypoint(locationData.toLocation());
                if (!loop) {
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
    public boolean requiresPlayer() {
        return false;
    }
}
