package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class ClearPlayerStatsAction extends HTSLImpl {

    public ClearPlayerStatsAction() {
        super("Clear Player Stats Action");
    }

    @Override
    public String toString() {
        return "ClearPlayerStatsAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.BARRIER);
        builder.name("&eClear Player Stats");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.BARRIER);
        builder.name("&aClear Player Stats");
        builder.description("Clears all of the player's stats.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.getStatManager().getPlayerStats(player).clear();
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "clearPlayerStats";
    }
}
