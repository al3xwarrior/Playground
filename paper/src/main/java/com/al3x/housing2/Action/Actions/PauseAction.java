package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Duple;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class PauseAction extends HTSLImpl implements NPCAction {

    public PauseAction() {
        super(
                "pause_action",
                "Pause Action",
                "Pauses execution of the remaining actions for the specified amount of ticks.",
                Material.CLOCK,
                List.of("pause")
        );

        getProperties().add(
                new NumberProperty(
                        "duration",
                        "Duration",
                        "The amount of ticks to wait before continuing. 1 second is 20 ticks."
                ).setValue("5")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.RUNNING;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
