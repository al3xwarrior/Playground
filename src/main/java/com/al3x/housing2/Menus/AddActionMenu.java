package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class AddActionMenu extends Menu{

    private Player player;
    private HousingWorld house;
    private EventType event;

    public AddActionMenu(Player player, HousingWorld house, EventType event) {
        super(player, colorize("&aAdd Action"), 54);
        this.player = player;
        this.house = house;
        this.event = event;
        setupItems();
    }

    @Override
    public void setupItems() {
        
    }
}
