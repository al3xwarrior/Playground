package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class SkinSelectorMenu extends Menu {

    private Main main;
    private Player player;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public SkinSelectorMenu(Main main, Player player, HousesManager housesManager, HousingNPC housingNPC) {
        super(player, colorize("&7Change Skin"), 45);
        this.main = main;
        this.player = player;
        this.housesManager = housesManager;
        this.housingNPC = housingNPC;
        setupItems();
    }

    @Override
    public void setupItems() {


        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            new NPCMenu(main, player, housesManager, housingNPC).open();
        });
    }

}
