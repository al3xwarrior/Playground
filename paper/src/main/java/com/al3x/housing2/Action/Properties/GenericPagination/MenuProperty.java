package com.al3x.housing2.Action.Properties.GenericPagination;

import com.al3x.housing2.Action.Properties.PaginationProperty;
import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class MenuProperty extends PaginationProperty<CustomMenu> {

    public MenuProperty(String id, String name, String description) {
        super(id, name, description, Material.CHEST);
    }

    @Override
    protected List<CustomMenu> getItems(HousingWorld house) {
        return house.getCustomMenus();
    }

    @Override
    protected ItemBuilder buildItem(CustomMenu item) {
        return ItemBuilder.create(Material.PAPER)
                .name(item.getTitle())
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW);
    }

    @Override
    protected void onSelect(CustomMenu item, Player player) {
        player.sendMessage(colorize("&aSet " + getName() + " to: " + item.getTitle()));
    }
}
