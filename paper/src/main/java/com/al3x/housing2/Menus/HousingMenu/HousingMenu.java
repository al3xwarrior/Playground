package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.HouseSettingsMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.PlayerListingMenu;
import com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions.GroupsMenu;
import com.al3x.housing2.Menus.ItemsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Enums.permissions.Permissions.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class HousingMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public HousingMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Housing Menu"), 5*9);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {

        ItemStack playerListing = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta playerListingMeta = playerListing.getItemMeta();
        playerListingMeta.setDisplayName(colorize("&aPlayer Listing"));
        playerListing.setItemMeta(playerListingMeta);
        addItem(8, playerListing, () -> {
            new PlayerListingMenu(main, player, house).open();
        });

        if (house.hasPermission(player, Permissions.PRO_TOOLS)) {
            ItemStack protools = new ItemStack(Material.STICK);
            ItemMeta protoolsMeta = protools.getItemMeta();
            protoolsMeta.setDisplayName(colorize("&aPro Tools"));
            protools.setItemMeta(protoolsMeta);
            addItem(13, protools, () -> {
                player.sendMessage("protools");
            });
        }

        if (house.hasPermission(player, BUILD)) {
            ItemStack skulls = main.getHeadDatabaseAPI().getItemHead("95806");
            ItemMeta skullsMeta = skulls.getItemMeta();
            skullsMeta.setDisplayName(colorize("&aHeads"));
            skulls.setItemMeta(skullsMeta);
            addItem(20, skulls, () -> {
                new HeadsMenu(main, player, house).open();
            });
        }

        if (house.hasPermission(player, ITEMS)) {
            ItemStack items = new ItemStack(Material.EMERALD);
            ItemMeta itemsMeta = items.getItemMeta();
            itemsMeta.setDisplayName(colorize("&aItems"));
            items.setItemMeta(itemsMeta);
            addItem(23, items, () -> {
                new ItemsMenu(main, player, house).open();
            });
        }

        if (house.hasPermission(player, ITEMS) && Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            CustomStack stack = CustomStack.getInstance("ruby_large");
            if (stack != null) {
                ItemStack items = stack.getItemStack();
                ItemMeta itemsMeta = items.getItemMeta();
                itemsMeta.setDisplayName(colorize("&cCustom Items"));
                items.setItemMeta(itemsMeta);
                addItem(24, items, () -> {
                    new CustomItemBrowserMenu(main, player, house).open();
                });
            }
        }

        ItemStack travel = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta travelMeta = travel.getItemMeta();
        travelMeta.setDisplayName(colorize("&aTravel to someone else's house"));
        travel.setItemMeta(travelMeta);
        addItem(36, travel, () -> {
            new HouseBrowserMenu(player, main.getHousesManager()).open();
        });

        if (house.hasSystem(player)) {
            ItemStack systems = new ItemStack(Material.ACTIVATOR_RAIL);
            ItemMeta systemsMeta = systems.getItemMeta();
            systemsMeta.setDisplayName(colorize("&aSystems"));
            systems.setItemMeta(systemsMeta);
            addItem(22, systems, () -> {
                new SystemsMenu(main, player, house).open();
            });
        }

        if (house.hasPermission(player, Permissions.BUILD) && (house.getOwner().isOnline() || house.hasPermission(player, Permissions.OFFLINE_BUILD))) {
            ItemStack buildMode = new ItemStack(Material.STONE_PICKAXE);
            ItemMeta buildModeMeta = buildMode.getItemMeta();
            buildModeMeta.setDisplayName(colorize("&aMode: &eBuild Mode"));
            buildMode.setItemMeta(buildModeMeta);
            addItem(44, buildMode, () -> {
                player.sendMessage("Build Mode");
            });
        }

        if (house.hasPermission(player, HOUSE_SETTINGS)) {
            ItemBuilder visitingRules = ItemBuilder.create(Material.PLAYER_HEAD);
            visitingRules.name("&aVisiting Rules");
            visitingRules.info("&7Current Privacy", "&a" + house.getPrivacy().asString());
            visitingRules.lClick(ItemBuilder.ActionType.TOGGLE_YELLOW);
            addItem(0, visitingRules.build(), () -> {
                //thanks chatgippity lol, I would have made this a lot more complicated
                house.setPrivacy(HousePrivacy.values()[(house.getPrivacy().ordinal() + 1) % HousePrivacy.values().length]);
                player.sendMessage(colorize("&fPrivacy set to " + house.getPrivacy().asString()));
                setupItems();
            });

            ItemStack houseSettings = new ItemStack(Material.COMPARATOR);
            ItemMeta houseSettingsMeta = houseSettings.getItemMeta();
            houseSettingsMeta.setDisplayName(colorize("&aHouse Settings"));
            houseSettings.setItemMeta(houseSettingsMeta);
            addItem(21, houseSettings, () -> {
                new HouseSettingsMenu(main, player, house).open();
            });
        }

        if (house.hasPermission(player, EDIT_PERMISSIONS_AND_GROUP)) {
            ItemStack permissionsGroups = new ItemStack(Material.FILLED_MAP);
            ItemMeta permissionsGroupsMeta = permissionsGroups.getItemMeta();
            permissionsGroupsMeta.setDisplayName(colorize("&aPermissions and Groups"));
            permissionsGroups.setItemMeta(permissionsGroupsMeta);
            addItem(31, permissionsGroups, () -> {
                new GroupsMenu(player, main, house).open();
            });
        }
    }
}
