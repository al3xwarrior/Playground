package com.al3x.housing2.Menus;

import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
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
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ANVIL;

public class AddConditionMenu extends Menu {
    private Menu backMenu;
    private Main main;
    private Player player;
    private int page = 1;
    private HousingWorld house;
    private Function function;
    private EventType event;
    private List<Condition> conditions;
    private String search = "";
    
    public AddConditionMenu(Main main, Player player, HousingWorld house, List<Condition> conditions, Menu backMenu) {
        super(player, colorize("&aAdd Condition"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.conditions = conditions;
        this.backMenu = backMenu;
        setupItems();
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(null, 54, "§aAdd Condition (" + page + "/" + getConditions().getPageCount() + ")");
        setupItems();
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void setupItems() {
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        PaginationList<Condition> paginationList = getConditions();
        List<Condition> conditionsList = paginationList.getPage(page);

        if (conditionsList != null && !conditionsList.isEmpty()) {
            for (int i = 0; i < conditionsList.size(); i++) {
                Condition condition = conditionsList.get(i);
                ItemBuilder item = new ItemBuilder();
                condition.createAddDisplayItem(item);
                addItem(slots[i] - 1, item.build(), () -> {
                    conditions.add(condition);
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
            new ActionsMenu(main, player, house, conditions, null, true).open();
        });
    }

    public PaginationList<Condition> getConditions() {
        List<Condition> conditionArray = Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).toList();
        List<Condition> newConditions = new ArrayList<>();
        for (Condition condition : conditionArray) {
            if (condition == null) continue;
            if (function != null) {
                if (function.isGlobal() && condition.requiresPlayer()) continue;
                if (condition.allowedEvents() != null && !condition.allowedEvents().contains(null)) continue;
                newConditions.add(condition);
                continue;
            }

            if (event != null) {
                if (condition.allowedEvents() != null && !condition.allowedEvents().contains(event)) continue;
                newConditions.add(condition);
                continue;
            }

            if (condition.allowedEvents() != null) continue;

            newConditions.add(condition);
        }

        if (search != null) {
            newConditions = newConditions.stream().filter(i -> Color.removeColor(i.getName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(newConditions, 21);
    }
}
