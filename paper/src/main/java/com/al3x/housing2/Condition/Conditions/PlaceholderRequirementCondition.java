package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaceholderRequirementCondition extends CHTSLImpl {
    private String placeholder;
    private StatComparator comparator;
    private String compareValue;
    private boolean ignoreCase;
    private boolean ignoreColor;

    public PlaceholderRequirementCondition() {
        super("Placeholder Requirement");
        this.placeholder = "%stat.player/Kills%";
        this.comparator = StatComparator.EQUALS;
        this.compareValue = "1.0";
        this.ignoreCase = false;
        this.ignoreColor = false;
    }

    @Override
    public String toString() {
        return "PlaceholderRequirementCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.OAK_SIGN);
        builder.name("&ePlaceholder Requirement");
        builder.description("Requires a placeholder to match the provided condition.");
        builder.info("Placeholder", placeholder);
        builder.info("Comparator", comparator.name());
        builder.info("Value", compareValue);
        builder.info("Ignores Case", ignoreCase ? "Yes" : "No");
        builder.info("Ignores Color", ignoreColor ? "Yes" : "No");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.OAK_SIGN);
        builder.name("&ePlaceholder Requirement");
        builder.description("Requires a placeholder to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("placeholder",
                        ItemBuilder.create(Material.OAK_SIGN)
                                .name("&ePlaceholder")
                                .info("&7Current Value", "")
                                .info(null, "&a" + placeholder)
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
                ),
                new ActionEditor.ActionItem("ignoreColor",
                        ItemBuilder.create((ignoreColor ? Material.LIME_DYE : Material.RED_DYE))
                                .name((ignoreColor ? "&aIgnore Color" : "&cIgnore Color"))
                                .info("&7Current Value", "")
                                .info(null, "&a" + ignoreColor)
                                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "Placeholder Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        String placeholderValue = HandlePlaceholders.parsePlaceholders(player, house, placeholder);
        String compareValue = HandlePlaceholders.parsePlaceholders(player, house, this.compareValue);

        if (ignoreCase) {
            placeholderValue = placeholderValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        if (ignoreColor) {
            placeholderValue = Color.removeColor(placeholderValue);
            compareValue = Color.removeColor(compareValue);
        }

        return Comparator.compare(comparator, placeholderValue, compareValue);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("placeholder", placeholder);
        data.put("comparator", comparator.name());
        data.put("compareValue", compareValue);
        data.put("ignoreCase", ignoreCase);
        data.put("ignoreColor", ignoreColor);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "placeholder";
    }
}
