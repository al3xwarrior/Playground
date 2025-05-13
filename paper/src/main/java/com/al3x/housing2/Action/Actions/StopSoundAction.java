package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.SoundProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
@Getter
@Setter
public class StopSoundAction extends HTSLImpl {
    public StopSoundAction() {
        super(ActionEnum.STOP_SOUND,
                "Stop Sound",
                "Stops a sound effect.",
                Material.QUARTZ,
                List.of("stopSound")
        );

        getProperties().addAll(List.of(
                new SoundProperty(
                        "sound",
                        "Sound",
                        "The sound to stop."
                ).setValue(Sound.ENTITY_PLAYER_LEVELUP),
                new BooleanProperty(
                        "clearAll",
                        "Clear All",
                        "If true, this will clear all sound effects except the one selected."
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        boolean clearAll = getValue("clearAll", Boolean.class);
        Sound s = getValue("sound", Sound.class);
        if (clearAll) {
            for (Sound sound : Sound.values()) { //I don't love this, but its the same implementation as the clear potion effect action
                if (sound == s) continue;
                player.stopSound(sound);
            }
        } else {
            player.stopSound(s);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
