package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements Listener {
    private Inventory inventory;
    private Map<Integer, Runnable> leftClickActions = new HashMap<>();
    private Map<Integer, Runnable> rightClickActions = new HashMap<>();
    private Player player;
    private int size;

    public Menu(Player player, String title, int size) {
        this.player = player;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, title);
        setupItems();
    }

    public abstract void setupItems();

    public void clearItems() {
        inventory.clear();
    }

    // Opens the menu for the player
    public void open() {
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    // Handles the click event by running the corresponding action
    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            event.setCancelled(true);
            int slot = event.getSlot();
            Runnable leftAction = leftClickActions.get(slot);
            Runnable rightAction = rightClickActions.get(slot);

            if (event.getClick() == org.bukkit.event.inventory.ClickType.LEFT && leftAction != null) {
                leftAction.run(); // Run the left-click action
            } else if (event.getClick() == org.bukkit.event.inventory.ClickType.RIGHT && rightAction != null) {
                rightAction.run(); // Run the right-click action
            }
        }
    }

    // Helper method to add an item and bind actions to its slot
    public void addItem(int slot, ItemStack item, Runnable leftClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, leftClickAction);
    }
    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, leftClickAction);
        rightClickActions.put(slot, rightClickAction);
    }
}
