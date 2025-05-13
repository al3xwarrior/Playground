package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class ShowNPCAction extends HTSLImpl implements NPCAction {

    public ShowNPCAction() {
        super(
                ActionEnum.SHOW_NPC,
                "Show NPC",
                "Shows the NPC from the player who triggered the action.",
                Material.LIME_DYE,
                List.of("showNPC")
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
        if (filter != null && filter.affectsPlayer(player.getUniqueId())) {
            filter.removePlayer(player.getUniqueId());
        }
        player.showEntity(Main.getInstance(), npc.getEntity());
    }

    @Override
    public boolean hide() {
        return true;
    }
}
