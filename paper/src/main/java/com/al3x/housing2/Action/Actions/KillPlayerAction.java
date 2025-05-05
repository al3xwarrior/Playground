package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class KillPlayerAction extends HTSLImpl {

    public KillPlayerAction() {
        super(
                ActionEnum.KILL_PLAYER,
                "Kill Player",
                "Kills the player.",
                Material.IRON_BARS,
                List.of("killPlayer")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.setHealth(0.0);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
