package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;
import java.util.List;

public class IsNPCHiddenCondition extends CHTSLImpl implements NPCCondition {
    public IsNPCHiddenCondition() {
        super(ConditionEnum.IS_NPC_HIDDEN,
                "Is NPC Hidden",
                "Check if the NPC is hidden.",
                Material.GRAY_DYE,
                List.of("isNPCHidden"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return !player.canSee(npc.getEntity());
    }
}
