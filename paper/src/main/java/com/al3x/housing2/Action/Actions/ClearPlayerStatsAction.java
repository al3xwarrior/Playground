package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class ClearPlayerStatsAction extends HTSLImpl {

    public ClearPlayerStatsAction() {
        super(
                "clear_player_stats_action",
                "Clear Player Stats",
                "Clears all of the player's stats.",
                Material.BARRIER,
                List.of("clearPlayerStats")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.getStatManager().getPlayerStats(player).clear();
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
