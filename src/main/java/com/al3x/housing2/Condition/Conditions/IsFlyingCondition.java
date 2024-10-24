package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class IsFlyingCondition extends Condition {

        public IsFlyingCondition() {
            super("Is Flying");
        }

        @Override
        public String toString() {
            return "IsFlyingCondition";
        }

        @Override
        public void createDisplayItem(ItemBuilder builder) {
            builder.material(Material.FEATHER);
            builder.name("&eIs Flying");
            builder.description("Check if the player is flying.");
            builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            builder.shiftClick();
        }

        @Override
        public void createAddDisplayItem(ItemBuilder builder) {
            builder.material(Material.FEATHER);
            builder.name("&aIs Flying");
            builder.description("Check if the player is Flying.");
            builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
        }

        @Override
        public boolean execute(Player player, HousingWorld house) {
            return player.isFlying();
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
