package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import io.papermc.paper.entity.TeleportFlag;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.*;

public class TeleportAction extends HTSLImpl implements NPCAction {
    private String customLocation;
    private Locations location;
    boolean keepVelocity;

    public TeleportAction() {
        super("Teleport Action");
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
        this.keepVelocity = false;
    }

    public TeleportAction(boolean houseSpawn) {
        super("Teleport Action");
        this.customLocation = null;
        this.location = Locations.HOUSE_SPAWN;
        this.keepVelocity = false;
    }

    @Override
    public String toString() {
        return "Teleport Action (Location: " + (location == CUSTOM ? customLocation : location) + " Keep Velocity: " + (keepVelocity ? "Yes" : "No") + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_PEARL);
        builder.name("&eTeleport Player");
        builder.info("&eSettings", "");
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));
        builder.info("Keep Velocity", keepVelocity ? "&aYes" : "&cNo");

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
                ),
                new ActionEditor.ActionItem("keepVelocity", ItemBuilder.create((keepVelocity ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aKeep Velocity")
                        .info("&7Current Value", "")
                        .info(null, keepVelocity ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );

        return new ActionEditor(4, "&eTeleport Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Location loc = null;

        switch (location) {
            case INVOKERS_LOCATION -> {
                loc = player.getLocation();
            }
            case HOUSE_SPAWN -> {
                loc = house.getSpawn();
            }
            case CUSTOM, PLAYER_LOCATION -> {
                loc = getLocationFromString(player, house, customLocation);
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

        if (keepVelocity) {// Get the Bukkit Vector from the location difference
            Location deltaLoc = loc.clone().subtract(player.getLocation());

            Vec3 delta = new Vec3(deltaLoc.getX(), deltaLoc.getY(), deltaLoc.getZ());

            //System.out.println("Test: " + deltaLoc);

            PositionMoveRotation change = new PositionMoveRotation(delta, Vec3.ZERO, deltaLoc.getYaw(), deltaLoc.getPitch());

            // Create the new packet with teleport id "1" and a set of relative flags.
            ClientboundPlayerPositionPacket posPacket = new ClientboundPlayerPositionPacket(
                338,
                change,
                Set.of(Relative.X, Relative.Y, Relative.Z, Relative.DELTA_X, Relative.DELTA_Y, Relative.DELTA_Z, Relative.X_ROT, Relative.Y_ROT)
            );

            ((CraftPlayer) player).getHandle().setPos(loc.getX(), loc.getY(), loc.getZ());
            ((CraftPlayer) player).getHandle().setRot(loc.getPitch(), loc.getYaw());

            // Send the packet using the player's connection.
            ((CraftPlayer) player).getHandle().connection.send(posPacket);
        } else {
            player.teleport(loc);
        }

        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        data.put("keepVelocity", keepVelocity);
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
        keepVelocity = (boolean) data.getOrDefault("keepVelocity", false);
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\""  : location.name();
        return " ".repeat(indent) + keyword() + " " + loc + " " + keepVelocity;
    }

    @Override
    public String keyword() {
        return "teleport";
    }

    @Override
    public String syntax() {
        return "teleport <location> <keepVelocity>";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] split = action.split(" ");

        location = fromString(split[0]);

        if (location != null) {
            if (location == PLAYER_LOCATION) {
                customLocation = "0,0,0";
            }
        } else {
            location = CUSTOM;
            customLocation = action;
        }

        split = Arrays.copyOfRange(split, 1, split.length);

        if (split.length > 0) {
            keepVelocity = Boolean.parseBoolean(split[0]);
            split = Arrays.copyOfRange(split, 1, split.length);
        } else {
            keepVelocity = false;
        }

        return nextLines;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Location loc = null;

        switch (location) {
            case INVOKERS_LOCATION -> {
                loc = player.getLocation();
            }
            case HOUSE_SPAWN -> {
                loc = house.getSpawn();
            }
            case CUSTOM, PLAYER_LOCATION -> {
                if (customLocation == null) {
                    return;
                }
                loc = getLocationFromString(player, house, customLocation);
                if (loc == null) {
                    return;
                }

                loc.setX(Math.max(-255, Math.min(255, loc.getX())));
                loc.setZ(Math.max(-255, Math.min(255, loc.getZ())));
                loc.setY(Math.max(-64, Math.min(320, loc.getY())));
                loc.setYaw(((loc.getYaw() + 180) % 360 + 360) % 360 - 180);
                loc.setPitch(Math.max(-90, Math.min(90, loc.getPitch())));
            }
        }

        npc.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
