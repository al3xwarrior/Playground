package com.al3x.housing2.Listeners.HouseEvents.Permissions;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class BlockInteractions implements Listener {
    private HousesManager housesManager;

    public BlockInteractions(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void itemFrameChangeEvent(PlayerItemFrameChangeEvent e) {
        HousingWorld house = housesManager.getHouse(e.getPlayer().getWorld());
        if (house == null) return;
        Player player = e.getPlayer();

        if (!((house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) || house.hasPermission(player, Permissions.OFFLINE_BUILD) && house.getOwner().isOnline())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void flowerPotManipulateEvent(PlayerFlowerPotManipulateEvent e) {
        HousingWorld house = housesManager.getHouse(e.getPlayer().getWorld());
        if (house == null) return;
        Player player = e.getPlayer();

        if (!((house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) || house.hasPermission(player, Permissions.OFFLINE_BUILD) && house.getOwner().isOnline())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void hangingBreakByEntityEvent(HangingBreakByEntityEvent e) {
        if (!(e.getRemover() instanceof Player player)) return;
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        if (!((house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) || house.hasPermission(player, Permissions.OFFLINE_BUILD) && house.getOwner().isOnline())) {
            e.setCancelled(true);
        }
    }
}
