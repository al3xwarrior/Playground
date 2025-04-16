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
    public StatRequirementCondition() {
        super(ConditionEnum.STAT_REQUIREMENT,
                "Stat Requirement",
                "Requires a stat to match the provided condition.",
                Material.FEATHER,
                List.of("stat"));

        getProperties().addAll(List.of(
                new StringProperty(
                        "stat",
                        "Stat Name",
                        "The name of the player stat to check for."
                ).setValue("Kills"),
                new EnumProperty<>(
                        "comparator",
                        "Comparator",
                        "The comparator to use for the stat.",
                        StatComparator.class
                ).setValue(StatComparator.EQUALS),
                new StringProperty(
                        "compareValue",
                        "Compare Value",
                        "The value to compare the stat against."
                ).setValue("1.0"),
                new BooleanProperty(
                        "ignoreCase",
                        "Ignore Case",
                        "Whether to ignore case when comparing the stat value."
                ).setValue(false)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String statString = getValue("stat", StringProperty.class).parsedValue(house, player);
        Stat stat = house.getStatManager().getPlayerStatByName(player, statString);
        String compareValue = getValue("compareValue", StringProperty.class).parsedValue(house, player);
        String statValue = stat.getValue();

        if (getValue("ignoreCase", BooleanProperty.class).getValue()) {
            statValue = statValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        return Comparator.compare(getValue("comparator", StatComparator.class), statValue, compareValue)
                ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
