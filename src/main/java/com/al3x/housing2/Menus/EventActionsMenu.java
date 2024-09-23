package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class EventActionsMenu extends Menu {

    private Player player;
    private HousingWorld house;

    public EventActionsMenu(Player player, HousingWorld house) {
        super(player, colorize("&eEvent Actions"), 54);
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    protected void setupItems() {
        ItemStack playerJoin = new ItemStack(Material.OAK_DOOR);
        ItemMeta playerJoinMeta = playerJoin.getItemMeta();
        playerJoinMeta.setDisplayName(colorize("&aPlayer Join"));
        playerJoin.setItemMeta(playerJoinMeta);
        addItem(10, playerJoin, () -> {
            new ActionsMenu(player, house, "&ePlayer Join Event", house.getEventActions(EventType.PLAYER_JOIN)).open();
        });

        ItemStack playerQuit = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta playerQuitMeta = playerQuit.getItemMeta();
        playerQuitMeta.setDisplayName(colorize("&aPlayer Quit"));
        playerQuit.setItemMeta(playerQuitMeta);
        addItem(11, playerQuit, () -> {
            player.sendMessage("Player Quit event selected...");
        });

        ItemStack playerDeath = new ItemStack(Material.BONE);
        ItemMeta playerDeathMeta = playerDeath.getItemMeta();
        playerDeathMeta.setDisplayName(colorize("&aPlayer Death"));
        playerDeath.setItemMeta(playerDeathMeta);
        addItem(12, playerDeath, () -> {
            player.sendMessage("Player Death event selected...");
        });

        ItemStack playerKill = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta playerKillMeta = playerKill.getItemMeta();
        playerKillMeta.setDisplayName(colorize("&aPlayer Kill"));
        playerKill.setItemMeta(playerKillMeta);
        addItem(13, playerKill, () -> {
            player.sendMessage("Player Kill event selected...");
        });

        ItemStack playerRespawn = new ItemStack(Material.APPLE);
        ItemMeta playerRespawnMeta = playerRespawn.getItemMeta();
        playerRespawnMeta.setDisplayName(colorize("&aPlayer Respawn"));
        playerRespawn.setItemMeta(playerRespawnMeta);
        addItem(14, playerRespawn, () -> {
            player.sendMessage("Player Respawn event selected...");
        });

        ItemStack groupChange = new ItemStack(Material.PAPER);
        ItemMeta groupChangeMeta = groupChange.getItemMeta();
        groupChangeMeta.setDisplayName(colorize("&aGroup Change"));
        groupChange.setItemMeta(groupChangeMeta);
        addItem(15, groupChange, () -> {
            player.sendMessage("Group Change event selected...");
        });

        ItemStack pvpStateChange = new ItemStack(Material.IRON_SWORD);
        ItemMeta pvpStateChangeMeta = pvpStateChange.getItemMeta();
        pvpStateChangeMeta.setDisplayName(colorize("&aPvP State Change"));
        pvpStateChange.setItemMeta(pvpStateChangeMeta);
        addItem(19, pvpStateChange, () -> {
            player.sendMessage("PvP State Change event selected...");
        });

        ItemStack fishCaught = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishCaughtMeta = fishCaught.getItemMeta();
        fishCaughtMeta.setDisplayName(colorize("&aFish Caught"));
        fishCaught.setItemMeta(fishCaughtMeta);
        addItem(20, fishCaught, () -> {
            player.sendMessage("Fish Caught event selected...");
        });

        ItemStack enterPortal = new ItemStack(Material.OBSIDIAN);
        ItemMeta enterPortalMeta = enterPortal.getItemMeta();
        enterPortalMeta.setDisplayName(colorize("&aPlayer Enter Portal"));
        enterPortal.setItemMeta(enterPortalMeta);
        addItem(21, enterPortal, () -> {
            player.sendMessage("Player Enter Portal event selected...");
        });

        ItemStack playerDamage = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta playerDamageMeta = playerDamage.getItemMeta();
        playerDamageMeta.setDisplayName(colorize("&aPlayer Damage"));
        playerDamage.setItemMeta(playerDamageMeta);
        addItem(22, playerDamage, () -> {
            player.sendMessage("Player Damage event selected...");
        });

        ItemStack blockBreak = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blockBreakMeta = blockBreak.getItemMeta();
        blockBreakMeta.setDisplayName(colorize("&aPlayer Block Break"));
        blockBreak.setItemMeta(blockBreakMeta);
        addItem(23, blockBreak, () -> {
            player.sendMessage("Player Block Break event selected...");
        });

        ItemStack startParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta startParkourMeta = startParkour.getItemMeta();
        startParkourMeta.setDisplayName(colorize("&aStart Parkour"));
        startParkour.setItemMeta(startParkourMeta);
        addItem(24, startParkour, () -> {
            player.sendMessage("Start Parkour event selected...");
        });

        ItemStack completeParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta completeParkourMeta = completeParkour.getItemMeta();
        completeParkourMeta.setDisplayName(colorize("&aComplete Parkour"));
        completeParkour.setItemMeta(completeParkourMeta);
        addItem(25, completeParkour, () -> {
            player.sendMessage("Complete Parkour event selected...");
        });

        ItemStack dropItem = new ItemStack(Material.DROPPER);
        ItemMeta dropItemMeta = dropItem.getItemMeta();
        dropItemMeta.setDisplayName(colorize("&aPlayer Drop Item"));
        dropItem.setItemMeta(dropItemMeta);
        addItem(28, dropItem, () -> {
            player.sendMessage("Player Drop Item event selected...");
        });

        ItemStack pickUpItem = new ItemStack(Material.HOPPER);
        ItemMeta pickUpItemMeta = pickUpItem.getItemMeta();
        pickUpItemMeta.setDisplayName(colorize("&aPlayer Pick Up Item"));
        pickUpItem.setItemMeta(pickUpItemMeta);
        addItem(29, pickUpItem, () -> {
            player.sendMessage("Player Pick Up Item event selected...");
        });

        ItemStack changeHeldItem = new ItemStack(Material.BOOK);
        ItemMeta changeHeldItemMeta = changeHeldItem.getItemMeta();
        changeHeldItemMeta.setDisplayName(colorize("&aPlayer Change Held Item"));
        changeHeldItem.setItemMeta(changeHeldItemMeta);
        addItem(30, changeHeldItem, () -> {
            player.sendMessage("Player Change Held Item event selected...");
        });

        ItemStack toggleSneak = new ItemStack(Material.HAY_BLOCK);
        ItemMeta toggleSneakMeta = toggleSneak.getItemMeta();
        toggleSneakMeta.setDisplayName(colorize("&aPlayer Toggle Sneak"));
        toggleSneak.setItemMeta(toggleSneakMeta);
        addItem(31, toggleSneak, () -> {
            player.sendMessage("Player Toggle Sneak event selected...");
        });

        ItemStack toggleFlight = new ItemStack(Material.FEATHER);
        ItemMeta toggleFlightMeta = toggleFlight.getItemMeta();
        toggleFlightMeta.setDisplayName(colorize("&aPlayer Toggle Flight"));
        toggleFlight.setItemMeta(toggleFlightMeta);
        addItem(32, toggleFlight, () -> {
            player.sendMessage("Player Toggle Flight event selected...");
        });
    }
}
