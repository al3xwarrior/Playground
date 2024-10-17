package com.al3x.housing2.Menus;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.RandomActionMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;

public class AddActionMenu extends Menu {
    private Menu backMenu;
    private Main main;
    private Player player;
    private int page;
    private HousingWorld house;
    private EventType event;
    private List<Action> actions;

    public AddActionMenu(Main main, Player player, int page, HousingWorld house, EventType event) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.page = page;
        this.house = house;
        this.event = event;
        this.actions = (house.getEventActions(event) != null) ? house.getEventActions(event) : new ArrayList<>();
        setupItems();
    }

    //Will be used for random actions and conditions
    public AddActionMenu(Main main, Player player, int page, HousingWorld house, EventType event, List<Action> actions, Menu backMenu) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.page = page;
        this.house = house;
        this.event = event;
        this.actions = actions;

        if (actions == null) {
            this.actions = house.getEventActions(event);
        }

        this.backMenu = backMenu;
        setupItems();
    }

    @Override
    public void setupItems() {
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};
        List<Action> actionArray = Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter((action) ->{
            if (action == null) {
                return false;
            }
            return action.allowedEvents() == null || action.allowedEvents().contains(event);
        }).collect(Collectors.toList());
        PaginationList<Action> paginationList = new PaginationList<>(actionArray, 21);
        List<Action> actionsList = paginationList.getPage(page);

        for (int i = 0; i < actionsList.size(); i++) {
            Action action = actionsList.get(i);
            ItemBuilder item = new ItemBuilder();
            action.createAddDisplayItem(item);
            addItem(slots[i] - 1, item.build(), () -> {
                actions.add(action);
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });
        }

        if (paginationList.getPageCount() > 1) {
            ItemStack forwardArrow = new ItemStack(Material.ARROW);
            ItemMeta forwardArrowMeta = forwardArrow.getItemMeta();
            forwardArrowMeta.setDisplayName(colorize("&aNext Page"));
            forwardArrow.setItemMeta(forwardArrowMeta);
            addItem(53, forwardArrow, () -> {
                new AddActionMenu(main, player, page + 1, house, event, actions, backMenu).open();
            });
        }

        if (page > 1) {
            ItemStack backArrow = new ItemStack(Material.ARROW);
            ItemMeta backArrowMeta = backArrow.getItemMeta();
            backArrowMeta.setDisplayName(colorize("&aLast Page"));
            backArrow.setItemMeta(backArrowMeta);
            addItem(45, backArrow, () -> {
                new AddActionMenu(main, player, page - 1, house, event, actions, backMenu).open();
            });
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            new ActionsMenu(main, player, house, event).open();
        });
    }
}
