package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class StopHouseMusicAction extends HTSLImpl {

    public StopHouseMusicAction() {
        super("Stop House Music Action");
    }

    @Override
    public String toString() {
        return "StopHouseMusicAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_11);
        builder.name("&eStop House Music");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_11);
        builder.name("&aStop House Music");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        house.stopMusic();
        return true;
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
        return "stopHouseMusic";
    }
}
