package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.ClipboardManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionClipboardMenu extends Menu {
    Main main;
    HousingWorld house;
    ClipboardManager clipboardManager;
    Action action;
    ActionEditMenu previousMenu;
    int currentPage = 1;
    public ActionClipboardMenu(Player player, Main main, HousingWorld house, Action action, ActionEditMenu previousMenu) {
        super(player, "&7Action Clipboard", 9 * 6);
        this.main = main;
        this.house = house;
        this.clipboardManager = main.getClipboardManager();
        this.action = action;
        this.previousMenu = previousMenu;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        List<Action> newList = clipboardManager.fromClipboard(player.getUniqueId().toString());
        newList = newList.stream().filter(action -> action.getClass().equals(this.action.getClass())).toList();
        PaginationList<Action> actions = new PaginationList<>(newList, 21);
        List<Action> page = actions.getPage(currentPage);
        if (page.isEmpty()) {
            ItemStack noActions = new ItemStack(Material.BEDROCK);
            ItemMeta noActionsMeta = noActions.getItemMeta();
            noActionsMeta.setDisplayName(colorize("&cNo Actions!"));
            noActions.setItemMeta(noActionsMeta);
            addItem(22, noActions, () -> {
                player.sendMessage(colorize("&eAdd an action using the &aAdd Action &eitem below!"));
            });
        } else {
            for (int i = 0; i < page.size(); i++) {
                Action action = page.get(i);
                ItemBuilder itemBuilder = new ItemBuilder();
                action.createDisplayItem(itemBuilder, house);
                itemBuilder.lClick(ItemBuilder.ActionType.CLONE);
                itemBuilder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                itemBuilder.shiftClick(false);
                addItem(allowedSlots[i], itemBuilder.build(), (e) -> {
                    if (e.isRightClick()) {
                        clipboardManager.removeAction(player.getUniqueId().toString(), action);
                        setupItems();
                    } else if (e.isLeftClick()) {
                        this.action.fromData(action.data(), action.getClass());
                        previousMenu.setAction(this.action);
                        if (previousMenu.getUpdate() != null) {
                            previousMenu.getUpdate().run();
                        }
                        previousMenu.open();
                    }
                });
            }

            if (currentPage < actions.getPageCount()) {
                ItemStack next = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = next.getItemMeta();
                nextMeta.setDisplayName(colorize("&aNext Page"));
                next.setItemMeta(nextMeta);
                addItem(53, next, () -> {
                    if (currentPage >= actions.getPageCount()) return;
                    currentPage++;
                    setupItems();
                });
            }

            if (currentPage > 1) {
                ItemStack previous = new ItemStack(Material.ARROW);
                ItemMeta previousMeta = previous.getItemMeta();
                previousMeta.setDisplayName(colorize("&aPrevious Page"));
                previous.setItemMeta(previousMeta);
                addItem(52, previous, () -> {
                    if (currentPage <= 1) return;
                    currentPage--;
                    setupItems();
                });
            }
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            previousMenu.open();
        });
    }
}
