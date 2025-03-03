package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Utils.AbstractCancellable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class ChangeHeldItem implements Listener {
    public static HashMap<UUID, PlayerItemHeldEvent> playerItemHeldEvent = new HashMap<>();
    private HousesManager housesManager;

    public ChangeHeldItem(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onSwapToOffHand(PlayerSwapHandItemsEvent e) {
        sendEventExecution(housesManager, EventType.PLAYER_CHANGE_HELD_ITEM, e.getPlayer(), e);
    }

    @EventHandler
    public void onChangeHeldItem(PlayerItemHeldEvent e) {
        //I think you just used the wrong event?
        playerItemHeldEvent.put(e.getPlayer().getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_CHANGE_HELD_ITEM, e.getPlayer(), e);
    }
}
