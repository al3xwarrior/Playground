package com.al3x.housing2.Menus;

import com.al3x.housing2.Events.MenuSetupItemsEvent;
import com.al3x.housing2.Events.OpenActionMenuEvent;
import com.al3x.housing2.Events.OpenMenuEvent;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.StringUtilsKt;
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
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public abstract class Menu {
    protected Inventory inventory;
    private String title;
    private int size;
    protected Map<Integer, Consumer<InventoryClickEvent>> leftClickActions = new HashMap<>();
    protected Map<Integer, Consumer<InventoryClickEvent>> rightClickActions = new HashMap<>();
    protected Map<Integer, Consumer<InventoryClickEvent>> shiftLeftClickActions = new HashMap<>();
    protected Map<Integer, Consumer<InventoryClickEvent>> anyClickActions = new HashMap<>();
    protected Player player;

    public Menu(Player player, String title, int size) {
        this.inventory = Bukkit.createInventory(null, size, StringUtilsKt.housingStringFormatter(title));
        this.player = player;
        this.title = title;
        this.size = size;
    }

    @Deprecated() // Use setupItems instead
    public abstract void initItems();

    public void setupItems() {
        MenuSetupItemsEvent.Pre event = new MenuSetupItemsEvent.Pre(player, this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        initItems();

        MenuSetupItemsEvent.Post postEvent = new MenuSetupItemsEvent.Post(player, this);
        Bukkit.getPluginManager().callEvent(postEvent);
    }

    public void clearItems() {
        leftClickActions.clear();
        rightClickActions.clear();
        shiftLeftClickActions.clear();
        anyClickActions.clear();
        inventory.clear();
    }

    // Opens the menu for the player
    public void open() {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            OpenMenuEvent event = new OpenMenuEvent(this, player, Main.getInstance());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
                    AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
                }
                if (!event.isShowItems()) MenuManager.setWindowOpen(player, this);
                MenuManager.setMenu(player, this);
                return;
            }

            this.inventory = Bukkit.createInventory(null, size, StringUtilsKt.housingStringFormatter(title));

            setupItems();

            if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
                AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
            }
            MenuManager.setMenu(player, this);
            player.openInventory(inventory);
        });
    }

    protected boolean isCancelled() {
        return true;
    }

    // Handles the click event by running the corresponding action
    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory) {
            handleExternalClick(event);
        }
    }

    public void handleExternalClick(InventoryClickEvent event) {
        event.setCancelled(isCancelled());

        int slot = event.getSlot();
        Consumer<InventoryClickEvent> leftAction = leftClickActions.get(slot);
        Consumer<InventoryClickEvent> rightAction = rightClickActions.get(slot);
        Consumer<InventoryClickEvent> shiftLeftAction = shiftLeftClickActions.get(slot);
        Consumer<InventoryClickEvent> anyAction = anyClickActions.get(slot);

        if (event.getClick() == org.bukkit.event.inventory.ClickType.LEFT && leftAction != null) {
            leftAction.accept(event); // Run the left-click action
        } else if (event.getClick() == org.bukkit.event.inventory.ClickType.RIGHT && rightAction != null) {
            rightAction.accept(event); // Run the right-click action
        } else if (event.getClick() == org.bukkit.event.inventory.ClickType.SHIFT_LEFT && shiftLeftClickActions.get(slot) != null) {
            shiftLeftAction.accept(event); // Run the shift-left-click action
        } else if (anyClickActions.get(slot) != null) {
            anyAction.accept(event);
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

    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    // Helper method to add an item and bind actions to its slot
    public void addItem(int slot, ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        inventory.setItem(slot, item);
        anyClickActions.put(slot, clickAction);
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
    }

    public void addItem(int slot, ItemStack item) {
        addItem(slot, item, () -> {
        });
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
        rightClickActions.put(slot, (e) -> rightClickAction.run());
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction, Runnable shiftLeftClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
        rightClickActions.put(slot, (e) -> rightClickAction.run());
        shiftLeftClickActions.put(slot, (e) -> shiftLeftClickAction.run());
    }

    public void addItem(int slot, ItemStack item, Runnable leftClickAction, Runnable rightClickAction, Consumer<InventoryClickEvent> shiftLeftClickAction) {
        inventory.setItem(slot, item);
        leftClickActions.put(slot, (e) -> leftClickAction.run());
        rightClickActions.put(slot, (e) -> rightClickAction.run());
        shiftLeftClickActions.put(slot, shiftLeftClickAction);
    }

    public void openChat(Main main, Consumer<String> consumer) {
        openChat(main, "", consumer);
    }

    public void openChat(Main main, String previous, Consumer<String> consumer) {
        player.closeInventory();
        TextComponent cancelComp = new TextComponent(" §c[CANCEL]");
        cancelComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cancelinput"));
        cancelComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cClick to cancel")));
        TextComponent previousComp = new TextComponent(" §b[PREVIOUS]");
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

    public void handleDrag(InventoryDragEvent event) {

    }

    public void handleClose(InventoryCloseEvent event) {
    }
}
