package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class IsSneakingCondition extends CHTSLImpl {

        public IsSneakingCondition() {
            super("Is Sneaking");
        }

        @Override
        public String toString() {
            return "IsSneakingCondition";
        }

        @Override
        public void createDisplayItem(ItemBuilder builder) {
            builder.material(Material.HAY_BLOCK);
            builder.name("&eIs Sneaking");
            builder.description("Check if the player is sneaking.");
            builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            builder.shiftClick();
        }

        @Override
        public void createAddDisplayItem(ItemBuilder builder) {
            builder.material(Material.HAY_BLOCK);
            builder.name("&eIs Sneaking");
            builder.description("Check if the player is sneaking.");
            builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
        }

        @Override
        public boolean execute(Player player, HousingWorld house) {
            return player.isSneaking();
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
        return "isSneaking";
    }
}
