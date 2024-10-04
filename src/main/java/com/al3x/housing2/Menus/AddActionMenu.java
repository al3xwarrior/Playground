package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.RandomActionMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (page == 1) {
            ItemStack pushItem = new ItemStack(Material.PISTON);
            ItemMeta pushItemMeta = pushItem.getItemMeta();
            pushItemMeta.setDisplayName(colorize("&aPush Player Action"));
            pushItem.setItemMeta(pushItemMeta);
            addItem(0, pushItem, () -> {
                actions.add(new PushPlayerAction(house));
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            //We go ahead and create the action here so we can check if it is allowed
            CancelAction cancelAction = new CancelAction();
            if (cancelAction.allowedEvents().contains(event)) {
                ItemStack cancelItem = new ItemStack(Material.TNT);
                ItemMeta cancelItemMeta = cancelItem.getItemMeta();
                cancelItemMeta.setDisplayName(colorize("&aCancel Action"));
                cancelItem.setItemMeta(cancelItemMeta);
                addItem(11, cancelItem, () -> {
                    actions.add(cancelAction);
                    if (backMenu == null) {
                        house.setEventActions(event, actions);
                        new ActionsMenu(main, player, house, event).open();
                        return;
                    }
                    backMenu.open();
                });
            }

            ItemStack killItem = new ItemStack(Material.IRON_BARS);
            ItemMeta killItemMeta = killItem.getItemMeta();
            killItemMeta.setDisplayName(colorize("&aKill Player Action"));
            killItem.setItemMeta(killItemMeta);
            addItem(12, killItem, () -> {
                actions.add(new KillPlayerAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack fullHealItem = new ItemStack(Material.GOLDEN_APPLE);
            ItemMeta fullHealItemMeta = fullHealItem.getItemMeta();
            fullHealItemMeta.setDisplayName(colorize("&aFull Heal Action"));
            fullHealItem.setItemMeta(fullHealItemMeta);
            addItem(13, fullHealItem, () -> {
                actions.add(new FullHealAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack sendTitleItem = new ItemStack(Material.BOOK);
            ItemMeta sendTitleMeta = sendTitleItem.getItemMeta();
            sendTitleMeta.setDisplayName(colorize("&aSend Title Action"));
            sendTitleItem.setItemMeta(sendTitleMeta);
            addItem(14, sendTitleItem, () -> {
                actions.add(new SendTitleAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack actionBarItem = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta actionBarItemMeta = actionBarItem.getItemMeta();
            actionBarItemMeta.setDisplayName(colorize("&aAction Bar Action"));
            actionBarItem.setItemMeta(actionBarItemMeta);
            addItem(15, actionBarItem, () -> {
                actions.add(new ActionbarAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack resetInventoryItem = new ItemStack(Material.STONE);
            ItemMeta resetInventoryItemMeta = resetInventoryItem.getItemMeta();
            resetInventoryItemMeta.setDisplayName(colorize("&aReset Inventory Action"));
            resetInventoryItem.setItemMeta(resetInventoryItemMeta);
            addItem(16, resetInventoryItem, () -> {
                actions.add(new ResetInventoryAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack chatMessageItem = new ItemStack(Material.PAPER);
            ItemMeta chatMessageMeta = chatMessageItem.getItemMeta();
            chatMessageMeta.setDisplayName(colorize("&aSend Chat Action"));
            chatMessageItem.setItemMeta(chatMessageMeta);
            addItem(23, chatMessageItem, () -> {
                actions.add(new ChatAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack statChangeItem = new ItemStack(Material.FEATHER);
            ItemMeta statChangeItemMeta = statChangeItem.getItemMeta();
            statChangeItemMeta.setDisplayName(colorize("&aChange Stat Action"));
            statChangeItem.setItemMeta(statChangeItemMeta);
            addItem(29, statChangeItem, () -> {
                actions.add(new PlayerStatAction(player, house));
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            if (backMenu == null) {
                ItemStack showRandomActionItem = new ItemStack(Material.ENDER_CHEST);
                ItemMeta showRandomActionItemMeta = showRandomActionItem.getItemMeta();
                showRandomActionItemMeta.setDisplayName(colorize("&aRandom Action"));
                showRandomActionItem.setItemMeta(showRandomActionItemMeta);
                addItem(32, showRandomActionItem, () -> {
                    RandomAction randomAction = new RandomAction();
                    actions.add(randomAction);
                    house.setEventActions(event, actions);
                    new RandomActionMenu(main, player, house, randomAction, event).open();
                });
            }

            ItemStack showBossbarItem = new ItemStack(Material.WITHER_SKELETON_SKULL);
            ItemMeta showBossbarItemMeta = showBossbarItem.getItemMeta();
            showBossbarItemMeta.setDisplayName(colorize("&aShow Bossbar Action"));
            showBossbarItem.setItemMeta(showBossbarItemMeta);
            addItem(33, showBossbarItem, () -> {
                actions.add(new ShowBossbarAction());
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack playSoundItem = new ItemStack(Material.NOTE_BLOCK);
            ItemMeta playSoundItemMeta = playSoundItem.getItemMeta();
            playSoundItemMeta.setDisplayName(colorize("&aPlay Sound Action"));
            playSoundItem.setItemMeta(playSoundItemMeta);
            addItem(34, playSoundItem, () -> {
                actions.add(new PlaySoundAction(player, house));
                if (backMenu == null) {
                    house.setEventActions(event, actions);
                    new ActionsMenu(main, player, house, event).open();
                    return;
                }
                backMenu.open();
            });

            ItemStack forwardArrow = new ItemStack(Material.ARROW);
            ItemMeta forwardArrowMeta = forwardArrow.getItemMeta();
            forwardArrowMeta.setDisplayName(colorize("&aNext Page"));
            forwardArrow.setItemMeta(forwardArrowMeta);
            addItem(53, forwardArrow, () -> {
                new AddActionMenu(main, player, 2, house, event).open();
            });
        } else if (page == 2) {


            ItemStack backArrow = new ItemStack(Material.ARROW);
            ItemMeta backArrowMeta = backArrow.getItemMeta();
            backArrowMeta.setDisplayName(colorize("&aLast Page"));
            backArrow.setItemMeta(backArrowMeta);
            addItem(45, backArrow, () -> {
                new AddActionMenu(main, player, 1, house, event).open();
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
