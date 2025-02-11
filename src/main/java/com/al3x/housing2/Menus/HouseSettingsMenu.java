package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.JukeboxMenu;
import com.al3x.housing2.Menus.HousingMenu.TimeSelectorMenu;
import com.al3x.housing2.Menus.HousingMenu.WeatherSelectorMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class HouseSettingsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public HouseSettingsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7House Settings"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {

        addItem(10, ItemBuilder.create(Material.CLOCK)
                .name(colorize("&aTime Selector"))
                .description("Click to select the time settings for your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            new TimeSelectorMenu(main, player, house).open();
        });

        addItem(11, ItemBuilder.create(Material.WATER_BUCKET)
                .name(colorize("&aWeather Selector"))
                .description("Click to select the weather settings for your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            new WeatherSelectorMenu(main, player, house).open();
        });

        addItem(12, ItemBuilder.create(Material.OAK_SIGN)
                .name(colorize("&aSet House Name"))
                .description("Click to change the name of your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eType the message you would like for the &ahouse name&e."));
            openChat(main, house.getName(), (message) -> {
                house.setName(message);
            });
        });

        addItem(13, ItemBuilder.create(Material.OAK_SIGN)
                .name(colorize("&aSet House Description"))
                .description("Click to change the description of your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eType the message you would like for the &ahouse description&e."));
            openChat(main, house.getDescription(), (message) -> {
                house.setDescription(message);
            });
        });

        addItem(14, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aJoin/Leave Messages"))
                .description("Click to toggle the join/leave messages for your house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (house.getJoinLeaveMessages()) {
                house.setJoinLeaveMessages(false);
                player.sendMessage(colorize("&eDisabled &cJoin/Leave Messages"));
            } else {
                house.setJoinLeaveMessages(true);
                player.sendMessage(colorize("&eEnabled &aJoin/Leave Messages"));
            }
        });

        addItem(15, ItemBuilder.create(Material.JUKEBOX)
                .name(colorize("&aMusic Settings"))
                .description("Click to configure Music settings.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            if (house.hasPermission(player, Permissions.JUKEBOX)) {
                new JukeboxMenu(main, player, house).open();
                return;
            }
            player.sendMessage(colorize("&cYou do not have permission to modify the jukebox!"));
        });

        addItem(16, ItemBuilder.create(Material.SKELETON_SKULL)
                .name(colorize("&aDeath Messages"))
                .description("Click to toggle the death messages for your house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (house.getDeathMessages()) {
                house.setDeathMessages(false);
                player.sendMessage(colorize("&eDisabled &cDeath Messages"));
            } else {
                house.setDeathMessages(true);
                player.sendMessage(colorize("&eEnabled &aDeath Messages"));
            }
        });

        addItem(19, ItemBuilder.create(Material.CHEST)
                .name(colorize("&aKeep Inventory"))
                .description("Click to toggle keep inventory on your house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (house.getKeepInventory()) {
                house.setKeepInventory(false);
                player.sendMessage(colorize("&eDisabled &cKeep Inventory"));
            } else {
                house.setKeepInventory(true);
                player.sendMessage(colorize("&eEnabled &aKeep Inventory"));
            }
        });

        addItem(49, ItemBuilder.create(Material.NETHER_STAR)
                .name(colorize("&aMain Menu"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), () -> new HousingMenu(main, player, house).open());

    }
}
