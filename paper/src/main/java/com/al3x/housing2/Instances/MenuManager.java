package com.al3x.housing2.Instances;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MenuManager {

    private static Map<Player, Menu> playerMenus = new HashMap<>();
    private static Map<Player, UUID> playerHouseID = new HashMap<>();
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

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        if (house != null) {
            playerHouseID.put(player, house.getHouseUUID());
        }
    }

    public static Menu getPlayerMenu(Player player) {
        if (player.getWorld().getName().equals("world")) {
            return playerMenus.get(player);
        }
        if (!playerHouseID.containsKey(player)) {
            return null;
        }
        if (playerHouseID.get(player) != Main.getInstance().getHousesManager().getHouse(player.getWorld()).getHouseUUID()) {
            return null;
        }
        return playerMenus.get(player);
    }

    public static void removeMenu(Player player) {
        playerMenus.remove(player);
    }
}
