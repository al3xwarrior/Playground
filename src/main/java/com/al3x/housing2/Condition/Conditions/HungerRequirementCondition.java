package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HungerRequirementCondition extends Condition {
    private StatComparator comparator;
    private String compareValue;

    public HungerRequirementCondition() {
        super("Hunger Requirement");
        this.comparator = StatComparator.EQUALS;
        this.compareValue = "20";
    }

    @Override
    public String toString() {
        return "HungerRequirementCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.COOKED_BEEF);
        builder.name("&eHunger Requirement");
        builder.description("Requires the users current hunger level to match the provided condition.");
        builder.info("Comparator", comparator.name());
        builder.info("Value", compareValue);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.COOKED_BEEF);
        builder.name("&eHunger Requirement");
        builder.description("Requires the users current hunger level to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("comparator",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eMode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + comparator)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, StatComparator.values(), null
                ),
                new ActionEditor.ActionItem("compareValue",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eAmount")
                                .info("&7Current Value", "")
                                .info(null, "&a" + compareValue)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                )
        );
        return new ActionEditor(4, "Hunger Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return Comparator.compare(comparator, String.valueOf(player.getFoodLevel()), compareValue);
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("comparator", comparator.name());
        data.put("compareValue", compareValue);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
