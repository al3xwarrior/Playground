package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem.ActionType;
import com.al3x.housing2.Commands.Housing;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StackUtils;
import com.al3x.housing2.api.Housing2Api;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionEditMenu extends Menu {
    private Main main;
    private Action action;
    private Condition condition;
    private Player player;
    private HousingWorld house;
    private HousingNPC housingNPC;
    private EventType event;
    private Runnable update;
    private Menu backMenu;

    public Menu getBackMenu() {
        return backMenu;
    }

    private static ActionEditor getEditor(Action action, HousingWorld house, ActionEditMenu menu, Player player) {
        return action.editorMenu(house) != null ? action.editorMenu(house) : action.editorMenu(house, menu) != null ? action.editorMenu(house, menu) : action.editorMenu(house, menu, player);
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
    public String getTitle() {
        ActionEditor editor;
        if (action == null) {
            editor = getEditor(condition, house, this, player);
        } else {
            editor = getEditor(action, house, this, player);
        }

        return editor.getTitle();
    }

    @Override
    public void setupItems() {
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
        List<ActionEditor.ActionItem> items = editor.getItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        for (int i = 0; i < items.size(); i++) {
            ActionEditor.ActionItem item = items.get(i);
            int slot = slots[i] - 1;
            if (item.getSlot() != -1) {
                slot = item.getSlot();
            }
            addItem(slot, item.getBuilder().build(), (e) -> {
                if (item.getCustomRunnable() != null && (item.getType() == null || item.getType() == ActionType.CUSTOM)) {
                    if (item.getCustomRunnable().apply(e, null)) return;
                }

                //I don't understand java
                var o = new Object() {
                    Field field = null;
                    Object value = null;

                    public void setValue(Object value) {
                        if (item.getCustomRunnable() != null) {
                            if (!item.getCustomRunnable().apply(e, value)) return;
                            return;
                        }
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
                        if (action == null) {
                            new ActionEnumMenu(condition, item, main, player, house, event, this).open();
                            break;
                        }
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
                            actionMenu.setUpdate(update);
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
                                open();
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }

                    case GROUP: {
                        List<Duple<Group, ItemBuilder>> groups = new ArrayList<>();
                        for (Group group : house.getGroups()) {
                            groups.add(new Duple<>(group, ItemBuilder.create(Material.PAPER).name(group.getName()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }
                        PaginationMenu<Group> paginationMenu = new PaginationMenu<>(main, "Select a Group", groups, player, house, this, (group) -> {
                            try {
                                // Set the field
                                if (action != null) {
                                    Field field = action.getClass().getDeclaredField(item.getVarName());
                                    field.setAccessible(true);
                                    field.set(action, group.getName());
                                    player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + group.getName()));
                                    open();
                                } else {
                                    Field field = condition.getClass().getDeclaredField(item.getVarName());
                                    field.setAccessible(true);
                                    field.set(condition, group.getName());
                                    player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + group.getName()));
                                    open();
                                }
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }

                    case REGION: {
                        List<Duple<Region, ItemBuilder>> regions = new ArrayList<>();
                        for (Region region : house.getRegions()) {
                            regions.add(new Duple<>(region, ItemBuilder.create(Material.GRASS_BLOCK).name(region.getName()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }
                        PaginationMenu<Region> paginationMenu = new PaginationMenu<>(main, "Select a Region", regions, player, house, this, (region) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, region.getName());
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + region.getName()));
                                open();
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }

                    case LAYOUT: {
                        List<Duple<Layout, ItemBuilder>> layouts = new ArrayList<>();
                        for (Layout layout : house.getLayouts()) {
                            layouts.add(new Duple<>(layout, ItemBuilder.create(layout.getIcon()).name(layout.getName()).description(layout.getDescription()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }
                        PaginationMenu<Layout> paginationMenu = new PaginationMenu<Layout>(main, "Select a Layout", layouts, player, house, this, (layout) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, layout.getName());
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + layout.getName()));
                                open();
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }
                    case MENU: {
                        List<Duple<CustomMenu, ItemBuilder>> customMenus = new ArrayList<>();
                        for (CustomMenu customMenu : house.getCustomMenus()) {
                            customMenus.add(new Duple<>(customMenu, ItemBuilder.create(Material.CHEST).name(customMenu.getTitle()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }
                        PaginationMenu<CustomMenu> paginationMenu = new PaginationMenu<>(main, "Select a Menu", customMenus, player, house, this, (layout) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, layout.getTitle());
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + layout.getTitle()));
                                open();
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
                    case ITEM: {
                        if (o.field == null) return;
                        new ItemSelectMenu(player, this, (selectedItem) -> {
                            o.setValue(selectedItem);
                            if (selectedItem == null) {
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: &cNone"));
                                open();
                                return;
                            }
                            player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + StackUtils.getDisplayName(selectedItem)));
                            open();
                        }).open();
                        break;
                    }
                    case NPC: {
                        if (o.field == null) return;
                        List<Duple<HousingNPC, ItemBuilder>> npcs = new ArrayList<>();
                        for (HousingNPC npc : house.getNPCs()) {
                            double distance = npc.getLocation().distance(player.getLocation());
                            npcs.add(new Duple<>(npc, ItemBuilder.create(Material.PLAYER_HEAD).name(npc.getName()).info("Distance", Math.toIntExact(Math.round(distance))).info("NPC ID", npc.getNpcID()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
                        }

                        npcs.sort(Comparator.comparing(npc -> npc.getFirst().getLocation().distance(player.getLocation())));

                        PaginationMenu<HousingNPC> paginationMenu = new PaginationMenu<>(main, "Select a NPC", npcs, player, house, this, (npc) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, npc.getNpcID());
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + npc.getName()));
                                open();
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        paginationMenu.open();
                        break;
                    }
                    case ACTION_SETTING: {
                        try {
                            new ActionEditMenu((Action) action.getField(item.getVarName()), main, player, house, this).open();
                        } catch (NoSuchFieldException | IllegalAccessException | NumberFormatException ex) {
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
        addItem((editor.getRows() * 9) - 5, backArrow, () -> {
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });

        // Add export button
        if (action != null) {
            ItemBuilder export = ItemBuilder.create(Material.LIME_DYE).name("&aCopy to Clipboard").lClick(ItemBuilder.ActionType.EXPORT_YELLOW);
            addItem(((editor.getRows() - 1) * 9), export.build(), (e) -> {
                main.getClipboardManager().addAction(player.getUniqueId().toString(), action);
            });

            ItemBuilder importItem = ItemBuilder.create(Material.CYAN_DYE).name("&bImport from Clipboard").lClick(ItemBuilder.ActionType.IMPORT_YELLOW);
            addItem(((editor.getRows() - 1) * 9) + 1, importItem.build(), (e) -> {
                new ActionClipboardMenu(player, main, house, action, this).open();
            });

            ItemBuilder editExternally = ItemBuilder.create(Material.PURPLE_DYE).name("&eEdit Externally").lClick(ItemBuilder.ActionType.EDIT_YELLOW);
            addItem(((editor.getRows() - 1) * 9) + 2, editExternally.build(), (e) -> {
                TextComponent text = Component.text("Exporting to external editor...").color(TextColor.color(0xFFEE00));
                player.sendMessage(text);
                Housing2Api.Companion.getInstance().addEditing(
                        player.getUniqueId().toString(), UUID.randomUUID().toString(), action
                );

                text = Component.text("Exported successfully, open it ").append(
                        Component.text("here").color(TextColor.color(0x00FF00)).decorate(TextDecoration.UNDERLINED)
                                .clickEvent(ClickEvent.openUrl("http://localhost:8080/action?uuid=" + player.getUniqueId()))
                                .hoverEvent(Component.text("Click to open the external editor")
                                        .color(TextColor.color(0xFFEE00))
                                )
                ).append(
                        Component.text(" §c[Back]").clickEvent(ClickEvent.runCommand("/cancelinput"))
                                .hoverEvent(Component.text("§eClick to go back to where you were"))
                ).color(TextColor.color(0xFFEE00));
                player.sendMessage(text);
                player.closeInventory();
            });
        }
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public void setHousingNPC(HousingNPC housingNPC) {
        this.housingNPC = housingNPC;
    }

    public void setUpdate(Runnable update) {
        this.update = update;
    }

    public Runnable getUpdate() {
        return update;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
