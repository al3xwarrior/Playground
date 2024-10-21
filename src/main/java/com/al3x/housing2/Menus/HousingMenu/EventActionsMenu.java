package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class EventActionsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public EventActionsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Event Actions"), 54);
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
        playerJoinMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player joins the"),
                colorize("&7House."),
                "",
                colorize("&eClick to edit!")
        ));
        playerJoin.setItemMeta(playerJoinMeta);
        addItem(10, playerJoin, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_JOIN).open();
        });

        ItemStack playerQuit = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta playerQuitMeta = playerQuit.getItemMeta();
        playerQuitMeta.setDisplayName(colorize("&aPlayer Quit"));
        playerQuitMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player leaves the"),
                colorize("&7House."),
                "",
                colorize("&eClick to edit!")
        ));
        playerQuit.setItemMeta(playerQuitMeta);
        addItem(11, playerQuit, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_QUIT).open();
        });

        ItemStack playerDeath = new ItemStack(Material.BONE);
        ItemMeta playerDeathMeta = playerDeath.getItemMeta();
        playerDeathMeta.setDisplayName(colorize("&aPlayer Death"));
        playerDeathMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player dies."),
                "",
                colorize("&eClick to edit!")
        ));
        playerDeath.setItemMeta(playerDeathMeta);
        addItem(12, playerDeath, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_DEATH).open();
        });

        ItemStack playerKill = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta playerKillMeta = playerKill.getItemMeta();
        playerKillMeta.setDisplayName(colorize("&aPlayer Kill"));
        playerKillMeta.setLore(Arrays.asList(
                colorize("&7Executes for a player when they kill"),
                colorize("&7another player."),
                "",
                colorize("&eClick to edit!")
        ));
        playerKill.setItemMeta(playerKillMeta);
        addItem(13, playerKill, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_KILL).open();
        });

        ItemStack playerRespawn = new ItemStack(Material.APPLE);
        ItemMeta playerRespawnMeta = playerRespawn.getItemMeta();
        playerRespawnMeta.setDisplayName(colorize("&aPlayer Respawn"));
        playerRespawnMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player respawns"),
                "",
                colorize("&eClick to edit!")
        ));
        playerRespawn.setItemMeta(playerRespawnMeta);
        addItem(14, playerRespawn, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_RESPAWN).open();
        });

        ItemStack groupChange = new ItemStack(Material.PAPER);
        ItemMeta groupChangeMeta = groupChange.getItemMeta();
        groupChangeMeta.setDisplayName(colorize("&aGroup Change"));
        groupChangeMeta.setLore(Arrays.asList(
                colorize("&7Executes when a players group"),
                colorize("&7changes."),
                "",
                colorize("&eClick to edit!")
        ));
        groupChange.setItemMeta(groupChangeMeta);
        addItem(15, groupChange, () -> {
            player.sendMessage(colorize("&cNot ready yet."));
            //new ActionsMenu(main, player, house, "&eGroup Change Event", EventType.GROUP_CHANGE).open();
        });

        ItemStack pvpStateChange = new ItemStack(Material.IRON_SWORD);
        ItemMeta pvpStateChangeMeta = pvpStateChange.getItemMeta();
        pvpStateChangeMeta.setDisplayName(colorize("&aPvP State Change"));
        pvpStateChangeMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player has their"),
                colorize("&7PvP state change, such as changing"),
                colorize("&7between regions"),
                "",
                colorize("&eClick to edit!")
        ));
        pvpStateChange.setItemMeta(pvpStateChangeMeta);
        addItem(16, pvpStateChange, () -> {
            player.sendMessage(colorize("&cNot ready yet."));
            //new ActionsMenu(main, player, house, "&ePvP State Change Event", EventType.PVP_STATE_CHANGE).open();
        });

        ItemStack fishCaught = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishCaughtMeta = fishCaught.getItemMeta();
        fishCaughtMeta.setDisplayName(colorize("&aFish Caught"));
        fishCaughtMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player catches a"),
                colorize("&7fish while fishing."),
                "",
                colorize("&eClick to edit!")
        ));
        fishCaught.setItemMeta(fishCaughtMeta);
        addItem(19, fishCaught, () -> {
            new ActionsMenu(main, player, house, EventType.FISH_CAUGHT).open();
        });

        ItemStack enterPortal = new ItemStack(Material.OBSIDIAN);
        ItemMeta enterPortalMeta = enterPortal.getItemMeta();
        enterPortalMeta.setDisplayName(colorize("&aPlayer Enter Portal"));
        enterPortalMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player uses a"),
                colorize("&7nether or end portal."),
                "",
                colorize("&eClick to edit!")
        ));
        enterPortal.setItemMeta(enterPortalMeta);
        addItem(20, enterPortal, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_ENTER_PORTAL).open();
        });

        ItemStack playerDamage = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta playerDamageMeta = playerDamage.getItemMeta();
        playerDamageMeta.setDisplayName(colorize("&aPlayer Damage"));
        playerDamageMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player takes"),
                colorize("&7damage."),
                "",
                colorize("&eClick to edit!")
        ));
        playerDamage.setItemMeta(playerDamageMeta);
        addItem(21, playerDamage, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_DAMAGE).open();
        });

        ItemStack playerAttack = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta playerAttackMeta = playerAttack.getItemMeta();
        playerAttackMeta.setDisplayName(colorize("&aPlayer Attack"));
        playerAttackMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player damages"),
                colorize("&7another entity."),
                "",
                colorize("&eClick to edit!")
        ));
        playerAttack.setItemMeta(playerAttackMeta);
        addItem(22, playerAttack, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_ATTACK).open();
        });

        ItemStack blockBreak = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blockBreakMeta = blockBreak.getItemMeta();
        blockBreakMeta.setDisplayName(colorize("&aPlayer Block Break"));
        blockBreakMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player attempts to"),
                colorize("&7break a block in the plot."),
                "",
                colorize("&eClick to edit!")
        ));
        blockBreak.setItemMeta(blockBreakMeta);
        addItem(23, blockBreak, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_BLOCK_BREAK).open();
        });

        ItemStack blockPlace = new ItemStack(Material.COBBLESTONE);
        ItemMeta blockPlaceMeta = blockPlace.getItemMeta();
        blockPlaceMeta.setDisplayName(colorize("&aPlayer Block Place"));
        blockPlaceMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player attempts to"),
                colorize("&7place a block in the plot."),
                "",
                colorize("&eClick to edit!")
        ));
        blockPlace.setItemMeta(blockPlaceMeta);
        addItem(24, blockPlace, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_BLOCK_PLACE).open();
        });

        ItemStack startParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta startParkourMeta = startParkour.getItemMeta();
        startParkourMeta.setDisplayName(colorize("&aStart Parkour"));
        startParkourMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player starts doing"),
                colorize("&7parkour."),
                "",
                colorize("&eClick to edit!")
        ));
        startParkour.setItemMeta(startParkourMeta);
        addItem(25, startParkour, () -> {
            player.sendMessage(colorize("&cNot ready yet."));
            //new ActionsMenu(main, player, house, "&eStart Parkour Event", EventType.START_PARKOUR).open();
        });

        ItemStack completeParkour = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta completeParkourMeta = completeParkour.getItemMeta();
        completeParkourMeta.setDisplayName(colorize("&aComplete Parkour"));
        completeParkourMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player"),
                colorize("&7successfully completes the parkour."),
                "",
                colorize("&eClick to edit!")
        ));
        completeParkour.setItemMeta(completeParkourMeta);
        addItem(28, completeParkour, () -> {
            player.sendMessage(colorize("&cNot ready yet."));
            //new ActionsMenu(main, player, house, "&eComplete Parkour Event", EventType.COMPLETE_PARKOUR).open();
        });

        ItemStack dropItem = new ItemStack(Material.DROPPER);
        ItemMeta dropItemMeta = dropItem.getItemMeta();
        dropItemMeta.setDisplayName(colorize("&aPlayer Drop Item"));
        dropItemMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player drops an"),
                colorize("&7item."),
                "",
                colorize("&eClick to edit!")
        ));
        dropItem.setItemMeta(dropItemMeta);
        addItem(29, dropItem, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_DROP_ITEM).open();
        });

        ItemStack pickUpItem = new ItemStack(Material.HOPPER);
        ItemMeta pickUpItemMeta = pickUpItem.getItemMeta();
        pickUpItemMeta.setDisplayName(colorize("&aPlayer Pick Up Item"));
        pickUpItemMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player picks up an"),
                colorize("&7item."),
                "",
                colorize("&eClick to edit!")
        ));
        pickUpItem.setItemMeta(pickUpItemMeta);
        addItem(30, pickUpItem, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_PICKUP_ITEM).open();
        });

        ItemStack changeHeldItem = new ItemStack(Material.BOOK);
        ItemMeta changeHeldItemMeta = changeHeldItem.getItemMeta();
        changeHeldItemMeta.setDisplayName(colorize("&aPlayer Change Held Item"));
        changeHeldItemMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player changes"),
                colorize("&7their held item."),
                "",
                colorize("&eClick to edit!")
        ));
        changeHeldItem.setItemMeta(changeHeldItemMeta);
        addItem(31, changeHeldItem, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_CHANGE_HELD_ITEM).open();
        });

        ItemStack toggleSneak = new ItemStack(Material.HAY_BLOCK);
        ItemMeta toggleSneakMeta = toggleSneak.getItemMeta();
        toggleSneakMeta.setDisplayName(colorize("&aPlayer Toggle Twerk"));
        toggleSneakMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player toggles"),
                colorize("&7sneak."),
                "",
                colorize("&eClick to edit!")
        ));
        toggleSneak.setItemMeta(toggleSneakMeta);
        addItem(32, toggleSneak, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_TOGGLE_SNEAK).open();
        });

        ItemStack toggleFlight = new ItemStack(Material.FEATHER);
        ItemMeta toggleFlightMeta = toggleFlight.getItemMeta();
        toggleFlightMeta.setDisplayName(colorize("&aPlayer Toggle Flight"));
        toggleFlightMeta.setLore(Arrays.asList(
                colorize("&7Executes when a player toggles"),
                colorize("&7flight."),
                "",
                colorize("&eClick to edit!")
        ));
        toggleFlight.setItemMeta(toggleFlightMeta);
        addItem(33, toggleFlight, () -> {
            new ActionsMenu(main, player, house, EventType.PLAYER_TOGGLE_FLIGHT).open();
        });

        ItemStack chatMessage = new ItemBuilder()
                .material(Material.PLAYER_HEAD)
                .name("&aPlayer Chat Message")
                .description("&7Executes when a player sends a chat message.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build();
        addItem(34, chatMessage, () -> {
                    new ActionsMenu(main, player, house, EventType.PLAYER_CHAT).open();
                }
        );


        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            new SystemsMenu(main, player, house).open();
        });
    }
}
