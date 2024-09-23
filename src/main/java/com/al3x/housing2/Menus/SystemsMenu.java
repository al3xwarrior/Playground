package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class SystemsMenu extends Menu {

    private Player player;
    private HousingWorld house;

    public SystemsMenu(Player player, HousingWorld house) {
        super(player, colorize("&cSystems"), 45);
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    protected void setupItems() {
        ItemStack regions = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta regionsMeta = regions.getItemMeta();
        regionsMeta.setDisplayName(colorize("&aRegions"));
        regions.setItemMeta(regionsMeta);
        addItem(10, regions, () -> {
            player.sendMessage("Opening Regions Menu...");
        });

        ItemStack eventActions = new ItemStack(Material.COBWEB);
        ItemMeta eventActionsMeta = eventActions.getItemMeta();
        eventActionsMeta.setDisplayName(colorize("&aEvent Actions"));
        eventActions.setItemMeta(eventActionsMeta);
        addItem(11, eventActions, () -> {
            new EventActionsMenu(player, house).open();
        });

        ItemStack scoreboardEditor = new ItemStack(Material.MAP);
        ItemMeta scoreboardEditorMeta = scoreboardEditor.getItemMeta();
        scoreboardEditorMeta.setDisplayName(colorize("&aScoreboard Editor"));
        scoreboardEditor.setItemMeta(scoreboardEditorMeta);
        addItem(12, scoreboardEditor, () -> {
            player.sendMessage("Opening Scoreboard Editor...");
        });

        ItemStack commands = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta commandsMeta = commands.getItemMeta();
        commandsMeta.setDisplayName(colorize("&aCommands"));
        commands.setItemMeta(commandsMeta);
        addItem(13, commands, () -> {
            player.sendMessage("Opening Commands Menu...");
        });

        ItemStack functions = new ItemStack(Material.ACTIVATOR_RAIL);
        ItemMeta functionsMeta = functions.getItemMeta();
        functionsMeta.setDisplayName(colorize("&aFunctions"));
        functions.setItemMeta(functionsMeta);
        addItem(14, functions, () -> {
            player.sendMessage("Opening Functions Menu...");
        });

        ItemStack inventoryLayouts = new ItemStack(Material.IRON_AXE);
        ItemMeta inventoryLayoutsMeta = inventoryLayouts.getItemMeta();
        inventoryLayoutsMeta.setDisplayName(colorize("&aInventory Layouts"));
        inventoryLayouts.setItemMeta(inventoryLayoutsMeta);
        addItem(15, inventoryLayouts, () -> {
            player.sendMessage("Opening Inventory Layouts...");
        });

        ItemStack teams = new ItemStack(Material.OAK_SIGN);
        ItemMeta teamsMeta = teams.getItemMeta();
        teamsMeta.setDisplayName(colorize("&aTeams"));
        teams.setItemMeta(teamsMeta);
        addItem(16, teams, () -> {
            player.sendMessage("Opening Teams Menu...");
        });

        ItemStack customMenus = new ItemStack(Material.CHEST);
        ItemMeta customMenusMeta = customMenus.getItemMeta();
        customMenusMeta.setDisplayName(colorize("&aCustom Menus"));
        customMenus.setItemMeta(customMenusMeta);
        addItem(19, customMenus, () -> {
            player.sendMessage("Opening Custom Menus...");
        });
    }
}
