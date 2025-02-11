package com.al3x.housing2.Action;

import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionEditor {
    private int rows = 4;
    private String title = "Action Settings";
    private List<ActionItem> items = new ArrayList<>();

    public ActionEditor() {
    }

    public ActionEditor(int rows, String title, List<ActionItem> items) {
        this.rows = rows;
        this.title = title;
        this.items = items;
    }

    public ActionEditor(int rows, String title, ActionItem... items) {
        this.rows = rows;
        this.title = title;
        this.items = Arrays.asList(items);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ActionItem> getItems() {
        return items;
    }

    public void setItems(List<ActionItem> items) {
        this.items = items;
    }

    public static class ActionItem {
        private ItemBuilder builder;
        private ActionType type;
        private int slot = -1;
        private double min = 0;
        private double max = Double.MAX_VALUE;
        private Enum[] enumClass;
        private Material enumMaterial;
        private String varName;
        private BiFunction<InventoryClickEvent, Object, Boolean> customRunnable;

        public ActionItem(String varName, ItemBuilder builder, ActionType type) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;

            if (type == ActionType.ENUM) {
                Bukkit.getLogger().warning(varName + " is an enum type, but no enum class was provided.");
            }
        }

        public ActionItem(String varName, ItemBuilder builder, ActionType type, BiFunction<InventoryClickEvent, Object, Boolean> runnable) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;
            this.customRunnable = runnable;

            if (type == ActionType.ENUM) {
                Bukkit.getLogger().warning(varName + " is an enum type, but no enum class was provided.");
            }
        }

        public ActionItem(ItemBuilder builder, ActionType type, int slot, BiFunction<InventoryClickEvent, Object, Boolean> consumer) {
            this.builder = builder;
            this.type = type;
            this.slot = slot;
            this.customRunnable = consumer;

            if (type != ActionType.CUSTOM) {
                Bukkit.getLogger().warning("Slot provided for non-custom action item.");
            }
        }

        public ActionItem(String varName, ItemBuilder builder, ActionType type, double min, double max) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;
            this.min = min;
            this.max = max;

            if (type != ActionType.DOUBLE && type != ActionType.INT) {
                Bukkit.getLogger().warning(varName + " is not a number type, but min and max values were provided.");
            }
        }

        public ActionItem(String varName, ItemBuilder builder, ActionType type, Enum[] enumClass, Material enumMaterial) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;
            this.enumClass = enumClass;
            this.enumMaterial = enumMaterial;
        }

        public ActionItem(String varName, ItemBuilder builder, ActionType type, Enum[] enumClass, Material enumMaterial, BiFunction<InventoryClickEvent, Object, Boolean> customRunnable) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;
            this.enumClass = enumClass;
            this.enumMaterial = enumMaterial;
            this.customRunnable = customRunnable;
        }

        public ActionItem(String varName, ItemBuilder builder, BiFunction<InventoryClickEvent, Object, Boolean> customRunnable) {
            this.varName = varName;
            this.builder = builder;
            this.customRunnable = customRunnable;
        }

        public ItemBuilder getBuilder() {
            return builder;
        }

        public ActionType getType() {
            return type;
        }

        public Enum[] getEnumClass() {
            return enumClass;
        }

        public int getSlot() {
            return slot;
        }

        public Material getEnumMaterial() {
            return enumMaterial;
        }

        public String getVarName() {
            return varName;
        }

        public BiFunction<InventoryClickEvent, Object, Boolean> getCustomRunnable() {
            return customRunnable;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public void setCustomRunnable(BiFunction<InventoryClickEvent, Object, Boolean> customRunnable) {
            this.customRunnable = customRunnable;
        }

        public void setType(ActionType type) {
            this.type = type;
        }

        public void setBuilder(ItemBuilder builder) {
            this.builder = builder;
        }

        public enum ActionType {
            STRING, INT, DOUBLE, BOOLEAN, ENUM, ITEM, NPC, REGION, ACTION, ACTION_SETTING, FUNCTION, GROUP, TEAM, LAYOUT, MENU, CONDITION, CUSTOM
        }
    }


}
