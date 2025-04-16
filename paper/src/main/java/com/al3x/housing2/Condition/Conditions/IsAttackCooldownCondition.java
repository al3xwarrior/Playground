package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class IsAttackCooldownCondition extends CHTSLImpl {
    public IsAttackCooldownCondition() {
        super(ConditionEnum.IS_ATTACK_COOLDOWN,
                "Is Attack Cooldown",
                "Is the player on attack cooldown?",
                Material.CLOCK,
                List.of("isAttackCooldown"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return player.getAttackCooldown() != 1.0f ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
