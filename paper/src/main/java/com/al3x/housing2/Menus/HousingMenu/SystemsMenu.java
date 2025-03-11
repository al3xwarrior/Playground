package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.CustomMenus.CustomMenusMenu;
import com.al3x.housing2.Menus.HousingMenu.commands.CommandsMenu;
import com.al3x.housing2.Menus.HousingMenu.layout.LayoutsMenu;
import com.al3x.housing2.Menus.HousingMenu.regions.RegionsMenu;
import com.al3x.housing2.Menus.HousingMenu.teams.TeamsMenu;
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
    public void initItems() {
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        int slot = 0;
        if (house.hasPermission(player, Permissions.EDIT_REGIONS)) {
            ItemStack regions = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta regionsMeta = regions.getItemMeta();
            regionsMeta.setDisplayName(colorize("&aRegions"));
            regions.setItemMeta(regionsMeta);
            addItem(slots[slot], regions, () -> {
                new RegionsMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_EVENTS)) {
            ItemStack eventActions = new ItemStack(Material.COBWEB);
            ItemMeta eventActionsMeta = eventActions.getItemMeta();
            eventActionsMeta.setDisplayName(colorize("&aEvent Actions"));
            eventActions.setItemMeta(eventActionsMeta);
            addItem(slots[slot], eventActions, () -> {
                new EventActionsMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_SCOREBOARD)) {
            ItemStack scoreboardEditor = new ItemStack(Material.MAP);
            ItemMeta scoreboardEditorMeta = scoreboardEditor.getItemMeta();
            scoreboardEditorMeta.setDisplayName(colorize("&aScoreboard Editor"));
            scoreboardEditor.setItemMeta(scoreboardEditorMeta);
            addItem(slots[slot], scoreboardEditor, () -> {
                new ScoreboardMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_COMMANDS)) {
            ItemStack commands = new ItemStack(Material.COMMAND_BLOCK);
            ItemMeta commandsMeta = commands.getItemMeta();
            commandsMeta.setDisplayName(colorize("&aCommands"));
            commands.setItemMeta(commandsMeta);
            addItem(slots[slot], commands, () -> {
                new CommandsMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_FUNCTIONS)) {
            ItemStack functions = new ItemStack(Material.ACTIVATOR_RAIL);
            ItemMeta functionsMeta = functions.getItemMeta();
            functionsMeta.setDisplayName(colorize("&aFunctions"));
            functions.setItemMeta(functionsMeta);
            addItem(slots[slot], functions, () -> {
                new FunctionsMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_INVENTORY_LAYOUTS)) {
            ItemStack inventoryLayouts = new ItemStack(Material.IRON_AXE);
            ItemMeta inventoryLayoutsMeta = inventoryLayouts.getItemMeta();
            inventoryLayoutsMeta.setDisplayName(colorize("&aInventory Layouts"));
            inventoryLayouts.setItemMeta(inventoryLayoutsMeta);
            addItem(slots[slot], inventoryLayouts, () -> {
                new LayoutsMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_TEAMS)) {
            ItemStack teams = new ItemStack(Material.OAK_SIGN);
            ItemMeta teamsMeta = teams.getItemMeta();
            teamsMeta.setDisplayName(colorize("&aTeams"));
            teams.setItemMeta(teamsMeta);
            addItem(slots[slot], teams, () -> {
                new TeamsMenu(player, main, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.EDIT_CUSTOM_MENUS)) {
            ItemStack customMenus = new ItemStack(Material.CHEST);
            ItemMeta customMenusMeta = customMenus.getItemMeta();

            customMenusMeta.setDisplayName(colorize("&aCustom Menus"));
            customMenus.setItemMeta(customMenusMeta);
            addItem(slots[slot], customMenus, () -> {
                new CustomMenusMenu(main, player, house).open();
            });
            slot++;
        }

        if (house.hasPermission(player, Permissions.ITEM_NPCS)) {
            ItemStack npcs = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta npcsMeta = npcs.getItemMeta();

            npcsMeta.setDisplayName(colorize("&aNPCs"));
            npcs.setItemMeta(npcsMeta);
            addItem(slots[slot], npcs, () -> {
                new NPCsMenu(main, player, house).open();
            });
            slot++;
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new HousingMenu(main, player, house).open();
        });
    }
}
