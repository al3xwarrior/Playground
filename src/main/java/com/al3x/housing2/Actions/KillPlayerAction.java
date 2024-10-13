package com.al3x.housing2.Actions;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class KillPlayerAction extends Action {

    public KillPlayerAction() {
        super("Kill Player Action");
    }

    @Override
    public String toString() {
        return "KillPlayerAction";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.IRON_BARS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eKill Player Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Kills the player"),
                "",
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.setHealth(0.0);
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }
}
