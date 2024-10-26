package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.Plugin;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerEnterPortal implements Listener {
    private HousesManager housesManager;

    public PlayerEnterPortal(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onEnterPortal(EntityPortalEnterEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        sendEventExecution(housesManager, EventType.PLAYER_ENTER_PORTAL, (Player) e.getEntity(), null);
    }
}
