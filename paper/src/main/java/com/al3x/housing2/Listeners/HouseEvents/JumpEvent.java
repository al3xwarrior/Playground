package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class JumpEvent implements Listener {

    private HousesManager housesManager;

    public JumpEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerJumpEvent event) {
        sendEventExecution(housesManager, EventType.PLAYER_JUMP, event.getPlayer(), event);
    }

}