package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.*;
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
    private EventType event;
    private Menu backMenu;

    private int currentPage = 0;
    private final int itemsPerPage = 45;

    public ActionsMenu(Main main, Player player, HousingWorld house, EventType event) {
        super(player, colorize("&7Edit Actions"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.event = event;
        this.actions = house.getEventActions(event);
        setupItems();
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

        setupItems();
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

        int start = currentPage * allowedSlots.length;
        int end = Math.min((actions != null) ? actions.size() : 0, start + allowedSlots.length);

        // Display the actions for the current page using the allowed slots
        for (int i = start; i < end; i++) {
            Action action = actions.get(i);
            int slot = allowedSlots[i - start]; // Use the predefined slots
            addItem(slot, action.getDisplayItem(), () -> {
                if (action == null) {
                    player.sendMessage(colorize("&cError: Action is null?"));
                    return;
                }

                if (action instanceof ChatAction) {
                    new ChatActionMenu(main, player, house, (ChatAction) action, event).open();
                    return;
                }

                if (action instanceof SendTitleAction) {
                    new TitleActionMenu(main, player, house, (SendTitleAction) action, event).open();
                    return;
                }

                if (action instanceof ActionbarAction) {
                    new ActionbarActionMenu(main, player, house, (ActionbarAction) action, event).open();
                    return;
                }

                if (action instanceof PlayerStatAction) {
                    new PlayerStatActionMenu(main, player, house, (PlayerStatAction) action, event).open();
                    return;
                }

                if (action instanceof PushPlayerAction) {
                    new PushPlayerActionMenu(main, player, house, (PushPlayerAction) action, event).open();
                    return;
                }

                if (action instanceof PlaySoundAction) {
                    new PlaySoundActionMenu(main, player, house, (PlaySoundAction) action, event).open();
                    return;
                }

                if (action instanceof RandomAction) {
                    new RandomActionMenu(main, player, house, (RandomAction) action, event).open();
                    return;
                }
            }, () -> {
                removeAction(action);
            });
        }

        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = previous.getItemMeta();
        prevMeta.setDisplayName(colorize("&aPrevious Page"));
        previous.setItemMeta(prevMeta);
        if (currentPage > 0) {
            addItem(45, previous, () -> {
                currentPage--;
                setupItems(); // Refresh the menu with the new page
                return;
            });
        } else {
            addItem(49, previous, () -> {
                new EventActionsMenu(main, player, house).open();
            });
        }


        if (end < ((actions != null) ? actions.size() : 0)) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(colorize("&aNext Page"));
            next.setItemMeta(nextMeta);
            addItem(53, next, () -> {
                currentPage++;
                setupItems(); // Refresh the menu with the new page
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
        });

        ItemStack addAction = new ItemStack(Material.PAPER);
        ItemMeta addActionMeta = addAction.getItemMeta();
        addActionMeta.setDisplayName(colorize("&aAdd Action"));
        addAction.setItemMeta(addActionMeta);
        addItem(50, addAction, () -> {
            new AddActionMenu(main, player, 1, house, event, actions, (backMenu != null ? this : null)).open();
        });
    }

}
