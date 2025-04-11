package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.CustomMenuData;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.Serialization;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomMenu {
    String title;
    int rows;
    int refreshRate;
    ArrayList<Duple<ItemStack, List<Action>>> items;

    public CustomMenu(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.refreshRate = 20;
        this.items = new ArrayList<>();

        for (int i = 0; i < rows * 9; i++) {
            items.add(null);
        }

        items.set(0, new Duple<>(new ItemStack(Material.DIAMOND), new ArrayList<>()));
    }

    public CustomMenu(CustomMenuData data) {
        this.title = data.getTitle();
        this.rows = data.getRows();
        this.refreshRate = data.getRefreshRate();
        this.items = new ArrayList<>(data.getItems().stream().map(item -> {
            if (item == null) return null;
            try {
                return new Duple<>(Serialization.itemStackFromBase64(item.getItem()), ActionData.toList(item.getActions()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).toList());
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItem(int slot, ItemStack item, List<Action> action) {
        items.set(slot, new Duple<>(item, action));
    }

    public ArrayList<Duple<ItemStack, List<Action>>> getItems() {
        return items;
    }

    public int getRows() {
        return rows;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        if (refreshRate < 1) {
            refreshRate = 1;
        }
        this.refreshRate = refreshRate;
    }

    public void setRows(int rows) {
        this.rows = rows;

        if (rows * 9 < items.size()) {
            items = new ArrayList<>(items.subList(0, rows * 9));
        } else {
            for (int i = items.size(); i < rows * 9; i++) {
                items.add(null);
            }
        }
    }

    public CustomMenuData toData() {
        return new CustomMenuData(title, rows, refreshRate, items.stream().map(item -> {
            if (item == null) return null;
            return new CustomMenuData.CustomMenuItem(Serialization.itemStackToBase64(item.getFirst()), ActionData.fromList(item.getSecond()));
        }).toList());
    }
}
