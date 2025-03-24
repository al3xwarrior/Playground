package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

public class HideNPCAction extends HTSLImpl implements NPCAction {

    public HideNPCAction() {
        super("Hide NPC Action");
    }

    @Override
    public String toString() {
        return "HideNPCAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRAY_DYE);
        builder.name("&eHide NPC");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRAY_DYE);
        builder.name("&aHide NPC");
        builder.description("Hides the NPC from the player who triggered the action.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "hideNPC";
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
