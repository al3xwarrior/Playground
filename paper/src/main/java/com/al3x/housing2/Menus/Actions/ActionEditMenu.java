package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Properties.CustomSlotProperty;
import com.al3x.housing2.Action.Properties.ExpandableProperty;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.OpenActionMenuEvent;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StackUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
@Setter
@Getter
public class ActionEditMenu extends Menu {
    private final Main main;
    private Action action;
    private Condition condition;
    private final Player player;
    private final HousingWorld house;
    @Setter
    private HousingNPC housingNPC;
    @Setter
    private EventType event;
    @Getter
    @Setter
    private Runnable update;
    @Getter
    private final Menu backMenu;

    @Getter
    @Setter
    private List<Action> parentActions = new ArrayList<>();

    public int getBackMenusNestedLevel() {
        if (backMenu == null) {
            return 0;
        }
        if (backMenu instanceof ActionsMenu) {
            return ((ActionsMenu) backMenu).getNestedLevel();
        }
        if (backMenu instanceof AddActionMenu) {
            return ((AddActionMenu) backMenu).getNestedLevel();
        }
        return 0;
    }

    private static ActionEditor getEditor(Action action, HousingWorld house, ActionEditMenu menu, Player player) {
        return action.editorMenu(house, menu, player);
    }

    private static ActionEditor getEditor(Condition condition, HousingWorld house, ActionEditMenu menu, Player player) {
        return condition.editorMenu(house, menu, player);
    }

    //Action
    public ActionEditMenu(Action action, Main main, Player player, HousingWorld house, Menu backMenu) {
        super(player, colorize(getEditor(action, house, null, player).getTitle()), getEditor(action, house, null, player).getRows() * 9);
        this.main = main;
        this.action = action;
        this.player = player;
        this.house = house;
        this.backMenu = backMenu;
    }

    // Condition
    public ActionEditMenu(Condition condition, Main main, Player player, HousingWorld house, Menu backMenu) {
        super(player, colorize(getEditor(condition, house, null, player).getTitle()), getEditor(condition, house, null, player).getRows() * 9);
        this.main = main;
        this.action = null;
        this.condition = condition;
        this.player = player;
        this.house = house;
        this.backMenu = backMenu;
    }

    @Override
    public String getTitle() {
        ActionEditor editor;
        if (action == null) {
            editor = getEditor(condition, house, this, player);
        } else {
            editor = getEditor(action, house, this, player);
        }

        return editor.getTitle();
    }

    public static boolean isLimitReached(List<Action> actions, Action action) {
        if (action.limit() != -1) {
            int count = 0;
            for (Action a : actions) {
                if (a.toString().equals(action.toString())) {
                    count++;
                }
            }
            if (count >= action.limit()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void open() {
        Bukkit.getScheduler().runTask(main, () -> {
            OpenActionMenuEvent event = new OpenActionMenuEvent(this, action, main, player, house, backMenu);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
                    AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
                }
                MenuManager.setWindowOpen(player, this);
                MenuManager.setMenu(player, this);
                return;
            }
            super.open();
        });
    }

    @Override
    public void initItems() {
        if (update != null) update.run();

        clearItems();
        //Only needed for actions that need their own custom menu
        ActionEditor editor;
        if (action == null) {
            editor = getEditor(condition, house, this, player);
        } else {
            editor = getEditor(action, house, this, player);
        }
        setTitle(colorize(editor.getTitle()));
        List<ActionProperty<?>> properties = new ArrayList<>(editor.getProperties());
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        //Go through all properties and expand them
        for (int i = 0; i < properties.size(); i++) {
            ActionProperty<?> property = properties.get(i);
            if (property instanceof ExpandableProperty) {
                ExpandableProperty expandableProperty = (ExpandableProperty) property;
                List<ActionProperty<?>> expandedProperties = expandableProperty.getProperties();
                properties.remove(property);
                properties.addAll(i, expandedProperties);
            }
        }

        for (int i = 0; i < properties.size(); i++) {
            ActionProperty<?> property = properties.get(i);
            ItemBuilder builder = property.getDisplayItem();
            int slot = slots[i] - 1;
            if (property instanceof CustomSlotProperty<?> customSlotProperty) {
                slot = customSlotProperty.getCustomSlot();
            }
            addItem(slot, builder.build(), (e) -> {
                e.setCancelled(true);
                property.runnable(e, house, player, this);
            });
        }

        // Add back button
        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem((editor.getRows() * 9) - 5, backArrow, () -> {
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });

        addItem(editor.getRows() * 9 - 1, ItemBuilder.create(Material.PAPER)
                .name(colorize("&dSet Comment"))
                .description("Sets a comment to be visible from the menu")
                .build(), () -> {
            player.sendMessage(colorize("&aEnter what you would like the comment to be."));
            if (action.getComment() == null) {
                action.setComment("");
            }
            openChat(main, action.getComment(), (message) -> {
                if (message.length() > 100) {
                    player.sendMessage(colorize("&cMaximum comment length of 100 characters!"));
                    return;
                }
                action.setComment(message);
            });
        });

        // Add export button
        if (action != null) {
            ItemBuilder export = ItemBuilder.create(Material.LIME_DYE).name("&aCopy to Clipboard").lClick(ItemBuilder.ActionType.EXPORT_YELLOW);
            addItem(((editor.getRows() - 1) * 9), export.build(), () -> {
                main.getClipboardManager().addAction(player.getUniqueId().toString(), action);
            });

            ItemBuilder importItem = ItemBuilder.create(Material.CYAN_DYE).name("&bImport from Clipboard").lClick(ItemBuilder.ActionType.IMPORT_YELLOW);
            addItem(((editor.getRows() - 1) * 9) + 1, importItem.build(), () -> {
                new ActionClipboardMenu(player, main, house, action, this).open();
            });
        }
    }
}
