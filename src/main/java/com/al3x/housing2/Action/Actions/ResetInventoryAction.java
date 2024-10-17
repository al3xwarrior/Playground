package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class ResetInventoryAction extends Action {

    public ResetInventoryAction() {
        super("Reset Inventory Action");
    }

    @Override
    public String toString() {
        return "ResetInventoryAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.STONE);
        builder.name("&eReset Inventory");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.changeOrderLore(true);
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.STONE);
        builder.name("&aReset Inventory");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.getInventory().clear();
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }
}
