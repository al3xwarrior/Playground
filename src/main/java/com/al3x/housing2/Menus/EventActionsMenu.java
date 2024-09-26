package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class EventActionsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public EventActionsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&eEvent Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack playerJoin = new ItemStack(Material.OAK_DOOR);
        ItemMeta playerJoinMeta = playerJoin.getItemMeta();
        playerJoinMeta.setDisplayName(colorize("&aPlayer Join"));
        playerJoin.setItemMeta(playerJoinMeta);
        addItem(10, playerJoin, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Join Event", EventType.PLAYER_JOIN).open();
        });

        ItemStack playerQuit = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta playerQuitMeta = playerQuit.getItemMeta();
        playerQuitMeta.setDisplayName(colorize("&aPlayer Quit"));
        playerQuit.setItemMeta(playerQuitMeta);
        addItem(11, playerQuit, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Quit Event", EventType.PLAYER_QUIT).open();
        });

        ItemStack playerDeath = new ItemStack(Material.BONE);
        ItemMeta playerDeathMeta = playerDeath.getItemMeta();
        playerDeathMeta.setDisplayName(colorize("&aPlayer Death"));
        playerDeath.setItemMeta(playerDeathMeta);
        addItem(12, playerDeath, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Death Event", EventType.PLAYER_DEATH).open();
        });

        ItemStack playerKill = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta playerKillMeta = playerKill.getItemMeta();
        playerKillMeta.setDisplayName(colorize("&aPlayer Kill"));
        playerKill.setItemMeta(playerKillMeta);
        addItem(13, playerKill, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Kill Event", EventType.PLAYER_KILL).open();
        });

        ItemStack playerRespawn = new ItemStack(Material.APPLE);
        ItemMeta playerRespawnMeta = playerRespawn.getItemMeta();
        playerRespawnMeta.setDisplayName(colorize("&aPlayer Respawn"));
        playerRespawn.setItemMeta(playerRespawnMeta);
        addItem(14, playerRespawn, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Respawn Event", EventType.PLAYER_RESPAWN).open();
        });

        ItemStack groupChange = new ItemStack(Material.PAPER);
        ItemMeta groupChangeMeta = groupChange.getItemMeta();
        groupChangeMeta.setDisplayName(colorize("&aGroup Change"));
        groupChange.setItemMeta(groupChangeMeta);
        addItem(15, groupChange, () -> {
            new ActionsMenu(main, player, house, "&eGroup Change Event", EventType.GROUP_CHANGE).open();
        });

        ItemStack pvpStateChange = new ItemStack(Material.IRON_SWORD);
        ItemMeta pvpStateChangeMeta = pvpStateChange.getItemMeta();
        pvpStateChangeMeta.setDisplayName(colorize("&aPvP State Change"));
        pvpStateChange.setItemMeta(pvpStateChangeMeta);
        addItem(16, pvpStateChange, () -> {
            new ActionsMenu(main, player, house, "&ePvP State Change Event", EventType.PVP_STATE_CHANGE).open();
        });

        ItemStack fishCaught = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishCaughtMeta = fishCaught.getItemMeta();
        fishCaughtMeta.setDisplayName(colorize("&aFish Caught"));
        fishCaught.setItemMeta(fishCaughtMeta);
        addItem(19, fishCaught, () -> {
            new ActionsMenu(main, player, house, "&eFish Caught Event", EventType.FISH_CAUGHT).open();
        });

        ItemStack enterPortal = new ItemStack(Material.OBSIDIAN);
        ItemMeta enterPortalMeta = enterPortal.getItemMeta();
        enterPortalMeta.setDisplayName(colorize("&aPlayer Enter Portal"));
        enterPortal.setItemMeta(enterPortalMeta);
        addItem(20, enterPortal, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Enter Portal Event", EventType.PLAYER_ENTER_PORTAL).open();
        });

        ItemStack playerDamage = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta playerDamageMeta = playerDamage.getItemMeta();
        playerDamageMeta.setDisplayName(colorize("&aPlayer Damage"));
        playerDamage.setItemMeta(playerDamageMeta);
        addItem(21, playerDamage, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Damage Event", EventType.PLAYER_DAMAGE).open();
        });

        ItemStack blockBreak = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blockBreakMeta = blockBreak.getItemMeta();
        blockBreakMeta.setDisplayName(colorize("&aPlayer Block Break"));
        blockBreak.setItemMeta(blockBreakMeta);
        addItem(22, blockBreak, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Block Break Event", EventType.PLAYER_BLOCK_BREAK).open();
        });

        ItemStack startParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta startParkourMeta = startParkour.getItemMeta();
        startParkourMeta.setDisplayName(colorize("&aStart Parkour"));
        startParkour.setItemMeta(startParkourMeta);
        addItem(23, startParkour, () -> {
            new ActionsMenu(main, player, house, "&eStart Parkour Event", EventType.START_PARKOUR).open();
        });

        ItemStack completeParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta completeParkourMeta = completeParkour.getItemMeta();
        completeParkourMeta.setDisplayName(colorize("&aComplete Parkour"));
        completeParkour.setItemMeta(completeParkourMeta);
        addItem(24, completeParkour, () -> {
            new ActionsMenu(main, player, house, "&eComplete Parkour Event", EventType.COMPLETE_PARKOUR).open();
        });

        ItemStack dropItem = new ItemStack(Material.DROPPER);
        ItemMeta dropItemMeta = dropItem.getItemMeta();
        dropItemMeta.setDisplayName(colorize("&aPlayer Drop Item"));
        dropItem.setItemMeta(dropItemMeta);
        addItem(25, dropItem, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Drop Item Event", EventType.PLAYER_DROP_ITEM).open();
        });

        ItemStack pickUpItem = new ItemStack(Material.HOPPER);
        ItemMeta pickUpItemMeta = pickUpItem.getItemMeta();
        pickUpItemMeta.setDisplayName(colorize("&aPlayer Pick Up Item"));
        pickUpItem.setItemMeta(pickUpItemMeta);
        addItem(28, pickUpItem, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Pick Up Item Event", EventType.PLAYER_PICKUP_ITEM).open();
        });

        ItemStack changeHeldItem = new ItemStack(Material.BOOK);
        ItemMeta changeHeldItemMeta = changeHeldItem.getItemMeta();
        changeHeldItemMeta.setDisplayName(colorize("&aPlayer Change Held Item"));
        changeHeldItem.setItemMeta(changeHeldItemMeta);
        addItem(29, changeHeldItem, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Change Held Item Event", EventType.PLAYER_CHANGE_HELD_ITEM).open();
        });

        ItemStack toggleSneak = new ItemStack(Material.HAY_BLOCK);
        ItemMeta toggleSneakMeta = toggleSneak.getItemMeta();
        toggleSneakMeta.setDisplayName(colorize("&aPlayer Toggle Sneak"));
        toggleSneak.setItemMeta(toggleSneakMeta);
        addItem(30, toggleSneak, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Toggle Sneak Event", EventType.PLAYER_TOGGLE_SNEAK).open();
        });

        ItemStack toggleFlight = new ItemStack(Material.FEATHER);
        ItemMeta toggleFlightMeta = toggleFlight.getItemMeta();
        toggleFlightMeta.setDisplayName(colorize("&aPlayer Toggle Flight"));
        toggleFlight.setItemMeta(toggleFlightMeta);
        addItem(31, toggleFlight, () -> {
            new ActionsMenu(main, player, house, "&ePlayer Toggle Flight Event", EventType.PLAYER_TOGGLE_FLIGHT).open();
        });
    }
}
