package com.al3x.housing2.Instances;

import java.util.HashMap;
import java.util.Map;

import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MenuManager {

    private static Map<Player, Menu> playerMenus = new HashMap<>();
    private static Map<Player, Listener> playerListeners = new HashMap<>();

    public static Listener setListener(Player player, Listener listener) {
        playerListeners.put(player, listener);
        return listener;
    }

    public static Listener getListener(Player player) {
        if (!playerListeners.containsKey(player)) {
            return null;
        }
        return playerListeners.get(player);
    }

    public static void setMenu(Player player, Menu menu) {
        playerMenus.put(player, menu);
    }

    public static Menu getPlayerMenu(Player player) {
        return playerMenus.get(player);
    }

    public static void removeMenu(Player player) {
        playerMenus.remove(player);
    }
}
