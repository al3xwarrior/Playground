package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
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

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Menu menu = getMenuForPlayer((Player) event.getPlayer());
        if (menu != null) {
            menu.handleClose(event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Menu menu = getMenuForPlayer((Player) event.getWhoClicked());
        if (!(menu instanceof ItemSelectMenu itemMenu)) return;

        if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                ItemStack item = event.getCurrentItem();
                itemMenu.consumer.accept(item);
                itemMenu.back.open();
            }
        }
    }

    private Menu getMenuForPlayer(Player player) {
        return MenuManager.getPlayerMenu(player);
    }
}
