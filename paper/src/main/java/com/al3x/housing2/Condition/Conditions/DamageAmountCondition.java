package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.DoubleProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DamageAmountCondition extends CHTSLImpl implements NPCCondition {

    public DamageAmountCondition() {
        super(ConditionEnum.DAMAGE_AMOUNT,
                "Damage Amount Requirement",
                "Checks if the damage amount is the same as the one specified.",
                Material.WOODEN_SWORD,
                List.of("damageAmount")
        );
        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "comparator",
                        "Comparator",
                        "The comparator to use for the damage amount.",
                        StatComparator.class
                ).setValue(StatComparator.GREATER_THAN_OR_EQUAL),
                new DoubleProperty(
                        "compareValue",
                        "Damage Amount",
                        "The damage amount to compare against."
                ).setValue(1.0)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        StatComparator comparator = getValue("comparator", StatComparator.class);
        double compareValue = getValue("compareValue", Double.class);
        if (event.cancellable() instanceof EntityDamageEvent e) {
            return Comparator.compare(comparator, e.getDamage(), compareValue) ? OutputType.TRUE : OutputType.FALSE;
        }
        return OutputType.FALSE;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_DAMAGE, EventType.PLAYER_ATTACK, EventType.NPC_DAMAGE);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        StatComparator comparator = getValue("comparator", StatComparator.class);
        double compareValue = getValue("compareValue", Double.class);
        if (event.cancellable() instanceof EntityDamageByEntityEvent e) {
            return Comparator.compare(comparator, e.getDamage(), compareValue);
        }
        return false;
    }
}
