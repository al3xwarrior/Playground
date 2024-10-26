package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionEditMenu extends Menu {
    private Main main;
    private Action action;
    private Condition condition;
    private Player player;
    private HousingWorld house;
    private HousingNPC housingNPC;
    private EventType event;
    private Menu backMenu;

    private static ActionEditor getEditor(Action action, HousingWorld house, ActionEditMenu menu, Player player) {
        return action.editorMenu(house) != null ? action.editorMenu(house) : action.editorMenu(house, menu) != null ? action.editorMenu(house, menu) : action.editorMenu(house, player);
    }

    private static ActionEditor getEditor(Condition condition, HousingWorld house, ActionEditMenu menu, Player player) {
        return condition.editorMenu(house) != null ? condition.editorMenu(house) : condition.editorMenu(house, menu) != null ? condition.editorMenu(house, menu) : condition.editorMenu(house, player);
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
    public void setupItems() {
        clearItems();
        //Only needed for actions that need their own custom menu
        ActionEditor editor;
        if (action == null) {
            editor = getEditor(condition, house, this, player);
        } else {
            editor = getEditor(action, house, this, player);
        }
        List<ActionEditor.ActionItem> items = editor.getItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        for (int i = 0; i < items.size(); i++) {
            ActionEditor.ActionItem item = items.get(i);
            addItem(slots[i] - 1, item.getBuilder().build(), (e) -> {
                if (item.getCustomRunnable() != null) {
                    item.getCustomRunnable().run();
                    return;
                }

                //I don't understand java
                var o = new Object() {
                    Field field = null;
                    Object value = null;

                    public void setValue(Object value) {
                        try {
                            field.set((action == null) ? condition : action, value);
                        } catch (IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                        }
                    }
                };
                try {
                    if (action != null) {
                        o.field = action.getClass().getDeclaredField(item.getVarName());
                        o.field.setAccessible(true);
                        o.value = o.field.get(action);
                    } else {
                        o.field = condition.getClass().getDeclaredField(item.getVarName());
                        o.field.setAccessible(true);
                        o.value = o.field.get(condition);
                    }
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    Bukkit.getLogger().warning("Failed to get field " + item.getVarName() + " in " + action.getName());
                    player.sendMessage(colorize("&cFailed to get field " + item.getBuilder().getName() + " in " + action.getName()));
                }
                switch (item.getType()) {
                    case STRING: {
                        player.sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                        openChat(main, o.value.toString(), (input) -> {
                            // Set the field
                            if (o.field == null) return;
                            o.setValue(input);
                            player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                        });
                        break;
                    }
                    case INT: {
                        player.sendMessage(colorize("&ePlease enter the number you wish to set in chat!"));
                        openChat(main, o.value.toString(), (input) -> {
                            try {
                                int num = Integer.parseInt(input);
                                // Check if the number is within the min and max range
                                if (num < item.getMin()) {
                                    player.sendMessage(colorize("&cNumber must be greater than " + item.getMin()));
                                    return;
                                }
                                if (num > item.getMax()) {
                                    player.sendMessage(colorize("&cNumber must be less than " + item.getMax()));
                                    return;
                                }
                                // Set the field
                                if (o.field == null) return;
                                o.setValue(num);
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                            } catch (NumberFormatException ex) {
                                player.sendMessage(colorize("&cInvalid number! Please enter a valid number."));
                            }
                        });
                        break;
                    }
                    case DOUBLE: {
                        player.sendMessage(colorize("&ePlease enter the number you wish to set in chat!"));
                        openChat(main, o.value.toString(), (input) -> {
                            try {
                                // a google search told me that using Double.valueOf might work compared to Double.parseDouble
                                double num = Double.valueOf(input);
                                // Check if the number is within the min and max range
                                if (num < item.getMin()) {
                                    player.sendMessage(colorize("&cNumber must be greater than " + item.getMin()));
                                    return;
                                }
                                if (num > item.getMax()) {
                                    player.sendMessage(colorize("&cNumber must be less than " + item.getMax()));
                                    return;
                                }
                                // Set the field
                                if (o.field == null) return;
                                o.setValue(num);
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                            } catch (NumberFormatException ex) {
                                player.sendMessage(colorize("&cInvalid number! Please enter a valid number."));
                            }
                        });
                        break;
                    }
                    case ENUM: {
                        new ActionEnumMenu(action, item, main, player, house, event, this).open();
                        break;
                    }
                    case ACTION: {
                        try {
                            //Get the sub actions field and open the ActionsMenu
                            Field field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            List<Action> subActions = (List<Action>) field.get(action);
                            ActionsMenu actionMenu = new ActionsMenu(main, player, house, subActions, this, item.getVarName());
                            //Add this to check for events and such
                            actionMenu.setEvent(event);
                            actionMenu.setHousingNPC(housingNPC);

                            actionMenu.open();
                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to get field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to get field " + item.getVarName() + " in " + action.getName()));
                        }
                        break;
                    }
                    case CONDITION: {
                        try {
                            //Get the sub actions field and open the ActionsMenu
                            Field field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            List<Condition> subActions = (List<Condition>) field.get(action);
                            ActionsMenu actionMenu = new ActionsMenu(main, player, house, subActions, this, true);
                            if (event != null) {
                                actionMenu.setEvent(event);
                            }
                            if (housingNPC != null) {
                                actionMenu.setHousingNPC(housingNPC);
                            }
                            actionMenu.open();
                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to get field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to get field " + item.getVarName() + " in " + action.getName()));
                        }
                        break;
                    }
                    case FUNCTION: {
                        List<Duple<Function, ItemBuilder>> functions = new ArrayList<>();
                        for (Function function : house.getFunctions()) {
                            functions.add(new Duple<>(function, ItemBuilder.create(function.getMaterial()).name(function.getName()).description(function.getDescription()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }
                        PaginationMenu<Function> paginationMenu = new PaginationMenu<Function>(main, "Select a Function", functions, player, house, this, (function) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, function.getName());
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + function.getName()));
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }
                    case BOOLEAN: {
                        if (o.field == null) return;
                        boolean value = o.value instanceof Boolean ? (boolean) o.value : false;
                        o.setValue(!value);
                        player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + !value));
                        open();
                        break;
                    }
                    case ACTION_SETTING: {
                        try {
                            Field field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            new ActionEditMenu((Action) field.get(action), main, player, house, this).open();
                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to get field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to get field " + item.getVarName() + " in " + action.getName()));
                        }
                        break;
                    }
                }
            });
        }

        // Add back button
        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(31, backArrow, () -> {
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public void setHousingNPC(HousingNPC housingNPC) {
        this.housingNPC = housingNPC;
    }
}
