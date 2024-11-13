package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GlobalStatRequirementCondition extends Condition {
    private String stat;
    private StatComparator comparator;
    private String compareValue;
    private boolean ignoreCase;

    public GlobalStatRequirementCondition() {
        super("Global Stat Requirement");
        this.stat = "Kills";
        this.comparator = StatComparator.EQUALS;
        this.compareValue = "1.0";
        this.ignoreCase = false;
    }

    @Override
    public String toString() {
        return "GlobalStatRequirementCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
        builder.name("&eGlobal Stat Requirement");
        builder.description("Requires a global stat to match the provided condition.");
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
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
        builder.name("&eGlobal Stat Requirement");
        builder.description("Requires a global stat to match the provided condition.");
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
        return new ActionEditor(4, "Global Stat Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Stat stat = house.getStatManager().getGlobalStatByName(this.stat);
        String compareValue = HandlePlaceholders.parsePlaceholders(player, house, this.compareValue);
        String statValue = stat.getValue();

        if (ignoreCase) {
            statValue = statValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        return Comparator.compare(comparator, statValue, compareValue);
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("stat", stat);
        data.put("comparator", comparator.name());
        data.put("compareValue", compareValue);
        data.put("ignoreCase", ignoreCase);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
