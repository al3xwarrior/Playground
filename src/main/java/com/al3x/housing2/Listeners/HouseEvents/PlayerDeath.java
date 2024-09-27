package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerDeath implements Listener {

    private HousesManager housesManager;

    public PlayerDeath(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_DEATH, e.getEntity());
    }
}
