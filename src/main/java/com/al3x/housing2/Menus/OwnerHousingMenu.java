package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class OwnerHousingMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public OwnerHousingMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&dHousing Menu"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack protools = new ItemStack(Material.STICK);
        ItemMeta protoolsMeta = protools.getItemMeta();
        protoolsMeta.setDisplayName(colorize("&aPro Tools"));
        protools.setItemMeta(protoolsMeta);
        addItem(0, protools, () -> {
            player.sendMessage("protools");
        });

        ItemStack skullPacks = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta skullPacksMeta = skullPacks.getItemMeta();
        skullPacksMeta.setDisplayName(colorize("&aPro Tools"));
        skullPacks.setItemMeta(skullPacksMeta);
        addItem(3, skullPacks, () -> {
            player.sendMessage("skullpacks");
        });

        ItemStack items = new ItemStack(Material.EMERALD);
        ItemMeta itemsMeta = items.getItemMeta();
        itemsMeta.setDisplayName(colorize("&aItems"));
        items.setItemMeta(itemsMeta);
        addItem(4, items, () -> {
            player.sendMessage("items");
        });

        ItemStack furnature = new ItemStack(Material.RED_BED);
        ItemMeta furnatureMeta = furnature.getItemMeta();
        furnatureMeta.setDisplayName(colorize("&aFurniture"));
        furnature.setItemMeta(furnatureMeta);
        addItem(5, furnature, () -> {
            player.sendMessage("furnature");
        });

        ItemStack systems = new ItemStack(Material.ACTIVATOR_RAIL);
        ItemMeta systemsMeta = systems.getItemMeta();
        systemsMeta.setDisplayName(colorize("&aSystems"));
        systems.setItemMeta(systemsMeta);
        addItem(6, systems, () -> {
            new SystemsMenu(main, player, house).open();
        });
    }
}
