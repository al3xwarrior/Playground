package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@ToString
@Getter
public class SetGamemodeAction extends HTSLImpl {
    private Gamemodes gamemode = Gamemodes.SURVIVAL;

    public SetGamemodeAction() {
        super(
                "set_gamemode_action",
                "Set Gamemode",
                "Sets the gamemode of a player.",
                Material.DAYLIGHT_DETECTOR,
                List.of("gamemode")
        );

        getProperties().add(
                new ActionProperty(
                        "gamemode",
                        "Gamemode",
                        "The gamemode to set.",
                        ActionProperty.PropertyType.ENUM,
                        Gamemodes.class
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (player != null) {
            player.setGameMode(gamemode.getGameMode());
        }
        return OutputType.SUCCESS;
    }

    @Override
    public List<EventType> disallowedEvents() {
        return Arrays.asList(EventType.PLAYER_QUIT);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
