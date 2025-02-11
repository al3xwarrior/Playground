package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class ToggleFly implements Listener {
    private HousesManager housesManager;

    public ToggleFly(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_TOGGLE_FLIGHT, e.getPlayer(), e);
    }

}
