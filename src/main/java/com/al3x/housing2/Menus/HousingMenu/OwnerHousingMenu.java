package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.HouseSettingsMenu;
import com.al3x.housing2.Menus.ItemsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class OwnerHousingMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public OwnerHousingMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Housing Menu"), 45);
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

        ItemStack blocks = new ItemStack(Material.BRICKS);
        ItemMeta blocksMeta = blocks.getItemMeta();
        blocksMeta.setDisplayName(colorize("&aBlocks"));
        blocks.setItemMeta(blocksMeta);
        addItem(2, blocks, () -> {
            player.sendMessage(colorize("&cUse the creative inventory stupid."));
        });

        ItemStack plants = new ItemStack(Material.ROSE_BUSH);
        ItemMeta plantsMeta = plants.getItemMeta();
        plantsMeta.setDisplayName(colorize("&aPlants"));
        plants.setItemMeta(plantsMeta);
        addItem(3, plants, () -> {
            player.sendMessage(colorize("&cUse the creative inventory stupid."));
        });

        ItemStack skullPacks = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta skullPacksMeta = skullPacks.getItemMeta();
        skullPacksMeta.setDisplayName(colorize("&aSkull Packs"));
        skullPacks.setItemMeta(skullPacksMeta);
        addItem(4, skullPacks, () -> {
            player.sendMessage("skullpacks");
        });

        ItemStack items = new ItemStack(Material.EMERALD);
        ItemMeta itemsMeta = items.getItemMeta();
        itemsMeta.setDisplayName(colorize("&aItems"));
        items.setItemMeta(itemsMeta);
        addItem(5, items, () -> {
            new ItemsMenu(main, player, house).open();
        });

        ItemStack furnature = new ItemStack(Material.RED_BED);
        ItemMeta furnatureMeta = furnature.getItemMeta();
        furnatureMeta.setDisplayName(colorize("&aFurniture"));
        furnature.setItemMeta(furnatureMeta);
        addItem(6, furnature, () -> {
            player.sendMessage("furnature");
        });

        ItemStack collectibles = new ItemStack(Material.CHEST);
        ItemMeta collectiblesMeta = collectibles.getItemMeta();
        collectiblesMeta.setDisplayName(colorize("&aCollectibles"));
        collectibles.setItemMeta(collectiblesMeta);
        addItem(8, collectibles, () -> {
            player.sendMessage(colorize("&cI don't feel like coding this."));
        });

        ItemStack pvp = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta pvpMeta = pvp.getItemMeta();
        pvpMeta.setDisplayName(colorize("&aPvP"));
        pvp.setItemMeta(pvpMeta);
        addItem(11, pvp, () -> {
            player.sendMessage(colorize("&cUse the creative inventory stupid."));
        });

        ItemStack weatherSelector = new ItemStack(Material.PINK_TULIP);
        ItemMeta weatherSelectorMeta = weatherSelector.getItemMeta();
        weatherSelectorMeta.setDisplayName(colorize("&aWeather Selector"));
        weatherSelector.setItemMeta(weatherSelectorMeta);
        addItem(12, weatherSelector, () -> {
            player.sendMessage("Weather Selector");
        });

        ItemStack biomesSkies = new ItemStack(Material.DEAD_BUSH);
        ItemMeta biomesSkiesMeta = biomesSkies.getItemMeta();
        biomesSkiesMeta.setDisplayName(colorize("&aBiomes & Skies"));
        biomesSkies.setItemMeta(biomesSkiesMeta);
        addItem(13, biomesSkies, () -> {
            player.sendMessage("Biomes & Skies");
        });

        ItemStack themes = new ItemStack(Material.PAINTING);
        ItemMeta themesMeta = themes.getItemMeta();
        themesMeta.setDisplayName(colorize("&aThemes"));
        themes.setItemMeta(themesMeta);
        addItem(14, themes, () -> {
            player.sendMessage(colorize("&cI might code this later idk."));
        });

        ItemStack travel = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta travelMeta = travel.getItemMeta();
        travelMeta.setDisplayName(colorize("&aTravel to someone else's house"));
        travel.setItemMeta(travelMeta);
        addItem(27, travel, () -> {
            new HouseBrowserMenu(player, main.getHousesManager()).open();
        });

        ItemStack rewards = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta rewardsMeta = rewards.getItemMeta();
        rewardsMeta.setDisplayName(colorize("&aRewards"));
        rewards.setItemMeta(rewardsMeta);
        addItem(30, rewards, () -> {
            player.sendMessage("Rewards");
        });

        ItemStack systems = new ItemStack(Material.ACTIVATOR_RAIL);
        ItemMeta systemsMeta = systems.getItemMeta();
        systemsMeta.setDisplayName(colorize("&aSystems"));
        systems.setItemMeta(systemsMeta);
        addItem(31, systems, () -> {
            new SystemsMenu(main, player, house).open();
        });

        ItemStack visibility = new ItemStack(Material.GRAY_DYE);
        ItemMeta visibilityMeta = visibility.getItemMeta();
        visibilityMeta.setDisplayName(colorize("&aVisibility"));
        visibility.setItemMeta(visibilityMeta);
        addItem(32, visibility, () -> {
            player.sendMessage("Visibility");
        });

        ItemStack buildMode = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta buildModeMeta = buildMode.getItemMeta();
        buildModeMeta.setDisplayName(colorize("&aMode: &eBuild Mode"));
        buildMode.setItemMeta(buildModeMeta);
        addItem(35, buildMode, () -> {
            player.sendMessage("Build Mode");
        });

        ItemStack search = new ItemStack(Material.COMPASS);
        ItemMeta searchMeta = search.getItemMeta();
        searchMeta.setDisplayName(colorize("&aSearch"));
        search.setItemMeta(searchMeta);
        addItem(36, search, () -> {
            player.sendMessage("Search");
        });

        ItemBuilder visitingRules = ItemBuilder.create(Material.PLAYER_HEAD);
        visitingRules.name("&aVisiting Rules");
        visitingRules.info("&7Current Privacy", "&a" + house.getPrivacy().asString());
        visitingRules.lClick(ItemBuilder.ActionType.TOGGLE_YELLOW);
        addItem(38, visitingRules.build(), () -> {
            //thanks chatgippity lol, I would have made this a lot more complicated
            house.setPrivacy(HousePrivacy.values()[(house.getPrivacy().ordinal() + 1) % HousePrivacy.values().length]);
            player.sendMessage(colorize("&fPrivacy set to " + house.getPrivacy().asString()));
            setupItems();
        });

        ItemStack houseSettings = new ItemStack(Material.COMPARATOR);
        ItemMeta houseSettingsMeta = houseSettings.getItemMeta();
        houseSettingsMeta.setDisplayName(colorize("&aHouse Settings"));
        houseSettings.setItemMeta(houseSettingsMeta);
        addItem(39, houseSettings, () -> {
            new HouseSettingsMenu(main, player, house).open();
        });

        ItemStack permissionsGroups = new ItemStack(Material.FILLED_MAP);
        ItemMeta permissionsGroupsMeta = permissionsGroups.getItemMeta();
        permissionsGroupsMeta.setDisplayName(colorize("&aPermissions and Groups"));
        permissionsGroups.setItemMeta(permissionsGroupsMeta);
        addItem(40, permissionsGroups, () -> {
            player.sendMessage("Permissions and Groups");
        });

        ItemStack playerListing = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta playerListingMeta = playerListing.getItemMeta();
        playerListingMeta.setDisplayName(colorize("&aPlayer Listing"));
        playerListing.setItemMeta(playerListingMeta);
        addItem(41, playerListing, () -> {
            player.sendMessage("Player Listing");
        });

        ItemStack clearInventory = new ItemStack(Material.CAULDRON);
        ItemMeta clearInventoryMeta = clearInventory.getItemMeta();
        clearInventoryMeta.setDisplayName(colorize("&aClear Inventory"));
        clearInventory.setItemMeta(clearInventoryMeta);
        addItem(42, clearInventory, () -> {
            player.closeInventory();
            player.getInventory().clear();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        });

        ItemStack jukebox = new ItemStack(Material.JUKEBOX);
        ItemMeta jukeboxMeta = jukebox.getItemMeta();
        jukeboxMeta.setDisplayName(colorize("&aJukebox"));
        jukebox.setItemMeta(jukeboxMeta);
        addItem(44, jukebox, () -> {
            player.sendMessage("Jukebox");
        });

    }
}
