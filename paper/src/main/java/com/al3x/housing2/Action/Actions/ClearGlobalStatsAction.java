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
public class ClearGlobalStatsAction extends HTSLImpl {

    public ClearGlobalStatsAction() {
        super(
                ActionEnum.CLEAR_GLOBALSTATS,
                "Clear Global Stats",
                "Clears all global stats.",
                Material.BARRIER,
                List.of("clearGlobalStats")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.getStatManager().getGlobalStats().clear();
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
