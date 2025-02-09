package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class BreakAction extends HTSLImpl {

    public BreakAction() {
        super("Break Action");
    }

    @Override
    public String toString() {
        return "BreakAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.STONE_PICKAXE);
        builder.name("&eBreak");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.STONE_PICKAXE);
        builder.name("&aBreak");
        builder.description("Stops executing any remaining actions inside of a loop.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public int limit() {
        return 1;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "break";
    }
}
