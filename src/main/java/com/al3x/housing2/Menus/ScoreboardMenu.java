package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.ActionbarActionMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ScoreboardMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public ScoreboardMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Scoreboard"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        int[] avaliableSlots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28};

        List<String> scoreboard = house.getScoreboard();
        for (int i = 0; i < scoreboard.size(); i++) {
            final String[] line = {scoreboard.get(i)};
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(line[0]);
            itemMeta.setLore(Arrays.asList(
                    colorize("&eLeft Click to edit!"),
                    colorize("&eRight Click to remove!"),
                    colorize("&7Use shift and left/right click to change order.")
            ));
            item.setItemMeta(itemMeta);

            int finalI = i;
            addItem(avaliableSlots[i], item, () -> {
                // Close the menu and prompt the player to enter a new message
                player.closeInventory();
                player.sendMessage(colorize("&ePlease enter the new string for this scoreboard line:"));

                // Register a listener to capture the next message the player types
                Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onPlayerChat(AsyncPlayerChatEvent e) {
                        e.setCancelled(true);
                        if (e.getPlayer().equals(player)) {
                            String newMessage = e.getMessage();
                            line[0] = newMessage;
                            player.sendMessage(colorize("&aLine set to: " + newMessage));
                            house.setScoreboard(List.of(line));

                            // Unregister this listener after capturing the message
                            AsyncPlayerChatEvent.getHandlerList().unregister(this);

                            // Reopen the ChatActionMenu
                            Bukkit.getScheduler().runTaskLater(main, () -> new ScoreboardMenu(main, player, house).open(), 1L); // Delay slightly to allow chat event to complete
                        }
                    }
                }, main);
            }, () -> {
                scoreboard.remove(finalI);
                new ScoreboardMenu(main, player, house).open();
            });
        }

        ItemStack addLine = new ItemStack(Material.PAPER);
        ItemMeta addLineMeta = addLine.getItemMeta();
        addLineMeta.setDisplayName(colorize("&aAdd Line"));
        addLine.setItemMeta(addLineMeta);
        addItem(41, addLine, () -> {
            if (house.getScoreboard().size() >= 15) {
                player.sendMessage(colorize("&cYou are at the max lines!"));
                return;
            }

            List<String> newScoreboard = house.getScoreboard();
            newScoreboard.add("Hello World!");
            house.setScoreboard(newScoreboard);
            new ScoreboardMenu(main, player, house).open();
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new OwnerHousingMenu(main, player, house).open();
        });
    }
}
