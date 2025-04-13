package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
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
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;

@Setter
public class StatInstanceProperty extends ExpandableProperty<List<StatInstance>> implements ActionProperty.PropertySerializer<List<StatInstance>, List<StatInstance.StatInstanceData>> {
    boolean showExpression = false;
    public StatInstanceProperty() {
    }

    @Override
    public List<ActionProperty<?>> getProperties() {
        List<ActionProperty<?>> properties = new ArrayList<>();
        List<StatInstance> value = getValue();
        for (int i = 0; i < value.size(); i++) {
            StatInstance instance = value.get(i);
            properties.add(new ModeProperty(i, instance));
            properties.add(new StatValueProperty(i, instance));
        }
        return properties;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {

    }

    @Override
    public List<StatInstance.StatInstanceData> serialize() {
        return getValue().stream().map(StatInstance.StatInstanceData::new).collect(Collectors.toList());
    }

    @Override
    public List<StatInstance> deserialize(List<StatInstance.StatInstanceData> value, HousingWorld house) {
        return value.stream().map((data) -> {
            StatInstance instance = new StatInstance();
            instance.mode = data.mode;
            Action action = ActionData.fromData(data.value);
            if (action instanceof StatValue) {
                instance.value = (StatValue) action;
            } else {
                instance.value = new StatValue();
                Main.getInstance().getLogger().severe("StatInstanceProperty: Action is not a StatValue in housing " + house.getHouseUUID().toString());
            }
            return instance;
        }).collect(Collectors.toList());
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
}
