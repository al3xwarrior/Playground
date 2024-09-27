package com.al3x.housing2.Actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class KillPlayerAction implements Action{

    public ActionbarAction() {}

    @Override
    public String toString() {
        return "KillPlayerAction";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.IRON_BAR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eKill Player Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Kills the player"),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void execute(Player player) {
        player.setHealth(0.0);
    }
}
