package com.al3x.housing2.Listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityLimitListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason reason = e.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.setCancelled(true); // I dont "THINK" natural spawns are needed
            return;
        }

        World world = e.getEntity().getWorld();

        if (world.getEntities().size() >= 200) {
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
        }
    }

}
