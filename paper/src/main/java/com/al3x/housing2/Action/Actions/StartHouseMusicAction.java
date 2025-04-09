package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class StartHouseMusicAction extends HTSLImpl {

    public StartHouseMusicAction() {
        super(
                "start_house_music_action",
                "Start House Music",
                "Starts the house music.",
                Material.MUSIC_DISC_PIGSTEP,
                List.of("startHouseMusic")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.startMusic();
        return OutputType.SUCCESS;
    }

    @Override
    public List<EventType> disallowedEvents() {
        return Arrays.asList(EventType.PLAYER_QUIT);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
