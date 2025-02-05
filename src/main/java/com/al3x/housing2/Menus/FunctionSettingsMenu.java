package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.FunctionsMenu;
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
        addItem(10, ItemBuilder.create(Material.ANVIL)
                .name(colorize("&aRename Function"))
                .description("Change the name of this function.")
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), () -> {
            openChat(main, function.getName(), (message) -> {
                function.setName(message);
            });
        });

        //Edit Description
        addItem(12, ItemBuilder.create(Material.BOOK)
                .name(colorize("&aEdit Description"))
                .description("Edit the description of this function.\n\n" + function.getDescription())
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), () -> {
            openChat(main, function.getDescription(), (message) -> {
                function.setDescription(message);
            });
        });

        //Edit Icon
        addItem(14, ItemBuilder.create(function.getMaterial())
                .name(colorize("&aEdit Icon"))
                .description("Change the icon of this function.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            EnumMenu<Material> enumMenu = new EnumMenu<>(main, "Select Icon", Material.values(), Material.MAP, player, house, this, (material) -> {
                function.setMaterial(material);
                open();
            });
            enumMenu.open();
        });

        //Toggle Global
        addItem(16, ItemBuilder.create(Material.PLAYER_HEAD)
                .skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1")
                .name(colorize("&aToggle Global"))
                .description("If enabled, this function will not run for all players in the house.\n\n&7Only really matters for automatic executions.")
                .info("&7Current", (function.isGlobal() ? "&aEnabled" : "&cDisabled"))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            function.setGlobal(!function.isGlobal());
            setupItems();
        });

        //Delete Function
        addItem(30, ItemBuilder.create(Material.TNT)
                .name(colorize("&aDelete Function"))
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), () -> {
            function.setLoaded(false);
            house.getFunctions().remove(function);
            new FunctionsMenu(main, player, house).open();
        });

        //Back
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Functions")
                .build(), () -> {
            new FunctionsMenu(main, player, house).open();
        });

        //Automatic Executions
        addItem(32, ItemBuilder.create(Material.COMPARATOR)
                .name(colorize("&aAutomatic Executions"))
                .description("Functions can be enabled to automatically executed for all players in houses every X amount of ticks. TIP: 1 second is 20 ticks\n\n&8Rip Poison loops :(")
                .info("&7Current", (function.getTicks() == null ? "&cDisabled" : "&a" + function.getTicks() + " ticks"))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the amount of ticks you want this function to run every (1 second is 20 ticks): "));
            openChat(main, (function.getTicks() != null ? function.getTicks().toString() : ""), (message) -> {
                try {
                    int ticks = Integer.parseInt(message);
                    if (ticks <= 0) {
                        function.setTicks(null);
                        player.sendMessage(colorize("&cDisabled automatic execution."));
                        return;
                    }
                    function.setTicks(ticks);
                } catch (NumberFormatException ex) {
                    function.setTicks(null);
                    player.sendMessage(colorize("&cInvalid number!"));
                }
            });
        });
    }
}
