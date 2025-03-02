package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.EatingListener;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class IsEating extends CHTSLImpl {

    public IsEating() {
        super("Is Eating");
    }

    @Override
    public String toString() {
        return "IsEatingCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.MELON);
        builder.name("&eIs Eating");
        builder.description("Check if the player is Eating. Hot Tip: This will also return true if the player is using a blockable item.");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&eIs Eating");
        builder.description("Check if the player is Eating. Hot Tip: This will also return true if the player is using a blockable item.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return EatingListener.isPlayerEating(player);
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
        return "isEating";
    }
}