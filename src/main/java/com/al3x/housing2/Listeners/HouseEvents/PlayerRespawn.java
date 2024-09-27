package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerRespawn implements Listener {

    private HousesManager housesManager;

    public PlayerRespawn(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_RESPAWN, e.getPlayer());
    }

}
