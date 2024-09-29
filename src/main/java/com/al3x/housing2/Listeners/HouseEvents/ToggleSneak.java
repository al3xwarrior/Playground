package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class ToggleSneak implements Listener {
    private HousesManager housesManager;

    public ToggleSneak(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_TOGGLE_SNEAK, e.getPlayer());
    }

}
