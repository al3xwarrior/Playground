package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.AddConditionMenu;
import com.al3x.housing2.Menus.HousingMenu.EventActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private List<Action> actions;
    private List<Condition> conditions;
    private HousingNPC housingNPC;
    private EventType event;
    private Function function;
    private Menu backMenu;
    private String varName;
    private Runnable update;
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
        this.update = () -> {
            house.setEventActions(event, actions);
        };
        this.backMenu = new EventActionsMenu(main, player, house);
    }

    public ActionsMenu(Main main, Player player, HousingWorld house, Function function, Menu backMenu) {
        super(player, colorize("&7Action: " + function.getName()), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.function = function;
        this.actions = function.getActions();
        this.backMenu = backMenu;
    }

    public ActionsMenu(Main main, Player player, HousingWorld house, List<Action> actions, Menu backMenu, String varName) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.actions = actions;
        this.backMenu = backMenu;
        this.varName = varName;

        if (actions == null) {
            this.actions = house.getEventActions(event);
        }
    }

    public ActionsMenu(Main main, Player player, HousingWorld house, List<Condition> conditions, Menu backMenu, boolean isConditionalMenu) {
        super(player, colorize("&7Edit Conditions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.conditions = conditions;
        this.actions = null;
        this.backMenu = backMenu;
    }

    private void removeAction(Action action) {
        actions.remove(action);
        setupItems();
    }

    public void removeCondition(Condition condition) {
        conditions.remove(condition);
        setupItems();
    }

    @Override
    public void open() {
        if (!house.hasPermission(player, Permissions.EDIT_ACTIONS)) {
            player.sendMessage(colorize("&cYou do not have permission to edit actions in this house!"));
            if (event != null) {
                new EventActionsMenu(main, player, house).open();
            }
            if (housingNPC != null) {
                new NPCMenu(main, player, housingNPC).open();
            }
            if (backMenu != null) {
                backMenu.open();
            }
            return;
        }
        super.open();
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        if (update != null) update.run();

        // Conditions
        if (actions == null) {
            PaginationList<Condition> paginationList = new PaginationList<>(conditions, allowedSlots.length);
            List<Condition> conditions = paginationList.getPage(currentPage);

            if (conditions == null || conditions.isEmpty()) {
                ItemStack noActions = new ItemStack(Material.BEDROCK);
                ItemMeta noActionsMeta = noActions.getItemMeta();
                noActionsMeta.setDisplayName(colorize("&cNo Actions!"));
                noActions.setItemMeta(noActionsMeta);
                addItem(22, noActions, () -> {
                    player.sendMessage(colorize("&eAdd an action using the &aAdd Action &eitem below!"));
                });
            } else {
                for (int i = 0; i < conditions.size(); i++) {
                    Condition condition = conditions.get(i);
                    int slot = allowedSlots[i];
                    ItemBuilder item = new ItemBuilder();
                    item.mClick(ItemBuilder.ActionType.CLONE);
                    condition.createDisplayItem(item);
                    int finalI = i;
                    addItem(slot, item.build(), (e) -> {
                        if (e.isShiftClick()) {
                            //shift actions around
                            shiftCondition(condition, e.isRightClick());
                            return;
                        }

                        if (e.getClick() == ClickType.MIDDLE) {
                            this.conditions.add(finalI, condition.clone());
                            setupItems();
                            return;
                        }

                        if (e.isLeftClick()) {
                            new ActionEditMenu(condition, main, player, house, this).open();
                        } else {
                            removeCondition(condition);
                        }
                    });
                }
            }

            ItemStack addCondition = new ItemStack(Material.PAPER);
            ItemMeta addActionMeta = addCondition.getItemMeta();
            addActionMeta.setDisplayName(colorize("&aAdd Condition"));
            addCondition.setItemMeta(addActionMeta);
            addItem(50, addCondition, () -> {
                AddConditionMenu menu = new AddConditionMenu(main, player, house, this.conditions, this);
                menu.setEvent(event);
                menu.setFunction(function);
                menu.open();
            });
        } else { // Actions
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
                    item.mClick(ItemBuilder.ActionType.CLONE);
                    action.createDisplayItem(item, house);
                    int finalI = i;
                    addItem(slot, item.build(), (e) -> {
                        if (e.isShiftClick()) {
                            //shift actions around
                            shiftAction(action, finalI, e.isRightClick());
                            return;
                        }

                        if (e.getClick() == ClickType.MIDDLE) {
                            this.actions.add(finalI, action.clone());
                            setupItems();
                            return;
                        }

                        if (e.isLeftClick()) {
                            ActionEditMenu menu = new ActionEditMenu(action, main, player, house, this);
                            menu.setEvent(event);
                            menu.setHousingNPC(housingNPC);
                            menu.setUpdate(update);
                            menu.open();
                        } else {
                            removeAction(action);
                        }
                    });
                }
            }

            if (currentPage < paginationList.getPageCount()) {
                ItemStack next = new ItemStack(Material.ARROW);
                ItemMeta nextMeta = next.getItemMeta();
                nextMeta.setDisplayName(colorize("&aNext Page"));
                next.setItemMeta(nextMeta);
                addItem(53, next, () -> {
                    if (currentPage >= paginationList.getPageCount()) return;
                    currentPage++;
                    setupItems();
                });
            }

            ItemStack addAction = new ItemStack(Material.PAPER);
            ItemMeta addActionMeta = addAction.getItemMeta();
            addActionMeta.setDisplayName(colorize("&aAdd Action"));
            addAction.setItemMeta(addActionMeta);
            addItem(50, addAction, () -> {
                AddActionMenu menu = new AddActionMenu(main, player, house, this.actions, this, varName != null);
                menu.setEvent(event);
                menu.setFunction(function);
                menu.open();
            });
        }

        if (currentPage > 1) {
            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = previous.getItemMeta();
            prevMeta.setDisplayName(colorize("&aPrevious Page"));
            previous.setItemMeta(prevMeta);
            addItem(45, previous, () -> {
                if (currentPage <= 1) return;
                currentPage--;
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
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void setBackMenu(Menu backMenu) {
        this.backMenu = backMenu;
    }

    public void setHousingNPC(HousingNPC housingNPC) {
        this.housingNPC = housingNPC;
    }

    public void setUpdate(Runnable update) {
        this.update = update;
    }

    public void shiftAction(Action action, int index, boolean forward) {

        if (actions == null || actions.size() < 2) return;

        actions.remove(index);

        if (forward) {
            actions.add((index == actions.size()) ? 0 : index + 1, action);
        } else {
            actions.add((index == 0) ? actions.size() : index - 1, action);
        }

        setupItems();
    }

    public void shiftCondition(Condition condition, boolean forward) {
        int index = conditions.indexOf(condition);

        if (conditions == null || conditions.size() < 2) return;

        conditions.remove(index);

        if (forward) {
            conditions.add((index == conditions.size()) ? 0 : index + 1, condition);
        } else {
            conditions.add((index == 0) ? conditions.size() : index - 1, condition);
        }

        setupItems();
    }
}
