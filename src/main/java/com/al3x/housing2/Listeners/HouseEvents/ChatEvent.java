package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;
import static com.al3x.housing2.Utils.Color.colorize;

public class ChatEvent implements Listener {
    public static HashMap<UUID, AsyncPlayerChatEvent> lastChatEvent = new HashMap<>();
    private HousesManager housesManager;

    public ChatEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        e.setCancelled(true);
        // Lobby Chat
        if (world.getName().equals("world")) {
            String message = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%" + player.getName() + "&7: &f");
            for (Player p : world.getPlayers()) {
                p.sendMessage(colorize(message) + e.getMessage());
            }
            return;
        }

        lastChatEvent.put(player.getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_CHAT, player, e);

        if (e.isCancelled()) {
            return;
        }

        String message = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%" + player.getName() + "&7: &f");
        for (Player p : world.getPlayers()) {
            p.sendMessage(colorize(message) + e.getMessage());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        lastChatEvent.remove(e.getPlayer().getUniqueId());
    }
}
