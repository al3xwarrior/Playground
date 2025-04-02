package com.al3x.housing2.Action;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

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

        if (type != PropertyType.DOUBLE && type != PropertyType.INT) {
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

    @Getter
    @RequiredArgsConstructor
    public enum PropertyType {
        STRING(Material.STRING),
        INT(Material.REDSTONE_TORCH),
        DOUBLE(Material.REPEATER),
        BOOLEAN(Material.TURTLE_SCUTE),
        ENUM(Material.BOOK),
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
        CUSTOM(Material.CHEST_MINECART);

        private final Material icon;
    }

    public ItemBuilder getDisplayItem() {
        return new ItemBuilder()
                .material(type.icon)
                .name("<yellow>" + name)
                .description(description)
                .info("<gray>Current value", value.toString())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
    }

    public ActionProperty showIf(boolean condition) {
        this.visible = condition;
        return this;
    }

    public interface ActionPropertyConsumer {
        BiFunction<InventoryClickEvent, Object, Boolean> accept(HousingWorld house, Menu menu, Player player);
    }
}
