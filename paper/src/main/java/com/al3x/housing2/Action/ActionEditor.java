package com.al3x.housing2.Action;

import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

@Setter
@Getter
public class ActionEditor {
    private int rows = 4;
    private String title = "Action Settings";
    private List<ActionProperty> properties = new ArrayList<>();

    public ActionEditor() {
    }

    public ActionEditor(int rows, String title, List<ActionProperty> properties) {
        this.rows = rows;
        this.title = title;
        this.properties = properties;
    }

    public ActionEditor(int rows, String title, ActionProperty... properties) {
        this.rows = rows;
        this.title = title;
        this.properties = Arrays.asList(properties);
    }
}
