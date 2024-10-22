package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerPickupItem implements Listener {
    private HousesManager housesManager;

    public PlayerPickupItem(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDrop(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        sendEventExecution(housesManager, EventType.PLAYER_PICKUP_ITEM, player, e);
    }

    @EventHandler
    public void onDrop(PlayerPickupArrowEvent e) {
        if (e.getArrow().getMetadata("projectile").isEmpty()) return;
        e.setCancelled(true);
    }

}
