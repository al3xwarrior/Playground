package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Action.Actions.PlaySoundAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class LocationMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PlaySoundAction action;
    private EventType event;

    public LocationMenu(Main main, Player player, HousingWorld house, PlaySoundAction action, EventType event) {
        super(player, colorize("&7Select Option"), 36);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
    }

    @Override
    public void setupItems() {
        ItemStack houseSpawnItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta houseSpawnItemMeta = houseSpawnItem.getItemMeta();
        houseSpawnItemMeta.setDisplayName(colorize("&aHouse Spawn Location"));
        houseSpawnItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getLocation().equals(Locations.HOUSE_SPAWN)) houseSpawnItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        houseSpawnItem.setItemMeta(houseSpawnItemMeta);
        addItem(10, houseSpawnItem, () -> {
            action.setLocation(Locations.HOUSE_SPAWN);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        ItemStack invokersLocationItem = new ItemStack(Material.COMPASS);
        ItemMeta invokersLocationItemMeta = invokersLocationItem.getItemMeta();
        invokersLocationItemMeta.setDisplayName(colorize("&aInvokers Location"));
        invokersLocationItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getLocation().equals(Locations.HOUSE_SPAWN)) invokersLocationItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        invokersLocationItem.setItemMeta(invokersLocationItemMeta);
        addItem(11, invokersLocationItem, () -> {
            action.setLocation(Locations.INVOKERS_LOCATION);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        ItemStack playerLocationItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta playerLocationItemMeta = playerLocationItem.getItemMeta();
        playerLocationItemMeta.setDisplayName(colorize("&aCurrent Location"));
        playerLocationItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getLocation().equals(Locations.HOUSE_SPAWN)) playerLocationItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        playerLocationItem.setItemMeta(playerLocationItemMeta);
        addItem(11, playerLocationItem, () -> {
            player.sendMessage(colorize("&cNot currently setup yet. Who uses this honestly?"));
        });

        ItemStack customCoordsItem = new ItemStack(Material.OAK_SIGN);
        ItemMeta customCoordsItemMeta = customCoordsItem.getItemMeta();
        customCoordsItemMeta.setDisplayName(colorize("&aCustom Coordinates"));
        customCoordsItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getLocation().equals(Locations.HOUSE_SPAWN)) customCoordsItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        customCoordsItem.setItemMeta(customCoordsItemMeta);
        addItem(11, customCoordsItem, () -> {
            player.sendMessage(colorize("&cNot currently setup yet. Who uses this honestly?"));
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(31, backArrow, () -> {
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });
    }
}
