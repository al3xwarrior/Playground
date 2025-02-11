package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.FunctionSettingsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class FunctionsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public FunctionsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Functions"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Function> paginationList = new PaginationList<>(house.getFunctions(), slots.length);
        List<Function> functions = paginationList.getPage(currentPage);
        if (functions != null) {
            for (int i = 0; i < functions.size(); i++) {
                Function function = functions.get(i);
                ItemBuilder item = ItemBuilder.create(function.getMaterial());
                item.name(colorize("&a" + function.getName()));
                item.description(function.getDescription());
                item.lClick(ItemBuilder.ActionType.EDIT_ACTIONS);
                item.rClick(ItemBuilder.ActionType.EDIT_FUNCTION);
                addItem(slots[i], item.build(), (e) -> {
                    if (e.getClick().isLeftClick()) {
                        //Remove invalid actions from global functions
                        if (function.isGlobal()) {
                            for (Action action: function.getActions()) {
                                if (action.requiresPlayer()) {
                                    function.getActions().remove(action);
                                }
                            }
                        }
                        new ActionsMenu(main, player, house, function, this).open();
                    } else if (e.getClick().isRightClick()) {
                        new FunctionSettingsMenu(main, player, house, function).open();
                    }
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build(), () -> {
            });
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), () -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), () -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new SystemsMenu(main, player, house).open();
        });

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate Function")).build(), () -> {
            player.sendMessage(colorize("&eEnter the name of the function:"));
            openChat(main, (s) -> {
                Bukkit.getScheduler().runTask(main, () -> {
                    Function function = house.createFunction(s);
                    if (function == null) {
                        player.sendMessage(colorize("&cA function with that name already exists!"));
                        return;
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);

                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        new ActionsMenu(main, player, house, function, this).open();
                    }, 1L);
                });
            });
        });
    }
}
