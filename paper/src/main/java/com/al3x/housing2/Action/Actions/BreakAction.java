package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

@ToString
public class BreakAction extends HTSLImpl implements NPCAction {

    public BreakAction() {
        super(
                "break_action",
                "Break Block",
                "Breaks a block at the specified location.",
                Material.STONE_PICKAXE
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //it isnt used so :shrug:
    }

    @Override
    public int limit() {
        return 1;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
