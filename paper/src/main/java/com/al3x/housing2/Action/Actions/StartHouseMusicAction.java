package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class StartHouseMusicAction extends HTSLImpl {

    public StartHouseMusicAction() {
        super("Start House Music Action");
    }

    @Override
    public String toString() {
        return "StartHouseMusicAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_PIGSTEP);
        builder.name("&eStart House Music");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_PIGSTEP);
        builder.name("&aStart House Music");
        builder.description("Starts the house music.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        house.startMusic();
        return OutputType.SUCCESS;
    }

    @Override
    public List<EventType> disallowedEvents() {
        return Arrays.asList(EventType.PLAYER_QUIT);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "startHouseMusic";
    }
}
