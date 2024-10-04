package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlaySoundAction implements Action{

    private HousingWorld house;
    private Float volume;
    private Float pitch;
    private Sound sound;
    private Locations location;

    public PlaySoundAction(Player player, HousingWorld house) {
        this.house = house;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        this.location = Locations.INVOKERS_LOCATION;
    }

    public PlaySoundAction(Player player, HousingWorld house, Float volume, Float pitch, Sound sound, Locations location) {
        this.house = house;
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
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&ePlay Sound Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Plays a sound to the player."),
                "",
                colorize("&eSettings:"),
                colorize("&fSound: " + getSound()),
                colorize("&fVolume: " + getVolume()),
                colorize("&fPitch: " + getPitch()),
                colorize("&fLocation: " + getLocation()),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player) {
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
}
