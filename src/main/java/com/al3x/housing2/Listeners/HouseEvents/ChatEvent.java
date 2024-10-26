package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class ChatEvent implements Listener {
    public static HashMap<UUID, AsyncPlayerChatEvent> lastChatEvent = new HashMap<>();
    private HousesManager housesManager;

    public ChatEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        lastChatEvent.put(e.getPlayer().getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_CHAT, e.getPlayer(), e);
    }
}
