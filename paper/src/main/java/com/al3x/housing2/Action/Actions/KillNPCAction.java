package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

public class KillNPCAction extends HTSLImpl implements NPCAction {

    public KillNPCAction() {
        super("Kill NPC Action");
    }

    @Override
    public String toString() {
        return "KillNPCAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&eKill NPC");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&aKill NPC");
        builder.description("Kills the NPC");
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
        return "killNPC";
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
