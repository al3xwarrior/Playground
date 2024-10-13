package com.al3x.housing2.Actions;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class FullHealAction extends Action {

    public FullHealAction() {
        super("Full Heal Action");
    }

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
    public boolean execute(Player player, HousingWorld house) {
        player.setHealth(player.getMaxHealth());
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }
}
