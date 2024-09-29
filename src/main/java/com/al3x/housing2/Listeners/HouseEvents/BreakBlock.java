package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class BreakBlock implements Listener {

    private HousesManager housesManager;

    public BreakBlock(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_BLOCK_BREAK, e.getPlayer());
    }

}
