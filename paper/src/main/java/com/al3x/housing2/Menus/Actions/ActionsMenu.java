package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.RunAsNPCAction;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.EventActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.al3x.housing2.Network.PlayerNetwork;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.network.payload.clientbound.ClientboundExport;
import com.al3x.housing2.network.payload.clientbound.ClientboundImport;
import com.al3x.housing2.network.payload.clientbound.ClientboundWebsocket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionsMenu extends Menu {
    private static HashMap<Duple<UUID, String>, ActionsMenu> menus = new HashMap<>();

    private Main main;
    private Player player;
    private HousingWorld house;
    @Getter
    @Setter
    private List<Action> actions;
    @Setter
    private List<Condition> conditions;
    @Setter
    private HousingNPC housingNPC;
    @Setter
    private EventType event;
    @Setter
    private Function function;
    @Getter
    @Setter
    private Menu backMenu;
    @Setter
    private Runnable update;
    @Setter
    @Getter
    private int nestedLevel = 0;
    //If the player accidentally deletes an action, we can cache it here
    private Action cachedAction;
    private Condition cachedCondition;
    private String varName = null;
    private String search = "";
    //1 is the new 0
    @Setter
    @Getter
    private List<Action> parentActions = new ArrayList<>();
    private int currentPage = 1;

    // NPC
    public ActionsMenu(Main main, Player player, HousingWorld house, HousingNPC housingNPC) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.housingNPC = housingNPC;
        this.actions = housingNPC.getActions();
        nestedLevel = 1;
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
        this.nestedLevel = 1;
    }

    public ActionsMenu(Main main, Player player, HousingWorld house, Function function, Menu backMenu) {
        super(player, colorize("&7Action: " + function.getName()), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.function = function;
        this.actions = function.getActions();
        this.backMenu = backMenu;
        this.nestedLevel = 1;
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
        cachedAction = action;
        setupItems();
    }

    public void removeCondition(Condition condition) {
        conditions.remove(condition);
        cachedCondition = condition;
        setupItems();
    }

    public void addParentAction(Action action) {
        parentActions.add(action);
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

        String context = "";
        if (event != null) {
            context = event.name();
        } else if (function != null) {
            context = function.getName();
        } else if (housingNPC != null) {
            context = housingNPC.getName();
        } else if (backMenu != null) {
            context = backMenu.toString(); //class memory address, however, this may not be the best way to identify a menu
        }
        if (this.varName != null) {
            context = context + this.varName;
        }

        for (Duple<UUID, String> key : menus.keySet()) {
            if (key.getFirst().equals(player.getUniqueId()) && key.getSecond().equals(context)) {
                if (menus.get(key) == this) {
                    super.open();
                } else {
                    this.cachedAction = menus.get(key).cachedAction;
                    this.cachedCondition = menus.get(key).cachedCondition;
                    super.open();
                }
                return;
            }
        }

        menus.put(new Duple<>(player.getUniqueId(), context), this);

        super.open();
    }

    @Override
    public void initItems() {
        clearItems();
        int[] allowedSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        if (update != null) update.run();

        // Conditions
        if (actions == null) {
            PaginationList<Condition> paginationList = new PaginationList<>(conditions, 21);
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
                    try {
                        Condition condition = conditions.get(i);
                        int slot = allowedSlots[i];
                        ItemBuilder item = new ItemBuilder();
                        item.mClick(ItemBuilder.ActionType.CLONE);
                        condition.createDisplayItem(item);
                        replacePlayerWithNPC(item);
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

                            if (e.isLeftClick() && condition.editorMenu(house, player, backMenu) != null) {
                                ActionEditMenu menu = new ActionEditMenu(condition, main, player, house, this);
                                menu.setEvent(event);
                                menu.setHousingNPC(housingNPC);
                                menu.setParentActions(parentActions);
                                menu.open();
                            } else if (e.isRightClick()) {
                                removeCondition(condition);
                            }
                        });
                    } catch (Exception e) {
                        int finalI1 = i;
                        addItem(allowedSlots[i], ItemBuilder.create(Material.BARRIER).name("&cError!").description("An error occurred whilst adding this condition!\n\n&cPlease report this to your nearest admin :).").rClick(ItemBuilder.ActionType.REMOVE_YELLOW).build(), () -> {

                        }, () -> {
                            removeCondition(conditions.get(finalI1));
                        });
                    }
                }
            }

            if (cachedCondition != null) {
                addItem(52,
                        ItemBuilder.create(Material.CAULDRON)
                                .name("&aRestore Condition")
                                .description("Restore the condition you last removed along with its settings.\n\nAction: " + cachedCondition.getName())
                                .lClick(ItemBuilder.ActionType.RESTORE)
                                .build(),
                        () -> {
                            this.conditions.add(cachedCondition);
                            cachedCondition = null;
                            setupItems();
                        }
                );
            }

            ItemStack addCondition = new ItemStack(Material.PAPER);
            ItemMeta addActionMeta = addCondition.getItemMeta();
            addActionMeta.setDisplayName(colorize("&aAdd Condition"));
            addCondition.setItemMeta(addActionMeta);
            addItem(50, addCondition, () -> {
                AddConditionMenu menu = new AddConditionMenu(main, player, house, this.conditions, this);
                menu.setEvent(event);
                menu.setNPC(housingNPC);
                menu.setFunction(function);
                menu.setParentActions(parentActions);
                menu.open();
            });
        } else { // Actions

            List<Action> as = new ArrayList<>();
            for (Action action : this.actions) {
                if (search.isEmpty() || action.getName().toLowerCase().contains(search.toLowerCase())) {
                    as.add(action);
                }
            }

            PaginationList<Action> paginationList = new PaginationList<>(as, allowedSlots.length);
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
                    try {
                        Action action = actions.get(i);
                        int slot = allowedSlots[i];
                        ItemBuilder item = new ItemBuilder();
                        item.mClick(ItemBuilder.ActionType.CLONE);
                        if (!Objects.equals(action.getComment(), "") && action.getComment() != null) {
                            item.description(action.getComment()).punctuation(false);
                        }
                        action.createDisplayItem();
                        replacePlayerWithNPC(item);
                        int finalI = i;
                        addItem(slot, item.build(), (e) -> {
                            if (e.isShiftClick()) {
                                //shift actions around
                                shiftAction(action, finalI + (currentPage - 1) * 21, e.isRightClick());
                                return;
                            }

                            if (e.getClick() == ClickType.MIDDLE) {
                                if (ActionEditMenu.isLimitReached(actions, action)) {
                                    player.sendMessage(colorize("&cYou have reached the limit for this action!"));
                                    return;
                                }
                                this.actions.add(finalI, action.clone());
                                setupItems();
                                return;
                            }


                            if (e.isLeftClick() && action.editorMenu(house, backMenu, player) != null) {
                                ActionEditMenu menu = new ActionEditMenu(action, main, player, house, this);
                                menu.setEvent(event);
                                menu.setHousingNPC(housingNPC);
                                menu.setUpdate(update);
                                menu.setParentActions(parentActions);
                                menu.open();
                            } else {
                                removeAction(action);
                            }
                        });
                    } catch (Exception e) {
                        int finalI1 = i;
                        addItem(allowedSlots[i], ItemBuilder.create(Material.BARRIER).name("&cError!").description("An error occurred whilst adding this action!\n\n&cPlease report this to your nearest admin :).").rClick(ItemBuilder.ActionType.REMOVE_YELLOW).build(), () -> {

                        }, () -> {
                            removeAction(actions.get(finalI1));
                        });
                    }
                }

                if (cachedAction != null) {
                    addItem(52,
                            ItemBuilder.create(Material.CAULDRON)
                                    .name("&aRestore Action")
                                    .description("Restore the action you last removed along with its settings.\n\nAction: " + cachedAction.getName())
                                    .lClick(ItemBuilder.ActionType.RESTORE)
                                    .build(),
                            () -> {
                                this.actions.add(cachedAction);
                                cachedAction = null;
                                setupItems();
                            }
                    );
                }

                PlayerNetwork network = PlayerNetwork.getNetwork(player);
                if (network.isUsingMod()) {
                    addItem(47, ItemBuilder.create(Material.PURPLE_DYE)
                                    .name("&aExport Actions")
                                    .description("Because you have the mod installed, you can export the actions to a file.")
                                    .lClick(ItemBuilder.ActionType.EXPORT_YELLOW).build(),
                            () -> {
                                String subActions = "";
                                for (Action action : actions) {
                                    if (action instanceof HTSLImpl htsl) {
                                        subActions += htsl.export() + "\n";
                                    }
                                }
                                ClientboundExport export = new ClientboundExport(subActions);
                                network.sendMessage(export);
                            }
                    );
                }
            }
            PlayerNetwork network = PlayerNetwork.getNetwork(player);
            if (network.isUsingMod()) {
                addItem(48, ItemBuilder.create(Material.PINK_DYE)
                                .name("&aImport Actions")
                                .description("Because you have the mod installed, you can import actions from a file.")
                                .lClick(ItemBuilder.ActionType.IMPORT_YELLOW).build(),
                        () -> network.sendMessage(new ClientboundImport())
                );

                if (network.getProtocolVersion() >= 2) {
                    addItem(46, ItemBuilder.create(Material.LIGHT_BLUE_DYE)
                                    .name("&aImport Actions from Websocket")
                                    .description("Because you have the mod installed, you can import actions automatically from a websocket created by the PTSL+ VSCode Extension.")
                                    .lClick(ItemBuilder.ActionType.ADD_YELLOW).build(),
                            () -> openChat(main, "7000", (message) -> {
                                if (NumberUtilsKt.isInt(message)) {
                                    if (network.getActionsMenu(Integer.parseInt(message)) != null) {
                                        player.sendMessage(colorize("&cAn actions menu with that port is already open!"));
                                        return;
                                    }
                                    network.sendMessage(new ClientboundWebsocket(Integer.parseInt(message)));
                                    network.setActionsMenu(Integer.parseInt(message), this);
                                } else {
                                    player.sendMessage(colorize("&cInvalid port!"));
                                }
                            })
                    );
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
                AddActionMenu menu = new AddActionMenu(main, player, house, this.actions, this, nestedLevel);
                menu.setEvent(event);
                menu.setNpc(housingNPC);
                menu.setFunction(function);
                menu.setParentActions(parentActions);
                menu.open();
            });
        }

        addItem(51, ItemBuilder.create(Material.ANVIL)
                .name("&aSearch")
                .description("Search for an action or condition.")
                .info("&7Current Value ", "")
                .info(null, search)
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .rClick(ItemBuilder.ActionType.CLEAR_SEARCH)
                .build(), () -> {
            player.sendMessage(colorize("&aSearch for an action or condition."));
            openChat(main, search, (message) -> {
                if (message == null || message.isEmpty()) {
                    setupItems();
                    return;
                }

                search = message;

                Bukkit.getScheduler().runTask(main, this::open);
            });
        }, () -> {
            search = "";
            setupItems();
        });


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

    private void replacePlayerWithNPC(ItemBuilder itemBuilder) {
        if (parentActions.stream().anyMatch(action -> action instanceof RunAsNPCAction)) {
            itemBuilder.description(itemBuilder.getDescription().replaceAll("(pP)layer", "NPC"));
        }
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
