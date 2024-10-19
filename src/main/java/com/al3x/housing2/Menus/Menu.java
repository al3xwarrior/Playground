package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Menu implements Listener {
    private Inventory inventory;
    private String title;
    private Map<Integer, Consumer<InventoryClickEvent>> leftClickActions = new HashMap<>();
    private Map<Integer, Consumer<InventoryClickEvent>> rightClickActions = new HashMap<>();
    private Player player;

    public Menu(Player player, String title, int size) {
        this.player = player;
        this.title = title;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public abstract void setupItems();

    public void clearItems() {
        inventory.clear();
    }

    // Opens the menu for the player
    public void open() {
        setupItems();
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    // Handles the click event by running the corresponding action
    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            event.setCancelled(true);

            int slot = event.getSlot();
            Consumer<InventoryClickEvent> leftAction = leftClickActions.get(slot);
            Consumer<InventoryClickEvent> rightAction = rightClickActions.get(slot);

            if (leftAction != null && rightAction == null) {
                leftAction.accept(event); // Run the left-click action

                // Will remove if it becomes annoying
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);

                return;
            }

            if (event.getClick() == org.bukkit.event.inventory.ClickType.LEFT && leftAction != null) {
                // Will remove if it becomes annoying
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                leftAction.accept(event); // Run the left-click action
            } else if (event.getClick() == org.bukkit.event.inventory.ClickType.RIGHT && rightAction != null) {
                // Will remove if it becomes annoying
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                rightAction.accept(event); // Run the right-click action
            }
        }
    }

    public String getTitle() {
        return title;
    }

    // Helper method to add an item and bind actions to its slot
    public void addItem(int slot, ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, clickAction);
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
        rightClickActions.put(slot, (e) -> rightClickAction.run());
    }

    protected void openChat(Main main, Consumer<String> consumer) {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {
                e.setCancelled(true);
                if (e.getPlayer().equals(player)) {
                    String input = e.getMessage();
                    consumer.accept(input);

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ActionEditMenu
                    Bukkit.getScheduler().runTaskLater(main, Menu.this::open, 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }
}
