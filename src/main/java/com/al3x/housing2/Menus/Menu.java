package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public abstract class Menu implements Listener {
    protected Inventory inventory;
    private String title;
    private int size;
    private Map<Integer, Consumer<InventoryClickEvent>> leftClickActions = new HashMap<>();
    private Map<Integer, Consumer<InventoryClickEvent>> rightClickActions = new HashMap<>();
    private Player player;

    public Menu(Player player, String title, int size) {
        this.inventory = Bukkit.createInventory(null, size, colorize(title));
        this.player = player;
        this.title = title;
        this.size = size;
    }

    public abstract void setupItems();

    public void clearItems() {
        leftClickActions.clear();
        rightClickActions.clear();
        inventory.clear();
    }

    // Opens the menu for the player
    public void open() {
        this.inventory = Bukkit.createInventory(null, size, colorize(getTitle()));
        setupItems();
        if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
                AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
        }
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    protected boolean isCancelled() {
        return true;
    }

    // Handles the click event by running the corresponding action
    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            event.setCancelled(isCancelled());

            int slot = event.getSlot();
            Consumer<InventoryClickEvent> leftAction = leftClickActions.get(slot);
            Consumer<InventoryClickEvent> rightAction = rightClickActions.get(slot);

            if (leftAction != null && rightAction == null) {
                leftAction.accept(event); // Run the left-click action


                return;
            }

            if (event.getClick() == org.bukkit.event.inventory.ClickType.LEFT && leftAction != null) {
                leftAction.accept(event); // Run the left-click action
            } else if (event.getClick() == org.bukkit.event.inventory.ClickType.RIGHT && rightAction != null) {
                rightAction.accept(event); // Run the right-click action
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public Player getOwner() {
        return player;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void addItem(int slot, ItemStack item) {
        addItem(slot, item, (e) -> {});
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
        rightClickActions.put(slot, (e) -> rightClickAction.run());
    }

    public void openChat(Main main, Consumer<String> consumer) {
        openChat(main, "", consumer);
    }

    public void openChat(Main main, String previous, Consumer<String> consumer) {
        player.closeInventory();
        TextComponent cancelComp = new TextComponent(" §c[CANCEL]");
        cancelComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cancelinput"));
        cancelComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cClick to cancel")));
        TextComponent previousComp = new TextComponent(" §b[Previous]");
        previousComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, previous));
        previousComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§bClick to paste previous value")));
        player.spigot().sendMessage(previousComp, cancelComp);

        Bukkit.getPluginManager().registerEvents(MenuManager.setListener(player, new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerChat(AsyncPlayerChatEvent e) {
                if (e.getPlayer().equals(player)) {
                    e.setCancelled(true);
                    String input = e.getMessage();
                    consumer.accept(input);

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ActionEditMenu
                    Bukkit.getScheduler().runTaskLater(main, Menu.this::open, 1L); // Delay slightly to allow chat event to complete
                }
            }
        }), main);
    }
}
