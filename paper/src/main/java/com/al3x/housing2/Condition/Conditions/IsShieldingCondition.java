package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class IsShieldingCondition extends CHTSLImpl {
    public IsShieldingCondition() {
        super(ConditionEnum.IS_SHIELDING,
                "Is Shielding",
                "Check if the player is shielding.",
                Material.SHIELD,
                List.of("isShielding"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return player.isBlocking() ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
