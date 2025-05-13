package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.DamageTypes;
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
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DamageTypeCondition extends CHTSLImpl implements NPCCondition {
    public DamageTypeCondition() {
        super(ConditionEnum.DAMAGE_TYPE,
                "Damage Type Requirement",
                "Checks if the damage type is the same as the one specified.",
                Material.IRON_SWORD,
                List.of("damageType")
        );

        getProperties().add(
                new EnumProperty<>(
                        "damageType",
                        "Damage Type",
                        "The type of damage to check for.",
                        DamageTypes.class
                ).setValue(DamageTypes.FALL)
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        DamageTypes damageType = getValue("damageType", DamageTypes.class);
        if (event.cancellable() instanceof EntityDamageEvent e) {
            return e.getDamageSource().getDamageType() == damageType.getDamageType()
                    ? OutputType.TRUE : OutputType.FALSE;
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
        DamageTypes damageType = getValue("damageType", DamageTypes.class);
        if (event.cancellable() instanceof EntityDamageEvent e) {
            return e.getDamageSource().getDamageType() == damageType.getDamageType();
        }
        return false;
    }
}
