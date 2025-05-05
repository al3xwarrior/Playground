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
public class FullHealAction extends HTSLImpl implements NPCAction {

    public FullHealAction() {
        super(
                ActionEnum.FULL_HEAL,
                "Full Heal",
                "Fully heals the player.",
                Material.GOLDEN_APPLE,
                List.of("fullHeal")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.setHealth(player.getMaxHealth());
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        le.setHealth(le.getMaxHealth());
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
