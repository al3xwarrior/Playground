package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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
    private Double volume = 1.0D;
    private Double pitch = 1.0D;
    private Sound sound = Sound.ENTITY_CREEPER_PRIMED;
    private String customLocation = null;
    private Locations location = INVOKERS_LOCATION;

    public PlaySoundAction() {
        super("play_sound_action",
                "Play Sound",
                "Plays a sound at the specified location.",
                Material.NOTE_BLOCK,
                List.of("sound")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "sound",
                        "Sound",
                        "The sound to play.",
                        ActionProperty.PropertyType.SOUND
                ),
                new ActionProperty(
                        "volume",
                        "Volume",
                        "The volume of the sound.",
                        ActionProperty.PropertyType.DOUBLE
                ),
                new ActionProperty(
                        "pitch",
                        "Pitch",
                        "The pitch of the sound.",
                        ActionProperty.PropertyType.DOUBLE
                ),
                new ActionProperty(
                        "location",
                        "Location",
                        "The location to play the sound at.",
                        ActionProperty.PropertyType.LOCATION
                )
        ));
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        switch (location) {
            case INVOKERS_LOCATION ->
                    player.playSound(player.getLocation(), sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            case HOUSE_SPAWN ->
                    player.playSound(house.getSpawn(), sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            case CUSTOM -> {
                if (customLocation == null) return OutputType.ERROR;
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc != null) player.playSound(loc, sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            }
        }
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("volume", volume);
        data.put("pitch", pitch);
        data.put("sound", sound.name());
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        volume = (Double) data.get("volume");
        pitch = (Double) data.get("pitch");
        sound = Sound.valueOf((String) data.get("sound"));
        customLocation = (String) data.get("customLocation");
        location = Locations.valueOf((String) data.get("location"));
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\""  : location.name();
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + sound.name() + " " + volume + " " + pitch + " " + loc;
    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <sound> <volume> <pitch> <location>";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        sound = Sound.valueOf(parts[0]);
        volume = Double.parseDouble(parts[1]);
        pitch = Double.parseDouble(parts[2]);
        if (Locations.fromString(parts[3]) != null) {
            location = Locations.fromString(parts[3]);
        } else {
            location = CUSTOM;
            Duple<String[], String> locationArg = handleArg(parts, 3);
            customLocation = locationArg.getSecond();
        }
        return nextLines;
    }
}
