package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import static com.al3x.housing2.Utils.Color.colorize;

public class EntityLimitListener implements Listener {

    private final int limit = 150;

    private void alertStaff(World world) {
        if (world.getEntities().size() > limit + 5) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                HousingWorld house = Main.getInstance().getHousesManager().getHouse(world);
                if (player.hasPermission("housing2.admin")) {
                    player.sendMessage(colorize("&cEntity limit reached in " + house.getName() + " &7(" + house.getOwnerName() + ")"));
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason reason = e.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.setCancelled(true); // I dont "THINK" natural spawns are needed
            return;
        }

        World world = e.getEntity().getWorld();

        if (world.getEntities().size() >= limit) {
            // Events that are either small and shouldnt be denied or part of Housing2 or another plugin
            // CURED, CUSTOM, ENDER_PEARL
            if (
                    reason == CreatureSpawnEvent.SpawnReason.SPAWNER ||
                    reason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG ||
                    reason == CreatureSpawnEvent.SpawnReason.TRIAL_SPAWNER ||
                    reason == CreatureSpawnEvent.SpawnReason.SILVERFISH_BLOCK ||
                    reason == CreatureSpawnEvent.SpawnReason.TRAP
            ) {
                e.setCancelled(true);
            }

            alertStaff(world);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e) {
        World world = e.getPlayer().getWorld();
        if (world.getEntities().size() >= limit) {
            e.setCancelled(true);
            alertStaff(world);
        }
    }

    @EventHandler
    public void blockDrop(BlockBreakEvent e) {
        World world = e.getPlayer().getWorld();
        if (world.getEntities().size() >= limit) {
            e.setDropItems(false);
            alertStaff(world);
        }
    }
    @EventHandler
    public void blockDispenseItem(BlockDispenseEvent e) {
        World world = e.getBlock().getWorld();
        if (world.getEntities().size() >= limit) {
            e.setCancelled(true);
            alertStaff(world);
        }
    }

}
