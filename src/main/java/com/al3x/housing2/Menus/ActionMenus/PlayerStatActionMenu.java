package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Action.Actions.PlayerStatAction;
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

public class PlayerStatActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PlayerStatAction action;
    private EventType event;

    public PlayerStatActionMenu(Main main, Player player, HousingWorld house, PlayerStatAction action, EventType event) {
        super(player, colorize("&eTitle Action Settings"), 36);
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
                e.setCancelled(true);
                if (e.getPlayer().equals(player)) {
                    String newMessage = e.getMessage();
                    if (type.equals("STATNAME")) {
                        action.setStatName(newMessage);
                        player.sendMessage(colorize("&aStat Name set to: " + newMessage));
                    } else if (type.equals("VALUE")) {
                        try {
//                            action.setValue(newMessage); deprecated so who cares
                        } catch (NumberFormatException ex) {
                            player.sendMessage(colorize("&cInvalid number format!"));
                            return;
                        }
                        player.sendMessage(colorize("&aValue set to: " + newMessage));
                    }

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ChatActionMenu
                    Bukkit.getScheduler().runTaskLater(main, () -> new PlayerStatActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }

    @Override
    public void setupItems() {
        ItemStack statNameItem = new ItemStack(Material.BOOK);
        ItemMeta statNameItemMeta = statNameItem.getItemMeta();
        statNameItemMeta.setDisplayName(colorize("&aStat Name"));
        statNameItemMeta.setLore(Arrays.asList(
                colorize("&fName: " + action.getStatName())
        ));
        statNameItem.setItemMeta(statNameItemMeta);

        addItem(10, statNameItem, () -> {
            setSetting("STATNAME");
        });

        ItemStack modeItem = new ItemStack(Material.COMPASS);
        ItemMeta modeItemMeta = modeItem.getItemMeta();
        modeItemMeta.setDisplayName(colorize("&aMode"));
        modeItemMeta.setLore(Arrays.asList(
                colorize("&fMode: " + action.getMode())
        ));
        modeItem.setItemMeta(modeItemMeta);

        addItem(11, modeItem, () -> {
            new OperationMenu(main, player, house, action, event).open();
        });

        ItemStack valueItem = new ItemStack(Material.BOOK);
        ItemMeta valueItemMeta = valueItem.getItemMeta();
        valueItemMeta.setDisplayName(colorize("&aValue"));
        valueItemMeta.setLore(Arrays.asList(
//                colorize("&fValue: " + action.getValue()) deprecated so who cares
        ));
        valueItem.setItemMeta(valueItemMeta);

        addItem(12, valueItem, () -> {
            setSetting("VALUE");
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
