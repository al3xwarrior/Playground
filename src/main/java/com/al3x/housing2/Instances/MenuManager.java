package com.al3x.housing2.Instances;

import java.util.HashMap;
import java.util.Map;

import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;

public class MenuManager {

    private static Map<Player, Menu> playerMenus = new HashMap<>();

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
