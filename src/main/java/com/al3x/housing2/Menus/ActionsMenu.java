package com.al3x.housing2.Menus;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.*;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private List<Action> actions;
    private HousingNPC housingNPC;
    private EventType event;
    private Menu backMenu;
    //1 is the new 0
    private int currentPage = 1;
    private final int itemsPerPage = 45;

    // NPC
    public ActionsMenu(Main main, Player player, HousingWorld house, HousingNPC housingNPC) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.housingNPC = housingNPC;
        this.actions = housingNPC.getActions();
    }

    // Events
    public ActionsMenu(Main main, Player player, HousingWorld house, EventType event) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.event = event;
        this.actions = house.getEventActions(event);
    }
    public ActionsMenu(Main main, Player player, HousingWorld house, EventType event, List<Action> actions, Menu backMenu) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.event = event;
        this.actions = actions;
        this.backMenu = backMenu;

        if (actions == null) {
            this.actions = house.getEventActions(event);
        }
    }

    public ActionsMenu(Main main, Player player, HousingWorld house, List<Action> actions, Menu backMenu) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.actions = actions;
        this.backMenu = backMenu;

        if (actions == null) {
            this.actions = house.getEventActions(event);
        }
    }

    private void removeAction(Action action) {
        actions.remove(action);
        if (event != null) {
            house.setEventActions(event, actions);
        }
        setupItems();
    }

    @Override
    public void setupItems() {
        clearItems();

        int[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        PaginationList<Action> paginationList = new PaginationList<>(actions, allowedSlots.length);
        List<Action> actions = paginationList.getPage(currentPage);

        if (actions == null || actions.isEmpty()) {
            ItemStack noActions = new ItemStack(Material.BEDROCK);
            ItemMeta noActionsMeta = noActions.getItemMeta();
            noActionsMeta.setDisplayName(colorize("&cNo Actions!"));
            noActions.setItemMeta(noActionsMeta);
            addItem(22, noActions, () -> {
                player.sendMessage(colorize("&eAdd an action using the &aAdd Action &eitem below!"));
            });
        } else {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(i);
                int slot = allowedSlots[i];
                ItemBuilder item = new ItemBuilder();
                action.createDisplayItem(item);
                addItem(slot, item.build(), () -> {
                    if (housingNPC != null) {
                        new ActionEditMenu(action, main, player, house, housingNPC).open();
                    }
                    if (event != null) {
                        new ActionEditMenu(action, main, player, house, event).open();
                    }
                }, () -> {
                    removeAction(action);
                });
            }
        }

        if (currentPage > 1) {
            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = previous.getItemMeta();
            prevMeta.setDisplayName(colorize("&aPrevious Page"));
            previous.setItemMeta(prevMeta);
            addItem(45, previous, () -> {
                currentPage--;
                setupItems();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(colorize("&aNext Page"));
            next.setItemMeta(nextMeta);
            addItem(53, next, () -> {
                currentPage++;
                setupItems();
            });
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            if (event != null) {
                new EventActionsMenu(main, player, house).open();
            }
            if (housingNPC != null) {
                new NPCMenu(main, player, housingNPC).open();
            }
            if (backMenu != null) {
                backMenu.open();
            }
        });

        ItemStack addAction = new ItemStack(Material.PAPER);
        ItemMeta addActionMeta = addAction.getItemMeta();
        addActionMeta.setDisplayName(colorize("&aAdd Action"));
        addAction.setItemMeta(addActionMeta);
        addItem(50, addAction, () -> {
            new AddActionMenu(main, player, 1, house, event, actions, backMenu).open();
        });
    }

}
