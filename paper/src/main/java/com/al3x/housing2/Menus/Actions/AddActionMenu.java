package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.OpenMenuEvent;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
    private HousingNPC npc;
    private List<Action> actions;

    private List<Action> parentActions = new ArrayList<>();
    private int nestedLevel = 0;

    private String search = "";

    //Will be used for random actions and conditions
    public AddActionMenu(Main main, Player player, HousingWorld house, List<Action> actions, Menu backMenu, int nestedLevel) {
        super(player, colorize("&aAdd Action"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.actions = actions;
        this.backMenu = backMenu;
        this.nestedLevel = nestedLevel;
        setupItems();
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public void setNpc(HousingNPC npc) {
        this.npc = npc;
    }

    public List<Action> getParentActions() {
        return parentActions;
    }

    public void setParentActions(List<Action> parentActions) {
        this.parentActions = parentActions;
    }

    @Override
    public void open() {
        OpenMenuEvent event = new OpenMenuEvent(this, player, Main.getInstance());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
                AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
            }
            MenuManager.setWindowOpen(player, this);
            MenuManager.setMenu(player, this);
            return;
        }

        this.inventory = Bukkit.createInventory(null, 54, "§aAdd Action (" + page + "/" + getActions().getPageCount() + ")");
        setupItems();
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        PaginationList<Action> paginationList = getActions();
        List<Action> actionsList = paginationList.getPage(page);

        final boolean[] hasPause = {false};
        if (actionsList != null && !actionsList.isEmpty()) {
            for (int i = 0; i < actionsList.size(); i++) {
                Action action = actionsList.get(i);
                ItemBuilder item = new ItemBuilder();
                action.createAddDisplayItem(item);

                addItem(slots[i] - 1, item.build(), () -> {
                    if (ActionEditMenu.isLimitReached(actions, action)) {
                        player.sendMessage(colorize("&cYou have reached the limit for this action!"));
                        return;
                    }

                    if (action instanceof PauseAction) {
                        hasPause[0] = true;
                    }

                    if (action instanceof CancelAction && hasPause[0]) {
                        player.sendMessage(colorize("&cJust know any cancel action after a pause action will not work!"));
                    }

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);


                    actions.add(action);
                    if (backMenu != null) {
                        backMenu.open();
                        return;
                    }
                });
            }
        }

        if (page < paginationList.getPageCount()) {
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
            ActionsMenu menu = new ActionsMenu(main, player, house, actions, null, null);
            menu.setEvent(event);
            menu.setFunction(function);
            menu.setParentActions(parentActions);
            menu.open();
        });
    }

    public PaginationList<Action> getActions() {
        List<Action> actionArray = Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter(Objects::nonNull).toList();
        List<Action> newActions = new ArrayList<>();

        for (Action action : actionArray) {
            if (action == null) continue;

            if (findRunAsNPCMenu() == null && action instanceof NPCAction npcA && npcA.hide()) continue;

            if (findRunAsNPCMenu() != null) {
                if (!(action instanceof NPCAction)) continue;
                if (action.getName().equals("Break Action") || action.getName().equals("Continue Action")) continue;
                if (action instanceof ExitAction) {
                    //Jesus fucking christ I hate my life <3
                    RepeatAction repeatAction = findRepeatMenu();
                    if (repeatAction != null) {
                        System.out.println("Repeat action found");
                        newActions.add(new BreakAction());
                        newActions.add(new ContinueAction());
                    }
                }

                newActions.add(action);
                continue;
            }

            if (function != null) {
                if (function.isGlobal() && action.requiresPlayer()) continue;
                if (action.allowedEvents() != null && !action.allowedEvents().contains(EventType.FUNCTION)) continue;
                if (action.disallowedEvents() != null && action.disallowedEvents().contains(EventType.FUNCTION)) continue;
                newActions.add(action);
                continue;
            }

            if (action.nestLimit() != -1 && nestedLevel >= action.nestLimit()) continue;

            if (event != null) {
                if (action.allowedEvents() != null && !action.allowedEvents().contains(event)) continue;
                if (action.disallowedEvents() != null && action.disallowedEvents().contains(event)) continue;
                newActions.add(action);
                continue;
            }
            if (action.allowedEvents() != null) continue;

            if (action.getName().equals("Break Action") || action.getName().equals("Continue Action")) continue;

            if (action instanceof RunAsNPCAction) { //update the default if the npc is not null
                if (npc != null) action = new RunAsNPCAction(npc);
            }

            newActions.add(action);

            if (action instanceof ExitAction) {
                //Jesus fucking christ I hate my life <3
                RepeatAction repeatAction = findRepeatMenu();
                if (repeatAction != null) {
                    System.out.println("Repeat action found");
                    newActions.add(new BreakAction());
                    newActions.add(new ContinueAction());
                }
            }

            if (action instanceof ConditionalAction && npc != null) {
                newActions.add(new CancelAction());
            }
        }

        if (search != null) {
            newActions = newActions.stream().filter(i -> Color.removeColor(i.getName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(newActions, 21);
    }

    public int getNestedLevel() {
        return nestedLevel;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    private RepeatAction findRepeatMenu() {
        for (Action parentAction : parentActions) {
            if (parentAction instanceof RepeatAction) {
                return (RepeatAction) parentAction;
            }
        }
        return null;
    }

    private RunAsNPCAction findRunAsNPCMenu() {
        for (Action parentAction : parentActions) {
            if (parentAction instanceof RunAsNPCAction) {
                return (RunAsNPCAction) parentAction;
            }
        }
        return null;
    }

    private Menu getBackMenu() {
        return backMenu;
    }
}
