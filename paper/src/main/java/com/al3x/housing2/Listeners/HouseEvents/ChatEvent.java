package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Commands.GlobalChat;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;
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
        // Lobby Chat

        if (GlobalChat.globalChat.getOrDefault(player.getUniqueId(), false)) {
            e.setCancelled(true);
            String message = PlaceholderAPI.setPlaceholders(player, "&6[Global] %luckperms_prefix%" + player.getName() + "&7: &f");
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (GlobalChat.isToggled.getOrDefault(p.getUniqueId(), false)) continue;
                p.sendMessage(colorize(message) + e.getMessage());
            }
            return;
        }

        if (world.getName().equals("world")) {
            e.setCancelled(true);
            String message = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%" + player.getName() + "&7: &f");
            for (Player p : world.getPlayers()) {
                p.sendMessage(colorize(message) + e.getMessage());
            }
            return;
        }

        PlayerData playerData = housesManager.getHouse(world).getPlayersData().get(player.getUniqueId().toString());
        if (playerData.getMuted() && playerData.getMuteExpiration().isAfter(Instant.now())) {
            e.setCancelled(true);
            player.sendMessage(colorize("&cYou are muted in this house!"));
            return;
        } else if (playerData.getMuted()) {
            playerData.setMuted(false);
        }

        lastChatEvent.put(player.getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_CHAT, player, e);

        if (e.isCancelled()) {
            return;
        }

        e.setCancelled(true);

        if (housesManager.getHouse(world) == null) {
            return;
        }
        HousingWorld house = housesManager.getHouse(world);

        Component message = StringUtilsKt.housingStringFormatter("%group.prefix%" + player.getName() + "&7: &f" + e.getMessage(), house, player);
        for (Player p : world.getPlayers()) {
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        lastChatEvent.remove(e.getPlayer().getUniqueId());
    }
}
