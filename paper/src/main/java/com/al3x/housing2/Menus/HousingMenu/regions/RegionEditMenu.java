package com.al3x.housing2.Menus.HousingMenu.regions;

import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.HousingMenu.commands.CommandArgumentsEditMenu;
import com.al3x.housing2.Menus.HousingMenu.commands.CommandsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class RegionEditMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Region region;

    public RegionEditMenu(Main main, Player player, HousingWorld house, Region region) {
        super(player, colorize("&7Edit: " + region.getName()), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.region = region;
    }

    @Override
    public void setupItems() {
        //Rename Region
        addItem(12, ItemBuilder.create(Material.NAME_TAG)
                .name(colorize("&aRename Region"))
                .description("Change the name of this region.")
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), () -> {
            player.sendMessage("§eEnter the new name for this region: ");
            openChat(main, region.getName(), (message) -> {
                region.setName(message);
            });
        });

        //Teleport to Region
        addItem(14, ItemBuilder.create(Material.ENDER_PEARL)
                .name(colorize("&aTeleport to Region"))
                .description("Teleport to the location of this region.")
                .lClick(ItemBuilder.ActionType.TELEPORT)
                .rClick(ItemBuilder.ActionType.SELECT_REGION)
                .build(), () -> {
            player.teleport(region.getFirst());
        }, () -> {
            main.getProtoolsManager().setPositions(player, region.getFirst(), region.getSecond());
            player.sendMessage("§aSelected region: §e" + region.getName());
        });

        //PvP Settings
        addItem(29, ItemBuilder.create(Material.IRON_SWORD)
                .name(colorize("&aPvP Settings"))
                .description("Toggle stuff, tbd.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage("&cComing one day...");
//            new PvPSettingsMenu(main, player, house, region).open();
        });

        //Entry Actions
        addItem(31, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aEntry Actions"))
                .description("Add actions to be executed when entering this region.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            new ActionsMenu(main, player, house, region.getEnterActions(), this, "RegionEntry").open();
        });

        //Exit Actions
        addItem(33, ItemBuilder.create(Material.PAPER)
                .name(colorize("&aExit Actions"))
                .description("Add actions to be executed when exiting this region.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            new ActionsMenu(main, player, house, region.getExitActions(), this, "RegionExit").open();
        });


        //Delete Region
        addItem(50, ItemBuilder.create(Material.TNT)
                .name(colorize("&aDelete Region"))
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), () -> {
            region.setLoaded(false);
            house.getRegions().remove(region);
            new RegionsMenu(main, player, house).open();
        });

        //Back
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Commands")
                .build(), () -> {
            new RegionsMenu(main, player, house).open();
        });
    }
}
