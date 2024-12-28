package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.CustomMenus.CustomMenusMenu;
import com.al3x.housing2.Menus.HousingMenu.commands.CommandsMenu;
import com.al3x.housing2.Menus.HousingMenu.layout.LayoutsMenu;
import com.al3x.housing2.Menus.HousingMenu.regions.RegionsMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class SystemsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public SystemsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Systems"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack regions = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta regionsMeta = regions.getItemMeta();
        regionsMeta.setDisplayName(colorize("&aRegions"));
        regions.setItemMeta(regionsMeta);
        addItem(10, regions, () -> {
            new RegionsMenu(main, player, house).open();
        });

        ItemStack eventActions = new ItemStack(Material.COBWEB);
        ItemMeta eventActionsMeta = eventActions.getItemMeta();
        eventActionsMeta.setDisplayName(colorize("&aEvent Actions"));
        eventActions.setItemMeta(eventActionsMeta);
        addItem(11, eventActions, () -> {
            new EventActionsMenu(main, player, house).open();
        });

        ItemStack scoreboardEditor = new ItemStack(Material.MAP);
        ItemMeta scoreboardEditorMeta = scoreboardEditor.getItemMeta();
        scoreboardEditorMeta.setDisplayName(colorize("&aScoreboard Editor"));
        scoreboardEditor.setItemMeta(scoreboardEditorMeta);
        addItem(12, scoreboardEditor, () -> {
            new ScoreboardMenu(main, player, house).open();
        });

        ItemStack commands = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta commandsMeta = commands.getItemMeta();
        commandsMeta.setDisplayName(colorize("&aCommands"));
        commands.setItemMeta(commandsMeta);
        addItem(13, commands, () -> {
            new CommandsMenu(main, player, house).open();
        });

        ItemStack functions = new ItemStack(Material.ACTIVATOR_RAIL);
        ItemMeta functionsMeta = functions.getItemMeta();
        functionsMeta.setDisplayName(colorize("&aFunctions"));
        functions.setItemMeta(functionsMeta);
        addItem(14, functions, () -> {
            new FunctionsMenu(main, player, house).open();
        });

        ItemStack inventoryLayouts = new ItemStack(Material.IRON_AXE);
        ItemMeta inventoryLayoutsMeta = inventoryLayouts.getItemMeta();
        inventoryLayoutsMeta.setDisplayName(colorize("&aInventory Layouts"));
        inventoryLayouts.setItemMeta(inventoryLayoutsMeta);
        addItem(15, inventoryLayouts, () -> {
            new LayoutsMenu(main, player, house).open();
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
            new CustomMenusMenu(main, player, house).open();
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new OwnerHousingMenu(main, player, house).open();
        });
    }
}
