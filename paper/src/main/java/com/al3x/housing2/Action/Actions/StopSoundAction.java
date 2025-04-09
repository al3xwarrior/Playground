package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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

    private Sound sound = Sound.ENTITY_PLAYER_LEVELUP; //Default sound
    private boolean clearAll = false;

    public StopSoundAction() {
        super("stop_sound_action",
                "Stop Sound",
                "Stops a sound effect.",
                Material.QUARTZ,
                List.of("stopSound")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "sound",
                        "Sound",
                        "The sound to stop.",
                        ActionProperty.PropertyType.SOUND
                ),
                new ActionProperty(
                        "clearAll",
                        "Clear All",
                        "If true, this will clear all sound effects except the one selected.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (clearAll) {
            for (Sound sound : Sound.values()) { //I don't love this, but its the same implementation as the clear potion effect action
                if (sound == this.sound) continue;
                player.stopSound(sound);
            }
        } else {
            player.stopSound(sound);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("sound", sound.name());
        data.put("clearall", clearAll);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        //Bunch of errors coming from PotionEffectType so I needed to add this
        if (!data.containsKey("sound")) return;
        sound = Sound.valueOf((String) data.get("sound"));
        if (!data.containsKey("clearall")) return;
        clearAll = (boolean) data.get("clearall");
    }
}
