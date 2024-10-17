package com.al3x.housing2.Menus;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionEditMenu extends Menu {
    private Main main;
    private Action action;
    private Player player;
    private HousingWorld house;
    private HousingNPC housingNPC;
    private EventType event;
    private Menu backMenu;

    // NPC
    public ActionEditMenu(Action action, Main main, Player player, HousingWorld house, HousingNPC housingNPC) {
        super(player, colorize(action.editorMenu(house).getTitle()), action.editorMenu(house).getRows() * 9);
        this.main = main;
        this.action = action;
        this.player = player;
        this.house = house;
        this.housingNPC = housingNPC;
    }

    // Events
    public ActionEditMenu(Action action, Main main, Player player, HousingWorld house, EventType event) {
        super(player, colorize(action.editorMenu(house).getTitle()), action.editorMenu(house).getRows() * 9);
        this.main = main;
        this.action = action;
        this.player = player;
        this.house = house;
        this.event = event;
    }



    @Override
    public void setupItems() {
        clearItems();
        ActionEditor editor = action.editorMenu(house);
        List<ActionEditor.ActionItem> items = editor.getItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        for (int i = 0; i < items.size(); i++) {
            ActionEditor.ActionItem item = items.get(i);
            addItem(slots[i] - 1, item.getBuilder().build(), (e) -> {
                if (item.getCustomRunnable() != null) {
                    item.getCustomRunnable().run();
                }

                switch (item.getType()) {
                    case STRING: {
                        player.sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                        openChat(item, (input) -> {
                            try {
                                // Set the field
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, input);
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            }
                        });
                        break;
                    }
                    case INT: {
                        player.sendMessage(colorize("&ePlease enter the number you wish to set in chat!"));
                        openChat(item, (input) -> {
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
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, num);
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            } catch (NumberFormatException ex) {
                                player.sendMessage(colorize("&cInvalid number! Please enter a valid number."));
                            }
                        });
                        break;
                    }
                    case DOUBLE: {
                        player.sendMessage(colorize("&ePlease enter the number you wish to set in chat!"));
                        openChat(item, (input) -> {
                            try {
                                double num = Double.parseDouble(input);
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
                                Field field = action.getClass().getDeclaredField(item.getVarName());
                                field.setAccessible(true);
                                field.set(action, num);
                                player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + input));
                            } catch (NoSuchFieldException | IllegalAccessException ex) {
                                Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                                player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                            } catch (NumberFormatException ex) {
                                player.sendMessage(colorize("&cInvalid number! Please enter a valid number."));
                            }
                        });
                        break;
                    }
                    case ENUM: {
                        new ActionEnumMenu(action, item, main, player, house, event).open();
                        break;
                    }
                    case ACTION: {
                        try {
                            //Get the sub actions field and open the ActionsMenu
                            Field field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            List<Action> subActions = (List<Action>) field.get(action);
                            new ActionsMenu(main, player, house, subActions, this).open();
                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to get field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to get field " + item.getVarName() + " in " + action.getName()));
                        }
                        break;
                    }
                    case BOOLEAN: {
                        try {
                            Field field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            boolean value = (boolean) field.get(action);
                            field.set(action, !value);
                            player.sendMessage(colorize("&a" + item.getBuilder().getName() + " set to: " + !value));
                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
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
            if (housingNPC != null) {
                new ActionsMenu(main, player, house, housingNPC).open();
                return;
            }
            if (event != null) {
                new ActionsMenu(main, player, house, event).open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });
    }

    public void openChat(ActionEditor.ActionItem item, Consumer<String> consumer) {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {
                e.setCancelled(true);
                if (e.getPlayer().equals(player)) {
                    String input = e.getMessage();
                    consumer.accept(input);

                    // Unregister this listener after capturing the message
                    AsyncPlayerChatEvent.getHandlerList().unregister(this);

                    // Reopen the ActionEditMenu
                    Bukkit.getScheduler().runTaskLater(main, ActionEditMenu.this::open, 1L); // Delay slightly to allow chat event to complete
                }
            }
        }, main);
    }
}
