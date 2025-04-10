package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.al3x.housing2.Utils.Color.colorize;

@Getter
@Setter
@ToString
public abstract class ActionProperty<V> {
    protected Main main = Main.getInstance();

    private final String id;
    private final String name;
    private final String description;
    private final Material icon;

    private ItemBuilder.ActionType rClick = null;
    private ItemBuilder.ActionType mClick = null;

    @Getter
    private V value;
    private boolean visible = true;
    private final ItemBuilder builder;

    private int slot = -1;
    private double min = 0;
    private double max = Double.MAX_VALUE;
    private Class<? extends Enum<?>> enumClass;
    private ActionPropertyConsumer consumer;
    private ActionProperties properties;

    public ActionProperty(String id, String name, String description, Material icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;

        this.builder = new ItemBuilder()
                .material(icon)
                .name("<yellow>" + name)
                .description(description)
                .info("<b><yellow>Current value</b>", "")
                .info(null, "<green>" + value);
    }

    public ActionProperty<V> setValue(V value) {
        this.value = value;
        return this;
    }

    public abstract void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu);

    @Getter
    @RequiredArgsConstructor
    public enum PropertyType {
        STRING(StringProperty.class),
        INT(),
        DOUBLE(),
        NUMBER(),
        BOOLEAN(),
        ENUM(),
        LOCATION(),
        ITEM(),
        NPC(),
        REGION(),
        ACTION(),
        ACTION_SETTING(),
        FUNCTION(),
        GROUP(),
        TEAM(),
        LAYOUT(),
        MENU(),
        CONDITION(),
        SLOT(),
        PATH(),
        STAT_INSTANCE(),
        ACTION_PROPERTIES(),
        COLOR(),
        CUSTOM(),
        SOUND();

        private final Class<? extends ActionProperty<?>> propertyClass;
    }

    public ItemBuilder getDisplayItem() {
        return builder;
    }

    public ActionProperty<V> showIf(boolean condition) {
        this.visible = condition;
        return this;
    }

    public ActionProperty<V> mClick(ItemBuilder.ActionType action) {
        this.mClick = action;
        return this;
    }

    public ActionProperty<V> rClick(ItemBuilder.ActionType action) {
        this.rClick = action;
        return this;
    }

//    public static class StatProperties implements ActionProperties {
//        private final List<StatInstance> statInstances = new ArrayList<>();
//
//        public void setStatInstances(List<StatInstance> statInstances) {
//            this.statInstances.clear();
//            this.statInstances.addAll(statInstances);
//        }
//
//        @Override
//        public List<ActionProperty> actions() {
//            List<ActionProperty> properties = new ArrayList<>();
//            for (int i = 0; i < statInstances.size(); i++) {
//                StatInstance instance = statInstances.get(i);
//                int finalI = i;
//                properties.add(new ActionProperty(
//                        "mode",
//                        "Mode " + (i + 1),
//                        "The mode of the stat to modify.",
//                        ActionProperty.PropertyType.CUSTOM,
//                        (house, menu, player) -> modeConsumer(house, menu, player, instance, finalI)
//                ).setValue(instance).rClick((i == 0) ? null : ItemBuilder.ActionType.REMOVE_YELLOW));
//
//                properties.add(new ActionProperty(
//                        "value",
//                        "Value " + (i + 1),
//                        "The value of the stat to modify.",
//                        ActionProperty.PropertyType.CUSTOM,
//                        (house, menu, player) -> valueConsumer(house, menu, player, instance, finalI)
//                ).setValue(instance).rClick((i == 0) ? null : ItemBuilder.ActionType.REMOVE_YELLOW).mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION));
//            }
//
//            return properties;
//        }
//
//        private BiFunction<InventoryClickEvent, Object, Boolean> modeConsumer(HousingWorld house, Menu backMenu, Player player, StatInstance instance, int i) {
//            return (event, obj) -> {
//                if (event.getClick() == ClickType.RIGHT && i > 0) {
//                    statInstances.remove(instance);
//                    backMenu.open();
//                    return true;
//                }
//
//                if (event.getClick() != ClickType.LEFT) return false;
//
//                List<Duple<StatOperation, ItemBuilder>> modes = new ArrayList<>();
//                for (StatOperation mode : StatOperation.values()) {
//                    if (mode.expressionOnly()) continue;
//                    modes.add(new Duple<>(mode, ItemBuilder.create(mode.getMaterial()).name("&a" + mode)));
//                }
//                new PaginationMenu<>(Main.getInstance(), "&eSelect a mode", modes, backMenu.getOwner(), house, backMenu, (mode) -> {
//                    instance.mode = mode;
//                    backMenu.open();
//                }).open();
//
//                return true;
//            };
//        }
//
//        private BiFunction<InventoryClickEvent, Object, Boolean> valueConsumer(HousingWorld house, Menu backMenu, Player player, StatInstance instance, int i) {
//            return (event, obj) -> {
//                if (event.getClick() == ClickType.MIDDLE) {
//                    instance.value.setExpression(!instance.value.isExpression());
//                    backMenu.open();
//                    return true;
//                }
//
//                if (event.getClick() == ClickType.RIGHT && i > 0) {
//                    statInstances.remove(instance);
//                    backMenu.open();
//                    return true;
//                }
//
//                if (event.getClick() != ClickType.LEFT) return false;
//
//                if (instance.value.isExpression()) {
//                    new ActionEditMenu(instance.value, Main.getInstance(), backMenu.getOwner(), house, backMenu).open();
//                } else {
//                    backMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
//                    backMenu.openChat(Main.getInstance(), instance.value.getLiteralValue(), (value) -> {
//                        instance.value.setLiteralValue(value);
//                        backMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
//                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), backMenu::open, 1L);
//                    });
//                }
//                return true;
//            };
//        }
//    }

    public interface ActionProperties {
        List<ActionProperty<?>> actions();
    }

    public interface ActionPropertyConsumer {
        BiFunction<InventoryClickEvent, Object, Boolean> accept(HousingWorld house, Menu menu, Player player);
    }
}
