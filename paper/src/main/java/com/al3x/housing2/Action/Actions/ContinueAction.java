package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class ContinueAction extends HTSLImpl implements NPCAction {

    public ContinueAction() {
        super(
                "continue_action",
                "Continue",
                "Skips the current remaining actions and continues to the next action.",
                Material.HAY_BLOCK,
                List.of("continue")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.CONTINUE;
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

}
