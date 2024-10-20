package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ClearEnderChestAction extends Action {

    public ClearEnderChestAction() {
        super("Clear Enderchest Action");
    }

    @Override
    public String toString() {
        return "ClearEnderChestAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_CHEST);
        builder.name("&eClear Enderchest Action");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_CHEST);
        builder.name("&aClear Enderchest");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.getEnderChest().clear();
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
