package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class SkinSelectorMenu extends Menu {

    private Main main;
    private Player player;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public SkinSelectorMenu(Main main, Player player, HousesManager housesManager, HousingNPC housingNPC) {
        super(player, colorize("&7Change Skin"), 54);
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
            new NPCMenu(main, player, housingNPC).open();
        });
    }

}
