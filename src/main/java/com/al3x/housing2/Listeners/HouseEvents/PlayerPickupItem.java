package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerPickupItem implements Listener {
    private HousesManager housesManager;

    public PlayerPickupItem(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDrop(PlayerPickupItemEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_PICKUP_ITEM, e.getPlayer(), e);
    }

}
