package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Layout;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class LayoutData {
    private String name;
    private String icon;
    private String description;
    private List<String> inventory;
    private List<String> hotbar;
    private List<String> armor;
    private String offhand;

    public LayoutData() {

    }

    public LayoutData(String name, String icon, String description, List<String> inventory, List<String> hotbar, List<String> armor, String offhand) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.armor = armor;
        this.offhand = offhand;
    }

    public static List<LayoutData> fromList(List<Layout> layouts) {
        List<LayoutData> layoutDatas = new ArrayList<>();
        for (Layout layout : layouts) {
            layoutDatas.add(layout.toData());
        }
        return layoutDatas;
    }

    public static List<Layout> toList(List<LayoutData> layoutDatas) {
        List<Layout> layouts = new ArrayList<>();
        for (LayoutData layoutData : layoutDatas) {
            layouts.add(new Layout(
                layoutData.getName(),
                layoutData.getIcon(),
                layoutData.getDescription(),
                layoutData.getInventory(),
                layoutData.getHotbar(),
                layoutData.getArmor(),
                layoutData.getOffhand()
            ));
        }
        return layouts;
    }
}