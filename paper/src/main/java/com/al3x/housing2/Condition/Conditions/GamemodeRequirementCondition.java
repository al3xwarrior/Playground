package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class GamemodeRequirementCondition extends CHTSLImpl {
    public GamemodeRequirementCondition() {
        super(ConditionEnum.GAMEMODE_REQUIREMENT,
                "Gamemode Requirement",
                "Checks if the player is in the specified gamemode.",
                Material.DAYLIGHT_DETECTOR,
                List.of("gamemode")
        );
        getProperties().add(new EnumProperty<>(
                "gamemode",
                "Gamemode",
                "The gamemode to check for.",
                Gamemodes.class
        ).setValue(Gamemodes.SURVIVAL));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return player.getGameMode() == getValue("gamemode", Gamemodes.class).getGameMode() ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
