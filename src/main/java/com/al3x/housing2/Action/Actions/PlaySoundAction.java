package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlaySoundAction extends Action {
    private Float volume;
    private Float pitch;
    private Sound sound;
    private Locations location;

    public PlaySoundAction() {
        super("Play Sound Action");
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        this.location = Locations.INVOKERS_LOCATION;
    }

    public PlaySoundAction(Float volume, Float pitch, Sound sound, Locations location) {
        super("Play Sound Action");
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
        this.location = location;
    }

    @Override
    public String toString() {
        return "PlaySoundAction (Volume: " + volume + ", Pitch: " + pitch + ", Sound: " + sound.toString() + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&ePlay Sound");
        builder.info("&eSettings", "");
        builder.info("Value", "&a" + sound);
        builder.info("Volume", "&a" + volume);
        builder.info("Pitch", "&a" + pitch);
        builder.info("Location", "&a" + location);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&aPlay Sound");
        builder.description("Play a sound with a custom pitch to the player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("sound",
                        ItemBuilder.create(Material.NOTE_BLOCK)
                                .name("&eSound")
                                .info("&7Current Value", "")
                                .info(null, "&a" + sound)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Sound.values(), Material.NOTE_BLOCK //Will eventually need to be a custom gui rather than this interface
                ),
                new ActionEditor.ActionItem("volume",
                        ItemBuilder.create(Material.IRON_BLOCK)
                                .name("&eVolume")
                                .info("&7Current Value", "")
                                .info(null, "&a" + volume)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                ),
                new ActionEditor.ActionItem("pitch",
                        ItemBuilder.create(Material.BELL)
                                .name("&ePitch")
                                .info("&7Current Value", "")
                                .info(null, "&a" + pitch)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                ),
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + location)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS
                )
        );

        return new ActionEditor(4, "&ePlayer Stat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        switch (location) {
            case INVOKERS_LOCATION -> player.playSound(player.getLocation(), sound, volume, pitch);
            case HOUSE_SPAWN -> player.playSound(house.getSpawn(), sound, volume, pitch);
        }
        return true;
    }

    public Float getVolume() {
        return volume;
    }
    public Float getPitch() {
        return pitch;
    }
    public Sound getSound() {
        return sound;
    }
    public Locations getLocation() {
        return location;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }
    public void setSound(String s) {
        try {
            sound = Sound.valueOf(s);
        } catch (IllegalArgumentException err) {
            sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        }
    }
    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }
    public void setVolume(Float volume) {
        this.volume = volume;
    }
    public void setLocation(Locations location) {
        this.location = location;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("volume", volume);
        data.put("pitch", pitch);
        data.put("sound", sound.name());
        data.put("location", location.name());
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        volume = NumberUtilsKt.toFloat((Double) data.get("volume"));
        pitch = NumberUtilsKt.toFloat((Double) data.get("pitch"));
        sound = Sound.valueOf((String) data.get("sound"));
        location = Locations.valueOf((String) data.get("location"));
    }
}
