package com.al3x.housing2.Action;

import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

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
        private double min = Double.MIN_VALUE;
        private double max = Double.MAX_VALUE;
        private Enum[] enumClass;
        private Material enumMaterial;
        private String varName;
        private Runnable customRunnable;

        public ActionItem(String varName, ItemBuilder builder, ActionType type) {
            this.varName = varName;
            this.builder = builder;
            this.type = type;

            if (type == ActionType.ENUM) {
                Bukkit.getLogger().warning(varName + " is an enum type, but no enum class was provided.");
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

        public ActionItem(String varName, ItemBuilder builder, Runnable customRunnable) {
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

        public Material getEnumMaterial() {
            return enumMaterial;
        }

        public String getVarName() {
            return varName;
        }

        public Runnable getCustomRunnable() {
            return customRunnable;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public void setCustomRunnable(Runnable customRunnable) {
            this.customRunnable = customRunnable;
        }

        public void setType(ActionType type) {
            this.type = type;
        }

        public void setBuilder(ItemBuilder builder) {
            this.builder = builder;
        }

        public enum ActionType {
            STRING, INT, DOUBLE, BOOLEAN, ENUM, ITEM, ACTION
        }

        public interface EnumInterface {
            ItemBuilder getDisplayItem();

        }
    }


}
