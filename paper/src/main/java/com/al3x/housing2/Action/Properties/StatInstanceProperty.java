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
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.al3x.housing2.Instances.HousesManager.gson;
import static com.al3x.housing2.Utils.Color.colorize;

@Setter
public class StatInstanceProperty extends ExpandableProperty<List<StatInstance>> implements ActionProperty.PropertySerializer<List<StatInstance>, List<StatInstance.StatInstanceData>> {
    boolean showExpression = false;
    public StatInstanceProperty() {
        super("statInstances");
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
    public List<StatInstance> deserialize(JsonElement val, HousingWorld housingWorld) {
        List<StatInstance.StatInstanceData> value = dataToList(val.getAsJsonArray(), StatInstance.StatInstanceData.class);

        return value.stream().map((data) -> {
            StatInstance instance = new StatInstance();
            instance.mode = data.mode;
            instance.value = new StatValueProperty.StatValueInstance(
                    data.value.isExpression(),
                    data.value.getLiteralValue(),
                    StatValue.fromActionData(data.value.getExpressionValue(), housingWorld)
            );
            return instance;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Duple<String, String>> getInfo() {
        List<Duple<String, String>> info = new LinkedList<>();
        info.add(new Duple<>("Stat Instances", ""));
        for (StatInstance instance : getValue()) {
            info.add(new Duple<>(instance.mode.name(), instance.value.toString()));
        }
        return info;
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
