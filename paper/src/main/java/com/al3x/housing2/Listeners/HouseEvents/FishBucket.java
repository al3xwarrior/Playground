package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFishEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class FishBucket implements Listener {
    private HousesManager housesManager;

    public FishBucket(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishBucket(PlayerBucketEntityEvent e) {
        if (!housesManager.hasPermissionInHouse(e.getPlayer(), Permissions.ENTITY_BUCKET)) {
            e.setCancelled(true);
        }
        sendEventExecution(housesManager, EventType.ENTITY_BUCKET, (Player) e.getEntity(), e);
    }
}
