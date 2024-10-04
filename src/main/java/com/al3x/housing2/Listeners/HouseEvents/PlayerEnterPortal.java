package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerEnterPortal implements Listener {
    private HousesManager housesManager;

    public PlayerEnterPortal(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onEnterPortal(PlayerPortalEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_ENTER_PORTAL, e.getPlayer(), e);
    }
}
