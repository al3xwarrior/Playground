package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Actions.PlayerStatAction;
import com.al3x.housing2.Actions.PushPlayerAction;
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

public class PushPlayerActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PushPlayerAction action;
    private EventType event;

    public PushPlayerActionMenu(Main main, Player player, HousingWorld house, PushPlayerAction action, EventType event) {
        super(player, colorize("&ePush Player Action Settings"), 36);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
        setupItems();
    }

    private void setSetting(String type) {
        // Close the menu and prompt the player to enter a new message
        player.closeInventory();
        player.sendMessage(colorize("&ePlease enter the new message for this action:"));

        // Register a listener to capture the next message the player types
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {
                if (e.getPlayer().equals(player)) {
                    e.setCancelled(true);
                    String newMessage = e.getMessage();
                    if (type.equals("AMOUNT")) {
                        action.setAmount(Double.parseDouble(newMessage));
                        player.sendMessage(colorize("&aVelocity set to: " + newMessage));
                    }

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ChatActionMenu
                    Bukkit.getScheduler().runTaskLater(main, () -> new PushPlayerActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }

    @Override
    public void setupItems() {
        ItemStack directionItem = new ItemStack(Material.COMPASS);
        ItemMeta subtitleItemMeta = directionItem.getItemMeta();
        subtitleItemMeta.setDisplayName(colorize("&aDirection"));
        subtitleItemMeta.setLore(Arrays.asList(
                colorize("&fDirection: " + action.getDirection())
        ));
        directionItem.setItemMeta(subtitleItemMeta);

        addItem(10, directionItem, () -> {
            new DirectionMenu(main, player, house, action, event).open();
        });

        ItemStack velocityItem = new ItemStack(Material.SLIME_BALL);
        ItemMeta velocityItemMeta = velocityItem.getItemMeta();
        velocityItemMeta.setDisplayName(colorize("&aVelocity"));
        velocityItemMeta.setLore(Arrays.asList(
                colorize("&fVelocity: " + action.getAmount())
        ));
        velocityItem.setItemMeta(velocityItemMeta);

        addItem(11, velocityItem, () -> {
            setSetting("AMOUNT");
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
