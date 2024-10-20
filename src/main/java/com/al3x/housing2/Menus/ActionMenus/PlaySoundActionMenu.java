package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Action.Actions.PlaySoundAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
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

public class PlaySoundActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PlaySoundAction action;
    private EventType event;

    public PlaySoundActionMenu(Main main, Player player, HousingWorld house, PlaySoundAction action, EventType event) {
        super(player, colorize("&ePlay Sound Settings"), 36);
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
        player.sendMessage(colorize("&ePlease enter the new setting for this action:"));

        // Register a listener to capture the next message the player types
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {
                e.setCancelled(true);
                if (e.getPlayer().equals(player)) {
                    String newMessage = e.getMessage();
                    if (type.equals("VOLUME")) {
                        action.setVolume(Float.valueOf(newMessage));
                        player.sendMessage(colorize("&aVolume set to: " + newMessage));
                    } else if (type.equals("PITCH")) {
                        action.setPitch(Float.valueOf(newMessage));
                        player.sendMessage(colorize("&aPitch set to: " + newMessage));
                    }

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ChatActionMenu
                    Bukkit.getScheduler().runTaskLater(main, () -> new PlaySoundActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }

    @Override
    public void setupItems() {
        ItemStack soundItem = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta soundItemMeta = soundItem.getItemMeta();
        soundItemMeta.setDisplayName(colorize("&aSound"));
        soundItemMeta.setLore(Arrays.asList(
                colorize("&fSound: " + action.getSound())
        ));
        soundItem.setItemMeta(soundItemMeta);
        addItem(10, soundItem, () -> {
            new SoundMenu(main, player, house, action, event).open();
        });

        ItemStack volumeItem = new ItemStack(Material.BOOK);
        ItemMeta volumeItemMeta = volumeItem.getItemMeta();
        volumeItemMeta.setDisplayName(colorize("&aVolume"));
        volumeItemMeta.setLore(Arrays.asList(
                colorize("&fVolume: " + action.getVolume())
        ));
        volumeItem.setItemMeta(volumeItemMeta);
        addItem(11, volumeItem, () -> {
            setSetting("VOLUME");
        });

        ItemStack pitchItem = new ItemStack(Material.BOOK);
        ItemMeta pitchItemMeta = pitchItem.getItemMeta();
        pitchItemMeta.setDisplayName(colorize("&aPitch"));
        pitchItemMeta.setLore(Arrays.asList(
                colorize("&fPitch: " + action.getPitch())
        ));
        pitchItem.setItemMeta(pitchItemMeta);
        addItem(12, pitchItem, () -> {
            setSetting("PITCH");
        });

        ItemStack locationItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta locationItemmMeta = locationItem.getItemMeta();
        locationItemmMeta.setDisplayName(colorize("&aLocation"));
        locationItemmMeta.setLore(Arrays.asList(
                colorize("&fLocation: " + action.getLocation())
        ));
        locationItem.setItemMeta(locationItemmMeta);
        addItem(13, locationItem, () -> {
            new LocationMenu(main, player, house, action, event).open();
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
