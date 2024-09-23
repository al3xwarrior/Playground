package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class AddActionMenu extends Menu{

    private Player player;
    private HousingWorld house;

    public AddActionMenu(Player player, HousingWorld house) {
        super(player, colorize("&aAdd Actions"), 54);
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    protected void setupItems() {

    }
}
