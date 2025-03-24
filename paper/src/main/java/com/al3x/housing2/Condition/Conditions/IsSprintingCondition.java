package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class IsSprintingCondition extends CHTSLImpl {

    public IsSprintingCondition() {
        super("Is Sprinting");
    }

    @Override
    public String toString() {
        return "IsSprintingCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.RABBIT_FOOT);
        builder.name("&eIs Sprinting");
        builder.description("Check if the player is sprinting.");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.RABBIT_FOOT);
        builder.name("&eIs Sprinting");
        builder.description("Check if the player is sprinting.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return player.isSprinting();
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
        return "isSprinting";
    }
}