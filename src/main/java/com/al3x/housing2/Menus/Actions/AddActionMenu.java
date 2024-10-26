package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ANVIL;

public class AddActionMenu extends Menu {
    private Menu backMenu;
    private Main main;
    private Player player;
    private int page = 1;
    private HousingWorld house;
    private Function function;
    private EventType event;
    private List<Action> actions;
    private String varName;
    private String search = "";

    public AddActionMenu(Main main, Player player, HousingWorld house, Function function, Menu backMenu) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.house = house;

        this.function = function;
        this.actions = function.getActions();
        this.backMenu = backMenu;

        setupItems();
    }

    public AddActionMenu(Main main, Player player, HousingWorld house, EventType event, Menu backMenu) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.house = house;

        this.event = event;
        this.actions = house.getEventActions(event);
        this.backMenu = backMenu;

        setupItems();
    }

    //Will be used for random actions and conditions
    public AddActionMenu(Main main, Player player, HousingWorld house, List<Action> actions, Menu backMenu, String varName) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.actions = actions;
        this.backMenu = backMenu;
        this.varName = varName;
        setupItems();
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(null, 54, "§aAdd Action (" + page + "/" + getActions().getPageCount() + ")");
        setupItems();
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void setupItems() {
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        PaginationList<Action> paginationList = getActions();
        List<Action> actionsList = paginationList.getPage(page);

        if (actionsList != null && !actionsList.isEmpty()) {
            for (int i = 0; i < actionsList.size(); i++) {
                Action action = actionsList.get(i);
                ItemBuilder item = new ItemBuilder();
                action.createAddDisplayItem(item);
                addItem(slots[i] - 1, item.build(), () -> {
                    actions.add(action);
                    if (backMenu != null) {
                        backMenu.open();
                        return;
                    }
                });
            }
        }

        if (paginationList.getPageCount() > 1) {
            ItemBuilder forwardArrow = new ItemBuilder();
            forwardArrow.material(Material.ARROW);
            forwardArrow.name("&aNext Page");
            forwardArrow.description("&ePage " + (page + 1));
            addItem(53, forwardArrow.build(), () -> {
                if (page + 1 > paginationList.getPageCount()) return;
                page++;
                open();
            });
        }

        if (page > 1) {
            ItemBuilder backArrow = new ItemBuilder();
            backArrow.material(Material.ARROW);
            backArrow.name("&aPrevious Page");
            backArrow.description("&ePage " + (page - 1));
            addItem(45, backArrow.build(), () -> {
                if (page - 1 < 1) return;
                page--;
                open();
            });
        }

        // Search
        addItem(48, new ItemBuilder().material(ANVIL).name("&eSearch").punctuation(false)
                .description("&7Search for an option.\n\n&eCurrent Value:\n&7" + search)
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .build(), (e) -> {
            if (e.getClick().isRightClick()) {
                search = "";
                page = 1;
                Bukkit.getScheduler().runTaskLater(main, this::open, 1L);
                return;
            }
            player.sendMessage(colorize("&ePlease enter the search term:"));
            openChat(main, search, (search) -> {
                this.search = search;
                page = 1;
                Bukkit.getScheduler().runTaskLater(main, this::open, 1L);
            });
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            new ActionsMenu(main, player, house, actions, null, null).open();
        });
    }

    public PaginationList<Action> getActions() {
        List<Action> actionArray = Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter(Objects::nonNull).toList();
        List<Action> newActions = new ArrayList<>();
        for (Action action : actionArray) {
            if (action == null) continue;
            if (function != null) {
                if (function.isGlobal() && action.requiresPlayer()) continue;
                if (action.allowedEvents() != null && !action.allowedEvents().contains(null)) continue;
                newActions.add(action);
                continue;
            }

            if (event != null) {
                if (action.allowedEvents() != null && !action.allowedEvents().contains(event)) continue;
                newActions.add(action);
                continue;
            }

            if (action.allowedEvents() != null) continue;

            newActions.add(action);
        }

        if (search != null) {
            newActions = newActions.stream().filter(i -> Color.removeColor(i.getName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(newActions, 21);
    }
}
