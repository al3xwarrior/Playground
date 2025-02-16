package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class EventActionsMenu extends Menu {

    private final Main main;
    private final Player player;
    private final HousingWorld house;

    int page = 1;

    public EventActionsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Event Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        if (page == 1) {
            addEventActionItem(10, Material.OAK_DOOR, "&aPlayer Join", "&7Executes when a player joins the House.", EventType.PLAYER_JOIN);
            addEventActionItem(11, Material.SPRUCE_DOOR, "&aPlayer Quit", "&7Executes when a player leaves the House.", EventType.PLAYER_QUIT);
            addEventActionItem(12, Material.BONE, "&aPlayer Death", "&7Executes when a player dies.", EventType.PLAYER_DEATH);
            addEventActionItem(13, Material.DIAMOND_SWORD, "&aPlayer Kill", "&7Executes for a player when they kill another player.", EventType.PLAYER_KILL);
            addEventActionItem(14, Material.APPLE, "&aPlayer Respawn", "&7Executes when a player respawns", EventType.PLAYER_RESPAWN);
            addNotReadyItem(15, Material.PAPER, "&aGroup Change", "&7Executes when a players group changes.");
            addNotReadyItem(16, Material.IRON_SWORD, "&aPvP State Change", "&7Executes when a player has their PvP state change, such as changing between regions");
            addEventActionItem(19, Material.FISHING_ROD, "&aFish Caught", "&7Executes when a player catches a fish while fishing.", EventType.FISH_CAUGHT);
            addEventActionItem(20, Material.OBSIDIAN, "&aPlayer Enter Portal", "&7Executes when a player uses a nether or end portal.", EventType.PLAYER_ENTER_PORTAL);
            addEventActionItem(21, Material.LAVA_BUCKET, "&aPlayer Damage", "&7Executes when a player takes damage.", EventType.PLAYER_DAMAGE);
            addEventActionItem(22, Material.GOLDEN_SWORD, "&aPlayer Attack", "&7Executes when a player damages another entity.", EventType.PLAYER_ATTACK);
            addEventActionItem(23, Material.GRASS_BLOCK, "&aPlayer Block Break", "&7Executes when a player attempts to break a block in the plot.", EventType.PLAYER_BLOCK_BREAK);
            addEventActionItem(24, Material.COBBLESTONE, "&aPlayer Block Place", "&7Executes when a player attempts to place a block in the plot.", EventType.PLAYER_BLOCK_PLACE);
            addNotReadyItem(25, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, "&aStart Parkour", "&7Executes when a player starts doing parkour.");
            addNotReadyItem(28, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, "&aComplete Parkour", "&7Executes when a player successfully completes the parkour.");
            addEventActionItem(29, Material.DROPPER, "&aPlayer Drop Item", "&7Executes when a player drops an item.", EventType.PLAYER_DROP_ITEM);
            addEventActionItem(30, Material.HOPPER, "&aPlayer Pick Up Item", "&7Executes when a player picks up an item.", EventType.PLAYER_PICKUP_ITEM);
            addEventActionItem(31, Material.BOOK, "&aPlayer Change Held Item", "&7Executes when a player changes their held item.", EventType.PLAYER_CHANGE_HELD_ITEM);
            addEventActionItem(32, Material.HAY_BLOCK, "&aPlayer Toggle Sneak", "&7Executes when a player toggles sneak.", EventType.PLAYER_TOGGLE_SNEAK);
            addEventActionItem(33, Material.FEATHER, "&aPlayer Toggle Flight", "&7Executes when a player toggles flight.", EventType.PLAYER_TOGGLE_FLIGHT);
            addEventActionItem(34, Material.PLAYER_HEAD, "&aPlayer Chat Message", "&7Executes when a player sends a chat message.", EventType.PLAYER_CHAT);

            addItem(53, new ItemBuilder().material(Material.ARROW).name("&aNext Page").build(), () -> {
                page = 2;
                open();
            });
        }

        if (page == 2) {
            addEventActionItem(10, Material.SLIME_BALL, "&aPlayer Jump", "&7Executes when a player jumps.", EventType.PLAYER_JUMP);
            addEventActionItem(11, Material.SHIELD, "&aPlayer Swap To Offhand", "&7Executes when a player swaps to their offhand.", EventType.PLAYER_SWAP_TO_OFFHAND);
            addEventActionItem(11, Material.COOKIE, "&aPlayer Give Cookie", "&7Executes when a player gives the house a cookie.", EventType.GIVE_COOKIE);

            addItem(45, new ItemBuilder().material(Material.ARROW).name("&aPrevious Page").build(), () -> {
                page = 1;
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name("&cGo Back").build(), () -> new SystemsMenu(main, player, house).open());
    }

    private void addEventActionItem(int slot, Material material, String name, String description, EventType eventType) {
        addItem(slot, new ItemBuilder()
                        .material(material)
                        .name(name)
                        .description(description)
                        .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                        .build(),
                () -> new ActionsMenu(main, player, house, eventType).open());
    }

    private void addNotReadyItem(int slot, Material material, String name, String description) {
        addItem(slot, new ItemBuilder()
                        .material(material)
                        .name(name)
                        .description(description)
                        .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                        .build(),
                () -> player.sendMessage(colorize("&cNot ready yet.")));
    }
}