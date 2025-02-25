package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class StatRequirementCondition extends CHTSLImpl {
    private String stat;
    private StatComparator comparator;
    private String compareValue;
    private boolean ignoreCase;

    public StatRequirementCondition() {
        super("Stat Requirement");
        this.stat = "Kills";
        this.comparator = StatComparator.EQUALS;
        this.compareValue = "1.0";
        this.ignoreCase = false;
    }

    @Override
    public String toString() {
        return "StatRequirementCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&eStat Requirement");
        builder.description("Requires a stat to match the provided condition.");
        builder.info("Stat", stat);
        builder.info("Comparator", comparator.name());
        builder.info("Value", compareValue);
        builder.info("Ignores Case", ignoreCase ? "Yes" : "No");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&eStat Requirement");
        builder.description("Requires a stat to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("stat",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eStat")
                                .info("&7Current Value", "")
                                .info(null, "&a" + stat)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.STRING
                ),
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
                        ActionEditor.ActionItem.ActionType.STRING
                ),
                new ActionEditor.ActionItem("ignoreCase",
                        ItemBuilder.create((ignoreCase ? Material.LIME_DYE : Material.RED_DYE))
                                .name((ignoreCase ? "&aIgnore Case" : "&cIgnore Case"))
                                .info("&7Current Value", "")
                                .info(null, "&a" + ignoreCase)
                                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "Stat Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        String statString = HandlePlaceholders.parsePlaceholders(player, house, this.stat);
        Stat stat = house.getStatManager().getPlayerStatByName(player, statString);
        String compareValue = HandlePlaceholders.parsePlaceholders(player, house, this.compareValue);
        String statValue = stat.getValue();
        if (ignoreCase) {
            statValue = statValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        return Comparator.compare(comparator, statValue, compareValue);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("stat", stat);
        data.put("comparator", comparator.name());
        data.put("compareValue", compareValue);
        data.put("ignoreCase", ignoreCase);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "stat";
    }

    @Override
    public String export(int indent) {
        String compareValue = this.compareValue;
        if (compareValue.contains(" ")) {
            compareValue = "\"" + compareValue + "\"";
        }
        return "stat " + stat + " " + comparator.name() + " " + compareValue + " " + ignoreCase;
    }

    @Override
    public void importCondition(String action, List<String> nextLines) {
        String[] parts = action.split(" ");
        Duple<String[], String> statArg = handleArg(parts, 0);
        this.stat = statArg.getSecond();
        parts = statArg.getFirst();
        if (parts.length == 0) {
            return;
        }
        this.comparator = StatComparator.getComparator(parts[0]);
        Duple<String[], String> compareValueArg = handleArg(parts, 1);
        compareValue = compareValueArg.getSecond();
        parts = compareValueArg.getFirst();
        if (parts.length > 0) {
            ignoreCase = Boolean.parseBoolean(parts[0]);
        }
    }
}
