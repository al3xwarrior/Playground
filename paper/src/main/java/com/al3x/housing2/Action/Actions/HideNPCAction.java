package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class HideNPCAction extends HTSLImpl implements NPCAction {

    public HideNPCAction() {
        super(
                "hide_npc_action",
                "Hide NPC",
                "Hides the NPC from the player that triggered the action.",
                Material.GRAY_DYE,
                List.of("hideNPC")
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
        PlayerFilter filter = npc.getTraitNullable(PlayerFilter.class);
        if (filter != null && !filter.affectsPlayer(player.getUniqueId())) {
            filter.addPlayer(player.getUniqueId());
        }
        if (npc.getEntity() != null) player.hideEntity(Main.getInstance(), npc.getEntity());
    }

    @Override
    public boolean hide() {
        return true;
    }
}
