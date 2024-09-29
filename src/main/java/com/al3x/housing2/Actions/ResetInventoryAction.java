package com.al3x.housing2.Actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class ResetInventoryAction implements Action{

    public ResetInventoryAction() {}

    @Override
    public String toString() {
        return "ResetInventoryAction";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eReset Inventory Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Clears the players inventory."),
                "",
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void execute(Player player) {
        player.getInventory().clear();
    }
}
