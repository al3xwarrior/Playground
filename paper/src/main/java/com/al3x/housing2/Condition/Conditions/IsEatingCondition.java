package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.EatingListener;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class IsEatingCondition extends CHTSLImpl {
    public IsEatingCondition() {
        super(ConditionEnum.IS_EATING,
                "Is Eating",
                "Check if the player is eating. This will also return true if the player is using a blockable item.",
                Material.MELON,
                List.of("isEating"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return EatingListener.isPlayerEating(player) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
