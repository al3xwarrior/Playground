package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Actions.ChatAction;
import com.al3x.housing2.Actions.SendTitleAction;
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

public class TitleActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private SendTitleAction action;
    private EventType event;

    public TitleActionMenu(Main main, Player player, HousingWorld house, SendTitleAction action, EventType event) {
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
                if (e.getPlayer().equals(player)) {
                    // Set the new message in the ChatAction
                    String newMessage = e.getMessage();
                    if (type.equals("TITLE")) {
                        action.setTitle(newMessage);
                        player.sendMessage(colorize("&aTitle set to: " + newMessage));
                    } else if (type.equals("SUBTITLE")) {
                        action.setSubtitle(newMessage);
                        player.sendMessage(colorize("&aSubtitle set to: " + newMessage));
                    } else if (type.equals("FADEIN")) {
                        action.setFadeIn(Integer.parseInt(newMessage));
                        player.sendMessage(colorize("&aFade In time set to: " + newMessage));
                    } else if (type.equals("STAY")) {
                        action.setStay(Integer.parseInt(newMessage));
                        player.sendMessage(colorize("&aStay Time set to: " + newMessage));
                    } else if (type.equals("FADEOUT")) {
                        action.setFadeOut(Integer.parseInt(newMessage));
                        player.sendMessage(colorize("&aFade Out time set to: " + newMessage));
                    }

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ChatActionMenu
                    Bukkit.getScheduler().runTaskLater(main, () -> new TitleActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }

    @Override
    public void setupItems() {
        ItemStack titleItem = new ItemStack(Material.BOOK);
        ItemMeta titleItemMeta = titleItem.getItemMeta();
        titleItemMeta.setDisplayName(colorize("&aTitle"));
        titleItem.setItemMeta(titleItemMeta);

        addItem(10, titleItem, () -> {
            setSetting("TITLE");
        });

        ItemStack subtitleItem = new ItemStack(Material.BOOK);
        ItemMeta subtitleItemMeta = subtitleItem.getItemMeta();
        subtitleItemMeta.setDisplayName(colorize("&aSubtitle"));
        subtitleItem.setItemMeta(subtitleItemMeta);

        addItem(11, titleItem, () -> {
            setSetting("SUBTITLE");
        });

        ItemStack fadeInItem = new ItemStack(Material.BOOK);
        ItemMeta fadeInItemMeta = fadeInItem.getItemMeta();
        fadeInItemMeta.setDisplayName(colorize("&aFade In Time"));
        fadeInItem.setItemMeta(fadeInItemMeta);

        addItem(12, titleItem, () -> {
            setSetting("FADEIN");
        });

        ItemStack stayItem = new ItemStack(Material.BOOK);
        ItemMeta stayItemMeta = stayItem.getItemMeta();
        stayItemMeta.setDisplayName(colorize("&aStay Time"));
        stayItem.setItemMeta(stayItemMeta);

        addItem(13, titleItem, () -> {
            setSetting("STAY");
        });

        ItemStack fadeOutItem = new ItemStack(Material.BOOK);
        ItemMeta fadeOutItemMeta = fadeOutItem.getItemMeta();
        fadeOutItemMeta.setDisplayName(colorize("&aFade Out Time"));
        fadeOutItem.setItemMeta(fadeOutItemMeta);

        addItem(14, titleItem, () -> {
            setSetting("FADEOUT");
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(31, backArrow, () -> {
            if (event != null) {
                new ActionsMenu(main, player, house, event.toString(), event).open();
                return;
            }
        });
    }
}
