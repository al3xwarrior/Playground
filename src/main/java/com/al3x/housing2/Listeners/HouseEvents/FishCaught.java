package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class FishCaught implements Listener {
    private HousesManager housesManager;

    public FishCaught(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onCatch(PlayerFishEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_DROP_ITEM, e.getPlayer(), e);
    }
}
