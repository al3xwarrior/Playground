package com.al3x.housing2.Actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class FullHealAction implements Action{

    public FullHealAction() {}

    @Override
    public String toString() {
        return "FullHealAction";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eFull Heal Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Fully heals the player."),
                "",
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void execute(Player player) {
        player.setHealth(player.getMaxHealth());
    }
}
