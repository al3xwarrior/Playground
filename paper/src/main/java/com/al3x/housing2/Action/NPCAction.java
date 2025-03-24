package com.al3x.housing2.Action;

import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public interface NPCAction {
    void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor);

    //Hide from non npc actions
    default boolean hide() {
        return false;
    }
}
