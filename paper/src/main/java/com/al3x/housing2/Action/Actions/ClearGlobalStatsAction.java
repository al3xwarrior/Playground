package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class ClearGlobalStatsAction extends HTSLImpl {

    public ClearGlobalStatsAction() {
        super("Clear Global Stats Action");
    }

    @Override
    public String toString() {
        return "ClearGlobalStatsAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.BARRIER);
        builder.name("&eClear Global Stats");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.BARRIER);
        builder.name("&aClear Global Stats");
        builder.description("Resets all global stats.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.getStatManager().getGlobalStats().clear();
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "clearGlobalStats";
    }
}
