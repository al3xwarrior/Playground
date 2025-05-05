package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Returnable;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ArgumentsProperty extends ExpandableProperty<List<ArgumentsProperty.Argument>> {

    public ArgumentsProperty(String id) {
        super(id);
    }

    @Override
    public List<Duple<String, String>> getInfo() {
        List<Duple<String, String>> info = new ArrayList<>();
        info.add(new Duple<>("Arguments", getValue().size() + ""));
        return info;
    }

    @Override
    public Returnable<Boolean> getVisible() {
        return () -> false;
    }

    @Override
    public List<ActionProperty<?>> getProperties() {
        List<ActionProperty<?>> properties = new ArrayList<>();
        for (int i = 0; i < getValue().size(); i++) {
            Argument argument = getValue().get(i);
            properties.add(
                    new CustomProperty<Argument>(
                            "arg_" + i,
                            "Argument: " + argument.name,
                            "The argument to pass to the function.",
                            Material.BOOK
                    ) {
                        @Override
                        public ItemBuilder getDisplayItem() {
                            return super.getDisplayItem()
                                    .mClick(ItemBuilder.ActionType.EDIT_KEY)
                                    .rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                        }

                        @Override
                        public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
                            if (event.isLeftClick()) {
                                menu.openChat(main, argument.value, (message -> {
                                    argument.value = message;
                                    menu.open();
                                }));
                            } else if (event.isRightClick()) {
                                ArgumentsProperty.this.getValue().remove(argument);
                                menu.open();
                            } else if (event.getClick() == ClickType.MIDDLE) {
                                menu.openChat(main, argument.name, (message -> {
                                    if (message.length() > 16) {
                                        player.sendMessage(colorize("&cThe argument name cannot be longer than 16 characters."));
                                        return;
                                    }
                                    if (message.isEmpty()) {
                                        player.sendMessage(colorize("&cThe argument name cannot be empty."));
                                        return;
                                    }
                                    if (ArgumentsProperty.this.getValue().stream().anyMatch(arg -> arg.name.equals(argument.name))) {
                                        player.sendMessage(colorize("&cYou already have an argument with that name."));
                                        return;
                                    }
                                    argument.name = message;
                                    menu.open();
                                }));
                            }
                        }
                    }
            );
        }

        return properties;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {

    }

    @Getter
    public static class Argument {
        String name;
        String value;

        public Argument(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
