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
public class ExitAction extends HTSLImpl implements NPCAction {

    public ExitAction() {
        super(
                "exit_action",
                "Exit",
                "Stops executing any remaining actions.",
                Material.BEDROCK,
                List.of("exit", "stop", "return")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.EXIT;
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