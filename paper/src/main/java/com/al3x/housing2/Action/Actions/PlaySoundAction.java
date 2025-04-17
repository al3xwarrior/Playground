package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.DoubleProperty;
import com.al3x.housing2.Action.Properties.LocationProperty;
import com.al3x.housing2.Action.Properties.SoundProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.*;
import static com.al3x.housing2.Utils.Color.colorize;

@Getter
@ToString
public class PlaySoundAction extends HTSLImpl {
    public PlaySoundAction() {
        super("play_sound_action",
                "Play Sound",
                "Plays a sound at the specified location.",
                Material.NOTE_BLOCK,
                List.of("sound")
        );

        getProperties().addAll(List.of(
                new SoundProperty(
                        "sound",
                        "Sound",
                        "The sound to play."
                ).setValue(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
                new DoubleProperty(
                        "volume",
                        "Volume",
                        "The volume of the sound."
                ).setValue(1.0),
                new DoubleProperty(
                        "pitch",
                        "Pitch",
                        "The pitch of the sound."
                ).setValue(1.0),
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to play the sound at."
                ).setValue(INVOKERS_LOCATION)
        ));
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.playSound(
                getValue("location", LocationProperty.class).getLocation(player, house, player.getLocation(), player.getEyeLocation()),
                getValue("sound", SoundProperty.class).getValue(),
                NumberUtilsKt.toFloat(getValue("volume", DoubleProperty.class).getValue()),
                NumberUtilsKt.toFloat(getValue("pitch", DoubleProperty.class).getValue()));
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
