package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaceholderRequirementCondition extends CHTSLImpl {
    public PlaceholderRequirementCondition() {
        super(ConditionEnum.PLACEHOLDER_REQUIREMENT,
                "Placeholder Requirement",
                "Requires a placeholder to match the provided condition.",
                Material.OAK_SIGN,
                List.of("placeholder"));

        getProperties().addAll(List.of(
                new StringProperty(
                        "placeholder",
                        "Placeholder",
                        "The placeholder to check."
                ).setValue("Kills"),
                new EnumProperty<>(
                        "comparator",
                        "Comparator",
                        "The comparator to use for the placeholder.",
                        StatComparator.class
                ).setValue(StatComparator.EQUALS),
                new StringProperty(
                        "compareValue",
                        "Compare Value",
                        "The value to compare the placeholder against."
                ).setValue("1.0"),
                new BooleanProperty(
                        "ignoreCase",
                        "Ignore Case",
                        "Whether to ignore case when comparing the placeholder value."
                ).setValue(false),
                new BooleanProperty(
                        "ignoreColor",
                        "Ignore Color",
                        "Whether to ignore colors when comparing the placeholder value."
                ).setValue(false)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String placeholderValue = getProperty("placeholder", StringProperty.class).parsedValue(house, player);
        String compareValue = getProperty("compareValue", StringProperty.class).parsedValue(house, player);

        if (getValue("ignoreCase", Boolean.class)) {
            placeholderValue = placeholderValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        if (getValue("ignoreColor", Boolean.class)) {
            placeholderValue = Color.removeColor(placeholderValue);
            compareValue = Color.removeColor(compareValue);
        }

        return Comparator.compare(getValue("comparator", StatComparator.class), placeholderValue, compareValue)
                ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
