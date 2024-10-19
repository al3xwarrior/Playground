package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

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
                .name(colorize("&aWeather Selector"))
                .description("Click to select the weather settings for your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Weather Selector selected");
        });

        addItem(11, ItemBuilder.create(Material.PLAYER_HEAD)
                .name(colorize("&aMax Players"))
                .description("Click to change the maximum number of players allowed.")
                .skullTexture("889ee0b7fef957ed9b464756e9e5615468a9c40c6c0b13f451f33b4103891eab")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Max Players selected");
        });

        addItem(12, ItemBuilder.create(Material.OAK_SIGN)
                .name(colorize("&aSet House Name"))
                .description("Click to change the name of your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage(colorize("&eType the message you would like for the &ahouse description&e."));
            openChat(main, (message) -> {
                house.setName(message);
            });
        });

        addItem(13, ItemBuilder.create(Material.OAK_SIGN)
                .name(colorize("&aSet House Description"))
                .description("Click to change the description of your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage(colorize("&eType the message you would like for the &ahouse description&e."));
            openChat(main, (message) -> {
                house.setDescription(message);
            });
        });

        addItem(14, ItemBuilder.create(Material.GRASS_BLOCK)
                .name(colorize("&aPlot Size"))
                .description("Click to modify the plot size.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Plot Size selected");
        });

        addItem(15, ItemBuilder.create(Material.NAME_TAG)
                .name(colorize("&aHouse Tags"))
                .description("Click to edit the house tags.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("House Tags selected");
        });

        addItem(16, ItemBuilder.create(Material.BOOK)
                .name(colorize("&aHouse Language"))
                .description("Click to change the language settings for your house.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("House Language selected");
        });

        addItem(19, ItemBuilder.create(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .name(colorize("&aParkour Settings"))
                .description("Click to configure the parkour settings.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Parkour Settings selected");
        });

        addItem(20, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aJoin/Leave Messages"))
                .description("Click to set the join/leave messages for your house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Join/Leave Messages selected");
        });

        addItem(21, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aDoor/Fence Auto-Reset"))
                .description("Click to enable/disable automatic door and fence resetting.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Door/Fence Auto-Reset selected");
        });

        addItem(22, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aStatuses"))
                .description("Click to manage the status settings of your house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Statuses selected");
        });

        addItem(23, ItemBuilder.create(Material.FEATHER)
                .name(colorize("&aPlayer Data"))
                .description("Click to view or modify player data.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("Player Data selected");
        });

        addItem(24, ItemBuilder.create(Material.IRON_SWORD)
                .name(colorize("&aPvP Settings"))
                .description("Click to configure PvP settings.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("PvP Settings selected");
        });

        addItem(49, ItemBuilder.create(Material.NETHER_STAR)
                .name(colorize("&aMain Menu"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> new OwnerHousingMenu(main, player, house).open());

    }
}
