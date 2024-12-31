package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlaceBlock implements Listener {

    private HousesManager housesManager;

    public PlaceBlock(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        HousingWorld house = housesManager.getHouse(e.getPlayer().getWorld());
        if (house == null) return;
        if (!house.hasPermission(e.getPlayer(), Permissions.BUILD)) {
            if (!house.getOwner().isOnline() && !house.hasPermission(e.getPlayer(), Permissions.OFFLINE_BUILD)) {
                e.setCancelled(true);
            }
        }

        sendEventExecution(housesManager, EventType.PLAYER_BLOCK_PLACE, e.getPlayer(), e);
    }

}
