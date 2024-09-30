package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Actions.ActionbarAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionbarActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private ActionbarAction action;
    private EventType event;

    public ActionbarActionMenu(Main main, Player player, HousingWorld house, ActionbarAction action, EventType event) {
        super(player, colorize("&eChat Action Settings"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack messageItem = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta messageItemMeta = messageItem.getItemMeta();
        messageItemMeta.setDisplayName(colorize("&aMessage"));
        messageItemMeta.setLore(Arrays.asList(
                colorize("&fMessage: "/* + action.getMessage()*/)
        ));
        messageItem.setItemMeta(messageItemMeta);

        addItem(10, messageItem, () -> {
            // Close the menu and prompt the player to enter a new message
            player.closeInventory();
            player.sendMessage(colorize("&ePlease enter the new message for this action:"));

            // Register a listener to capture the next message the player types
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerChat(AsyncPlayerChatEvent e) {
                    e.setCancelled(true);
                    if (e.getPlayer().equals(player)) {
                        String newMessage = e.getMessage();
                        action.setMessage(newMessage);
                        player.sendMessage(colorize("&aMessage set to: " + newMessage));

                        // Unregister this listener after capturing the message
                        AsyncPlayerChatEvent.getHandlerList().unregister(this);

                        // Reopen the ChatActionMenu
                        Bukkit.getScheduler().runTaskLater(main, () -> new ActionbarActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                    }
                }
            }, main);
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(31, backArrow, () -> {
            if (event != null) {
                new ActionsMenu(main, player, house, event).open();
                return;
            }
        });
    }
}
