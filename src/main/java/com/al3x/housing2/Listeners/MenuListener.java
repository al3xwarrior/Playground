package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onMenuClick(InventoryClickEvent event) {
        // Check if the inventory clicked belongs to a menu (you need to store menus somewhere)
        if (event.getWhoClicked() instanceof Player player) {
            // Here you would need a way to retrieve the player's current menu
            Menu menu = getMenuForPlayer(player);
            if (menu != null) {
                menu.handleClick(event);
            }
        }
    }

    private Menu getMenuForPlayer(Player player) {
        return MenuManager.getPlayerMenu(player);
    }
}
