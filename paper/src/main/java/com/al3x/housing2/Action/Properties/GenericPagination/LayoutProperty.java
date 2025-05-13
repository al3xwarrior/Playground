package com.al3x.housing2.Action.Properties.GenericPagination;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Properties.PaginationProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class LayoutProperty extends PaginationProperty<Layout> {

    public LayoutProperty(String id, String name, String description) {
        super(id, name, description, Material.CHAINMAIL_CHESTPLATE);
    }

    @Override
    protected List<Layout> getItems(HousingWorld house) {
        return house.getLayouts();
    }

    @Override
    protected ItemBuilder buildItem(Layout item) {
        return ItemBuilder.create(Material.PAPER)
                .name(item.getName())
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW);
    }

    @Override
    protected void onSelect(Layout item, Player player) {
//        player.sendMessage(colorize("&aSet " + getName() + " to: " + item.getName()));
    }
}
