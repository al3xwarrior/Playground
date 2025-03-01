package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import io.papermc.paper.event.entity.EntityPortalReadyEvent;
import org.bukkit.Bukkit;
import org.bukkit.PortalType;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.Plugin;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;
import static org.bukkit.Material.END_PORTAL_FRAME;

public class PlayerEnterPortal implements Listener {
    private HousesManager housesManager;

    public PlayerEnterPortal(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onEnterPortal(EntityPortalEnterEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        sendEventExecution(housesManager, EventType.PLAYER_ENTER_PORTAL, (Player) e.getEntity(), (Cancellable) null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPortal(EntityPortalReadyEvent e) {
        if (e.getPortalType() == PortalType.ENDER) {
            e.setCancelled(true);
        }
    }
}
