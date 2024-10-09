package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingNPC;
import org.bukkit.entity.Player;

public class HologramEditorMenu extends Menu{

    private Player player;
    private HousingNPC housingNPC;

    public HologramEditorMenu(Player player, String title, int size, HousingNPC housingNPC) {
        super(player, title, size);
        this.player = player;
        this.housingNPC = housingNPC;
    }

    @Override
    public void setupItems() {

    }
}
