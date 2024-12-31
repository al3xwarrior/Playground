package com.al3x.housing2.Listeners.HouseEvents.Permissions;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;


public class OpenSomething implements Listener {

    private HousesManager housesManager;

    public OpenSomething(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        if (!(e.getPlayer() instanceof Player player)) {return;}

        if (housesManager.getHouse(player.getWorld()) == null) {return;}

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (!e.hasBlock() || e.getClickedBlock() == null) return;

        if (!house.hasPermission(player, Permissions.IRON_DOOR) && e.getClickedBlock().getType() == org.bukkit.Material.IRON_DOOR) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        Material[] doors = {Material.OAK_DOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.DARK_OAK_DOOR, Material.CRIMSON_DOOR, Material.WARPED_DOOR, Material.MANGROVE_DOOR, Material.CHERRY_DOOR, Material.BAMBOO_DOOR};
        if (!house.hasPermission(player, Permissions.WOOD_DOOR) && Arrays.stream(doors).anyMatch(door -> e.getClickedBlock().getType() == door)) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        if (!house.hasPermission(player, Permissions.IRON_TRAPDOOR) && e.getClickedBlock().getType() == Material.IRON_TRAPDOOR) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        Material[] trapdoors = {Material.OAK_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.WARPED_TRAPDOOR, Material.MANGROVE_TRAPDOOR, Material.CHERRY_TRAPDOOR, Material.BAMBOO_TRAPDOOR};
        if (!house.hasPermission(player, Permissions.WOOD_TRAPDOOR) && Arrays.stream(trapdoors).anyMatch(trapdoor -> e.getClickedBlock().getType() == trapdoor)) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        Material[] fenceGates = {Material.OAK_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.MANGROVE_FENCE_GATE, Material.CHERRY_FENCE_GATE, Material.BAMBOO_FENCE_GATE};
        if (!house.hasPermission(player, Permissions.FENCE_GATE) && Arrays.stream(fenceGates).anyMatch(fencegate -> e.getClickedBlock().getType() == fencegate)) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        Material[] button = {Material.STONE_BUTTON, Material.OAK_BUTTON, Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.SPRUCE_BUTTON, Material.CRIMSON_BUTTON, Material.WARPED_BUTTON, Material.MANGROVE_BUTTON, Material.CHERRY_BUTTON, Material.BAMBOO_BUTTON};
        if (!house.hasPermission(player, Permissions.BUTTON) && Arrays.stream(button).anyMatch(b -> e.getClickedBlock().getType() == b)) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        if (!house.hasPermission(player, Permissions.LEVER) && e.getClickedBlock().getType() == Material.LEVER) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        if (!house.hasPermission(player, Permissions.USE_CHESTS) && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        if (!house.hasPermission(player, Permissions.USE_ENDER_CHESTS) && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }

        if (!house.hasPermission(player, Permissions.USE_SHULKERS) && e.getClickedBlock().getType().name().contains("SHULKER_BOX")) {
            e.setCancelled(true);
            player.sendMessage("&cYou do not have permission to open this!");
        }
    }
}