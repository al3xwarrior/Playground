package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.DoubleProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class HealthRequirementCondition extends CHTSLImpl implements NPCCondition {
    public HealthRequirementCondition() {
        super(ConditionEnum.HEALTH_REQUIREMENT,
                "Health Requirement",
                "Requires the users current health to match the provided condition.",
                Material.APPLE,
                List.of("health"));

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
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        StatComparator comparator = getValue("comparator", StatComparator.class);
        double compareValue = getValue("compareValue", DoubleProperty.class).getValue();
        return Comparator.compare(comparator, player.getHealth(), compareValue) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        StatComparator comparator = getValue("comparator", StatComparator.class);
        double compareValue = getValue("compareValue", DoubleProperty.class).getValue();
        if (npc.getEntity() instanceof LivingEntity le) {
            return Comparator.compare(comparator, le.getHealth(), compareValue);
        } else {
            return false;
        }
    }
}
