package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class IsShieldingCondition extends CHTSLImpl {

    public IsShieldingCondition() {
        super("Is Shielding");
    }

    @Override
    public String toString() {
        return "IsShieldingCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.SHIELD);
        builder.name("&eIs Shielding");
        builder.description("Check if the player is Shielding.");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.SHIELD);
        builder.name("&eIs Shielding");
        builder.description("Check if the player is Shielding.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return player.isBlocking();
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
        return "isShielding";
    }
}
