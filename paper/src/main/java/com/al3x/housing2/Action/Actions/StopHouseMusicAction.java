package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class StopHouseMusicAction extends HTSLImpl {
    public StopHouseMusicAction() {
        super(
                "stop_house_music_action",
                "Stop House Music",
                "Stops the house music.",
                Material.MUSIC_DISC_11,
                List.of("stopHouseMusic")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.stopMusic();
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
