package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class GlobalStatRequirementCondition extends CHTSLImpl implements NPCCondition {
    public GlobalStatRequirementCondition() {
        super(ConditionEnum.GLOBALSTAT_REQUIREMENT,
                "Global Stat Requirement",
                "Checks if the global stat matches the provided condition.",
                Material.PLAYER_HEAD,
                List.of("stat", "comparator", "compareValue", "ignoreCase")
        );
        getProperties().addAll(List.of(
                new StringProperty(
                        "stat",
                        "Stat Name",
                        "The name of the global stat to check for."
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
    public String toString() {
        return "GlobalStatRequirementCondition";
    }

    @Override
    public ItemBuilder createDisplayItem() {
        return super.createDisplayItem()
                .skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        super.createAddDisplayItem(builder);
        builder.skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String statName = getProperty("stat", StringProperty.class).parsedValue(house, player);
        Stat stat = house.getStatManager().getGlobalStatByName(statName);
        String compareValue = getProperty("compareValue", StringProperty.class).parsedValue(house, player);
        String statValue = stat.getValue();

        if (getProperty("ignoreCase", BooleanProperty.class).getValue()) {
            statValue = statValue.toLowerCase();
            compareValue = compareValue.toLowerCase();
        }

        StatComparator comparator = getValue("comparator", StatComparator.class);

        return Comparator.compare(comparator, statValue, compareValue) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

//    @Override
//    public String export(int indent) {
//        String compareValue = this.compareValue;
//        if (compareValue.contains(" ")) {
//            compareValue = "\"" + compareValue + "\"";
//        }
//        return "globalstat " + stat + " " + comparator.name() + " " + compareValue + " " + ignoreCase;
//    }
//
//    @Override
//    public void importCondition(String action, List<String> nextLines) {
//        String[] parts = action.split(" ");
//        Duple<String[], String> statArg = handleArg(parts, 0);
//        this.stat = statArg.getSecond();
//        parts = statArg.getFirst();
//        if (parts.length == 0) {
//            return;
//        }
//        this.comparator = StatComparator.getComparator(parts[0]);
//        Duple<String[], String> compareValueArg = handleArg(parts, 1);
//        compareValue = compareValueArg.getSecond();
//        parts = compareValueArg.getFirst();
//        if (parts.length > 0) {
//            ignoreCase = Boolean.parseBoolean(parts[0]);
//        }
//    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return execute(player, house) == OutputType.TRUE;
    }
}
