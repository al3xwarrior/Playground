package com.al3x.housing2.Action;

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
public class ActionProperty {
    private final String id;
    private final String name;
    private final String description;
    private final PropertyType type;

    private int slot = -1;
    private double min = 0;
    private double max = Double.MAX_VALUE;
    private Class<? extends Enum<?>> enumClass;
    private ActionPropertyConsumer consumer;
    private ActionProperties properties;

    private ItemBuilder.ActionType rClick = null;
    private ItemBuilder.ActionType mClick = null;

    private boolean visible = true;
    private Object value;

    public ActionProperty(String id, String name, String description, PropertyType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;

        if (type == PropertyType.ENUM) {
            throw new IllegalArgumentException("Enum class must be provided for ENUM type");
        }
    }

    public ActionProperty(String id, String name, String description, PropertyType type, ActionPropertyConsumer consumer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.consumer = consumer;

        if (type == PropertyType.ENUM) {
            throw new IllegalArgumentException("Enum class must be provided for ENUM type");
        }
    }

    public ActionProperty(String id, String name, String description, PropertyType type, ActionProperties properties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.properties = properties;

        if (type == PropertyType.ENUM) {
            throw new IllegalArgumentException("Enum class must be provided for ENUM type");
        }
    }

    public ActionProperty(String id, String name, String description, PropertyType type, int slot, ActionPropertyConsumer consumer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.slot = slot;
        this.consumer = consumer;

        if (type != PropertyType.CUSTOM) {
            throw new IllegalArgumentException("Enum class must be provided for ENUM type");
        }
    }

    public ActionProperty(String id, String name, String description, PropertyType type, double min, double max) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.min = min;
        this.max = max;

        if (type != PropertyType.DOUBLE && type != PropertyType.INT && type != PropertyType.NUMBER) {
            throw new IllegalArgumentException(id + " is not a number type, but min and max values were provided.");
        }
    }

    public ActionProperty(String id, String name, String description, PropertyType type, Class<? extends Enum<?>> enumClass) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.enumClass = enumClass;
    }

    public ActionProperty(String id, String name, String description, PropertyType type, Class<? extends Enum<?>> enumClass, ActionPropertyConsumer consumer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.enumClass = enumClass;
        this.consumer = consumer;
    }

    public ActionProperty setValue(Object value) {
        switch (type) {
            case STRING:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Value must be a String");
                }
                break;
            case INT:
                if (!(value instanceof Integer)) {
                    throw new IllegalArgumentException("Value must be an Integer");
                }
                break;
            case DOUBLE:
                if (!(value instanceof Double)) {
                    throw new IllegalArgumentException("Value must be a Double");
                }
                break;
            case BOOLEAN:
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException("Value must be a Boolean");
                }
                break;
            case NUMBER:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Value must be a Number");
                }
                String strValue = (String) value;
                try {
                    double numberValue = Double.parseDouble(strValue);
                    if (numberValue < min || numberValue > max) {
                        throw new IllegalArgumentException("Value must be between " + min + " and " + max);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Value must be a valid number");
                }
                break;
            case ENUM:
                if (!(value instanceof Enum)) {
                    throw new IllegalArgumentException("Value must be an Enum");
                }
                if (enumClass == null) {
                    throw new IllegalArgumentException("Enum class must be provided for ENUM type");
                }
                if (!enumClass.isInstance(value)) {
                    throw new IllegalArgumentException("Value must be an instance of " + enumClass.getName());
                }
                break;
            case ITEM:
                if (!(value instanceof ItemStack)) {
                    throw new IllegalArgumentException("Value must be an ItemStack");
                }
                break;
        }
        this.value = value;
        return this;
    }

    public Object getValue() {
        if (value instanceof String) {
            return value.toString();
        } else if (value instanceof Enum<?> enumValue) {
            return enumValue.name();
        } else if (value instanceof ItemStack itemStack) {
            return itemStack.getType().name();
        }
        return value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PropertyType {
        STRING(Material.STRING),
        INT(Material.REDSTONE_TORCH),
        DOUBLE(Material.REPEATER),
        NUMBER(Material.IRON_INGOT),
        BOOLEAN(Material.TURTLE_SCUTE),
        ENUM(Material.BOOK),
        LOCATION(Material.ENDER_PEARL),
        ITEM(Material.ITEM_FRAME),
        NPC(Material.PLAYER_HEAD),
        REGION(Material.MAP),
        ACTION(Material.PISTON),
        ACTION_SETTING(Material.COMPARATOR),
        FUNCTION(Material.ACTIVATOR_RAIL),
        GROUP(Material.WHITE_BANNER),
        TEAM(Material.BEACON),
        LAYOUT(Material.CHAINMAIL_CHESTPLATE),
        MENU(Material.CHEST),
        CONDITION(Material.REDSTONE),
        SLOT(Material.ARMOR_STAND),
        PATH(Material.DIAMOND_AXE),
        STAT_INSTANCE(Material.BARRIER),
        ACTION_PROPERTIES(Material.DIAMOND_SWORD),
        COLOR(Material.PAINTING),
        CUSTOM(Material.CHEST_MINECART),
        SOUND(Material.NOTE_BLOCK);

        private final Material icon;
    }

    public ItemBuilder getDisplayItem() {
        String value = switch (type) {
            case ITEM -> ((ItemStack) this.value).getType().name();
            case ACTION -> ((List<?>) this.value).size() + " actions";
            case CONDITION -> ((List<?>) this.value).size() + " conditions";
            default -> this.value.toString();
        };

        ItemBuilder builder = new ItemBuilder()
                .material(type.icon)
                .name("<yellow>" + name)
                .description(description)
                .info("<b><yellow>Current value</b>", "")
                .info(null, "<green>" + value);
        if (type == PropertyType.BOOLEAN) {
            builder.lClick(ItemBuilder.ActionType.TOGGLE_YELLOW);
        } else {
            builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        }
        if (rClick != null) {
            builder.rClick(rClick);
        }
        if (mClick != null) {
            builder.mClick(mClick);
        }

        return builder;
    }

    public ActionProperty showIf(boolean condition) {
        this.visible = condition;
        return this;
    }

    public ActionProperty mClick(ItemBuilder.ActionType action) {
        this.mClick = action;
        return this;
    }

    public ActionProperty rClick(ItemBuilder.ActionType action) {
        this.rClick = action;
        return this;
    }

    public static class StatProperties implements ActionProperties {
        private final List<StatInstance> statInstances = new ArrayList<>();

        public void setStatInstances(List<StatInstance> statInstances) {
            this.statInstances.clear();
            this.statInstances.addAll(statInstances);
        }

        @Override
        public List<ActionProperty> actions() {
            List<ActionProperty> properties = new ArrayList<>();
            for (int i = 0; i < statInstances.size(); i++) {
                StatInstance instance = statInstances.get(i);
                int finalI = i;
                properties.add(new ActionProperty(
                        "mode",
                        "Mode " + (i + 1),
                        "The mode of the stat to modify.",
                        ActionProperty.PropertyType.CUSTOM,
                        (house, menu, player) -> modeConsumer(house, menu, player, instance, finalI)
                ).setValue(instance).rClick((i == 0) ? null : ItemBuilder.ActionType.REMOVE_YELLOW));

                properties.add(new ActionProperty(
                        "value",
                        "Value " + (i + 1),
                        "The value of the stat to modify.",
                        ActionProperty.PropertyType.CUSTOM,
                        (house, menu, player) -> valueConsumer(house, menu, player, instance, finalI)
                ).setValue(instance).rClick((i == 0) ? null : ItemBuilder.ActionType.REMOVE_YELLOW).mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION));
            }

            return properties;
        }

        private BiFunction<InventoryClickEvent, Object, Boolean> modeConsumer(HousingWorld house, Menu backMenu, Player player, StatInstance instance, int i) {
            return (event, obj) -> {
                if (event.getClick() == ClickType.RIGHT && i > 0) {
                    statInstances.remove(instance);
                    backMenu.open();
                    return true;
                }

                if (event.getClick() != ClickType.LEFT) return false;

                List<Duple<StatOperation, ItemBuilder>> modes = new ArrayList<>();
                for (StatOperation mode : StatOperation.values()) {
                    if (mode.expressionOnly()) continue;
                    modes.add(new Duple<>(mode, ItemBuilder.create(mode.getMaterial()).name("&a" + mode)));
                }
                new PaginationMenu<>(Main.getInstance(), "&eSelect a mode", modes, backMenu.getOwner(), house, backMenu, (mode) -> {
                    instance.mode = mode;
                    backMenu.open();
                }).open();

                return true;
            };
        }

        private BiFunction<InventoryClickEvent, Object, Boolean> valueConsumer(HousingWorld house, Menu backMenu, Player player, StatInstance instance, int i) {
            return (event, obj) -> {
                if (event.getClick() == ClickType.MIDDLE) {
                    instance.value.setExpression(!instance.value.isExpression());
                    backMenu.open();
                    return true;
                }

                if (event.getClick() == ClickType.RIGHT && i > 0) {
                    statInstances.remove(instance);
                    backMenu.open();
                    return true;
                }

                if (event.getClick() != ClickType.LEFT) return false;

                if (instance.value.isExpression()) {
                    new ActionEditMenu(instance.value, Main.getInstance(), backMenu.getOwner(), house, backMenu).open();
                } else {
                    backMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                    backMenu.openChat(Main.getInstance(), instance.value.getLiteralValue(), (value) -> {
                        instance.value.setLiteralValue(value);
                        backMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), backMenu::open, 1L);
                    });
                }
                return true;
            };
        }
    }

    public interface ActionProperties {
        List<ActionProperty> actions();
    }

    public interface ActionPropertyConsumer {
        BiFunction<InventoryClickEvent, Object, Boolean> accept(HousingWorld house, Menu menu, Player player);
    }
}
