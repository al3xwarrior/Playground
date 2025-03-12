package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

public class IsNPCHiddenCondition extends CHTSLImpl implements NPCCondition {

    public IsNPCHiddenCondition() {
        super("Is NPC Hidden");
    }

    @Override
    public String toString() {
        return "IsNPCHiddenCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRAY_DYE);
        builder.name("&eIs NPC Hidden");
        builder.description("Check if the NPC is hidden.");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRAY_DYE);
        builder.name("&eIs NPC Hidden");
        builder.description("Check if the NPC is hidden.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
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
        return "isNPCHidden";
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return !player.canSee(npc.getEntity());
    }
}
