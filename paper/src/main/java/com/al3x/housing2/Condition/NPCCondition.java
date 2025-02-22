package com.al3x.housing2.Condition;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Instances.HousingWorld;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public interface NPCCondition {
    boolean npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor);

    //Hide from non npc conditions
    default boolean hide() {
        return false;
    }
}
