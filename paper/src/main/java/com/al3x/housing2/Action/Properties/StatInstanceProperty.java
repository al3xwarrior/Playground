package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.StatActionData;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

@Setter
public class StatInstanceProperty extends ExpandableProperty<List<StatInstance>> {
    boolean showExpression = false;
    public StatInstanceProperty(String id, String name, String description) {
        super(id, name, description, null);
    }

    @Override
    public List<ActionProperty<?>> getProperties() {
        List<ActionProperty<?>> properties = new ArrayList<>();
        List<StatInstance> value = getValue();
        for (int i = 0; i < value.size(); i++) {
            StatInstance instance = value.get(i);
            properties.add(new ModeProperty(i, instance));
            properties.add(new ValueProperty(i, instance));
        }
        return properties;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {

    }

    private class ModeProperty extends ActionProperty<StatOperation> {
        private final int index;
        private final StatInstance instance;
        public ModeProperty(int index, StatInstance instance) {
            super(
                    "mode" + index,
                    "Mode",
                    "The operation to perform on the stat",
                    Material.COMPASS
            );
            this.index = index;
            this.instance = instance;

            getBuilder().lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            if (index > 0) {
                getBuilder().rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            }
        }

        @Override
        protected String displayValue() {
            return instance.mode.name();
        }

        @Override
        public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
            if (event.getClick() == ClickType.RIGHT && index > 0) {
                StatInstanceProperty.this.getValue().remove(instance);
                menu.open();
                return;
            }

            if (event.getClick() != ClickType.LEFT) return;

            List<Duple<StatOperation, ItemBuilder>> modes = new ArrayList<>();
            for (StatOperation mode : StatOperation.values()) {
                if (!showExpression && mode.expressionOnly()) continue;
                modes.add(new Duple<>(mode, ItemBuilder.create(mode.getMaterial()).name("&a" + mode)));
            }
            new PaginationMenu<>(main, "&eSelect a mode", modes, player, house, menu, (mode) -> {
                instance.mode = mode;
                menu.open();
            }).open();
        }
    }

    private class ValueProperty extends ActionProperty<StatValue> {
        private final int index;
        private final StatInstance instance;
        public ValueProperty(int index, StatInstance instance) {
            super(
                    "value" + index,
                    "Amount",
                    "The value of the stat to modify.",
                    Material.BOOK
            );
            this.index = index;
            this.instance = instance;
            getBuilder().mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION);
            if (index > 0) {
                getBuilder().rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            }
        }

        @Override
        protected String displayValue() {
            return instance.value.toString();
        }

        @Override
        public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
            if (event.getClick() == ClickType.MIDDLE) {
                instance.value.setExpression(!instance.value.isExpression());
                menu.open();
                return;
            }

            if (event.getClick() == ClickType.RIGHT && index > 0) {
                StatInstanceProperty.this.getValue().remove(instance);
                menu.open();
                return;
            }

            if (event.getClick() != ClickType.LEFT) return;

            if (instance.value.isExpression()) {
                new ActionEditMenu(instance.value, main, player, house, menu).open();
            } else {
                player.sendMessage(colorize("&ePlease enter the value you wish to set in chat!"));
                menu.openChat(main, instance.value.getLiteralValue(), (value) -> {
                    instance.value.setLiteralValue(value);
                    player.sendMessage(colorize("&aValue set to: &e" + value));
                    Bukkit.getScheduler().runTaskLater(main, menu::open, 1L);
                });
            }
        }
    }
}
