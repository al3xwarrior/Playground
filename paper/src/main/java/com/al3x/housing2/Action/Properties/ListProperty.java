package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

//Shows a list of whatever in the builder
public class ListProperty<V> extends CustomSlotProperty<List<V>> {
    public ListProperty(String id, String name, String description, Material icon, int slot) {
        super(id, name, description, icon, slot);
    }

    @Override
    public ItemBuilder getDisplayItem() {
        ItemBuilder builder = getBuilder().clone();
        builder.info("<yellow>Current value", "");
        for (V v : getValue()) {
            builder.info(null, "<green>" + v.toString());
        }
        return builder;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {

    }
}
