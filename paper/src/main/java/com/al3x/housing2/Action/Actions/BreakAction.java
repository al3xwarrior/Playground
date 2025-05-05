package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class BreakAction extends HTSLImpl implements NPCAction {

    public BreakAction() {
        super(
                ActionEnum.BREAK,
                "Break Loop",
                "Breaks a repeat action.",
                Material.STONE_PICKAXE,
                List.of("breakBlock")
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
