package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class ContinueAction extends HTSLImpl {

    public ContinueAction() {
        super("Continue Action");
    }

    @Override
    public String toString() {
        return "ContinueAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.HAY_BLOCK);
        builder.name("&eContinue");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.HAY_BLOCK);
        builder.name("&aContinue");
        builder.description("Skips the current remaining actions and continues to the next action.");
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
        return 10;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "continue";
    }
}
