package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class FunctionSettingsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Function function;

    public FunctionSettingsMenu(Main main, Player player, HousingWorld house, Function function) {
        super(player, colorize("&7Edit: " + function.getName()), 9 * 4);
        this.main = main;
        this.player = player;
        this.house = house;
        this.function = function;
    }

    @Override
    public void setupItems() {

        //Rename Function
        addItem(11, ItemBuilder.create(Material.ANVIL)
                .name(colorize("&aRename Function"))
                .description("Change the name of this function.")
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), (e) -> {
            openChat(main, (message) -> {
                function.setName(message);
            });
        });

        //Edit Description
        addItem(13, ItemBuilder.create(Material.BOOK)
                .name(colorize("&aEdit Description"))
                .description("Edit the description of this function.\n\n" + function.getDescription())
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), (e) -> {
            openChat(main, (message) -> {
                function.setDescription(message);
            });
        });

        //Edit Icon
        addItem(15, ItemBuilder.create(function.getMaterial())
                .name(colorize("&aEdit Icon"))
                .description("Change the icon of this function.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            EnumMenu<Material> enumMenu = new EnumMenu<>(main, "Select Icon", Material.values(), Material.MAP, player, house, this, (material) -> {
                function.setMaterial(material);
                open();
            });
            enumMenu.open();
        });

        //Delete Function
        addItem(30, ItemBuilder.create(Material.TNT)
                .name(colorize("&aDelete Function"))
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), (e) -> {
            house.getFunctions().remove(function);
            new FunctionsMenu(main, player, house).open();
        });

        //Back
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Functions")
                .build(), (e) -> {
            new FunctionsMenu(main, player, house).open();
        });

        //Automatic Executions
        addItem(32, ItemBuilder.create(Material.COMPARATOR)
                .name(colorize("&aAutomatic Executions"))
                .description("Functions can be enabled to automatically executed for all players in houses every X amount of ticks. TIP: 1 second is 20 ticks")
                .info("&7Current", (function.getTicks() == null ? "&cDisabled" : "&a" + function.getTicks() + " ticks"))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), (e) -> {
            openChat(main, (message) -> {
                try {
                    function.setTicks(Double.parseDouble(message));
                } catch (NumberFormatException ex) {
                    function.setTicks(null);
                    player.sendMessage(colorize("&cInvalid number!"));
                }
            });
        });
    }
}
