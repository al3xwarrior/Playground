package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.DoubleProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class HungerRequirementCondition extends CHTSLImpl {
    public HungerRequirementCondition() {
        super(ConditionEnum.HUNGER_REQUIREMENT,
                "Hunger Requirement",
                "Requires the users current hunger level to match the provided condition.",
                Material.COOKED_BEEF,
                List.of("hunger"));

        getProperties().add(new EnumProperty<>(
                "comparator",
                "Mode",
                "The comparator to use for the health amount.",
                StatComparator.class
        ).setValue(StatComparator.EQUALS));

        getProperties().add(new DoubleProperty(
                "compareValue",
                "Amount",
                "The health amount to check against."
        ).setValue(20.0));
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        StatComparator comparator = getValue("comparator", StatComparator.class);
        double compareValue = getValue("compareValue", DoubleProperty.class).getValue();
        return Comparator.compare(comparator, player.getFoodLevel(), compareValue) ? OutputType.TRUE : OutputType.FALSE;
    }
}
