package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CustomMenuData {
    private String title;
    private int rows;
    private int refreshRate;
    private List<CustomMenuItem> items;

    public CustomMenuData() {
    }

    public CustomMenuData(String title, int rows, int refreshRate, List<CustomMenuItem> items) {
        this.title = title;
        this.rows = rows;
        this.refreshRate = refreshRate;
        this.items = items;
    }

    public static List<CustomMenu> toList(List<CustomMenuData> customMenuData, HousingWorld house) {
        List<CustomMenu> list = new ArrayList<>();
        for (CustomMenuData data : customMenuData) {
            list.add(new CustomMenu(data, house));
        }
        return list;
    }

    public static List<CustomMenuData> fromList(List<CustomMenu> customMenus) {
        List<CustomMenuData> list = new ArrayList<>();
        for (CustomMenu menu : customMenus) {
            list.add(menu.toData());
        }
        return list;
    }

    @Setter
    @Getter
    public static class CustomMenuItem {
        private String item;
        private List<ActionData> actions;

        public CustomMenuItem() {}

        public CustomMenuItem(String item, List<ActionData> actions) {
            this.item = item;
            this.actions = actions;
        }

    }
}