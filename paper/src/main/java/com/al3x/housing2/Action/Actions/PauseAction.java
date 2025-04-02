package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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
    String duration = "5.0"; // in ticks

    public PauseAction() {
        super(
                "pause_action",
                "Pause Action",
                "Pauses execution of the remaining actions for the specified amount of ticks.",
                Material.CLOCK,
                List.of("pause")
        );

        getProperties().add(
                new ActionProperty(
                        "duration",
                        "Duration",
                        "The amount of ticks to wait before continuing. 1 second is 20 ticks.",
                        ActionProperty.PropertyType.STRING
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.RUNNING;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (data.containsKey("duration")) {
            duration = data.get("duration").toString();
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String export(int indent) {
        return "pause " + (duration.contains(" ") ? "\"" + duration + "\"" : duration);
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        Duple<String[], String> durationArg = handleArg(action.split(" "), 0);
        duration = durationArg.getSecond();
        return nextLines;
    }
}
