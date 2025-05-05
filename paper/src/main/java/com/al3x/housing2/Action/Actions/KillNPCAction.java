package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class KillNPCAction extends HTSLImpl implements NPCAction {

    public KillNPCAction() {
        super(
                ActionEnum.KILL_NPC,
                "Kill NPC"
                , "Kills the NPC.",
                Material.IRON_BARS,
                List.of("killNPC")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            le.setHealth(0.1);
        }
    }

    @Override
    public boolean hide() {
        return true;
    }
}
