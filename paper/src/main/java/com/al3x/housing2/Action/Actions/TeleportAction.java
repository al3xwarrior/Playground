package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.LocationProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.function.BiFunction;

import static com.al3x.housing2.Enums.Locations.*;

@ToString
public class TeleportAction extends HTSLImpl implements NPCAction {
    public TeleportAction() {
        super(
                "teleport_action",
                "Teleport Player",
                "Teleports the player to a location.",
                Material.ENDER_PEARL,
                List.of("teleport")
        );

        getProperties().addAll(List.of(
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to teleport the player to."
                ).setValue("INVOKERS_LOCATION")
        ));
    }

    public TeleportAction(boolean houseSpawn) {
        this();
        if (houseSpawn) {
            getProperty("location", LocationProperty.class).setValue("HOUSE_SPAWN");
        }
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.teleport(getProperty("location", LocationProperty.class).getLocation(player, house));
        return OutputType.SUCCESS;
    }
    @Override
    public boolean requiresPlayer() {
        return true;
    }
//
//    @Override
//    public String export(int indent) {
//        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\"" : location.name();
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + loc;
//    }
//
//    @Override
//    public String syntax() {
//        return getScriptingKeywords().getFirst() + " <location>";
//    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        if (Locations.fromString(action) != null) {
//            location = Locations.fromString(action);
//            if (location == PLAYER_LOCATION) {
//                customLocation = "0,0,0";
//            }
//        } else {
//            location = CUSTOM;
//            customLocation = action;
//        }
//        return nextLines;
//    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        npc.teleport(getProperty("location", LocationProperty.class).getLocation(player,house, npc.getEntity().getLocation(), null), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
