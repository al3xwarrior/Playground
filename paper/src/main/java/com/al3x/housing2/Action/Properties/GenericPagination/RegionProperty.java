package com.al3x.housing2.Action.Properties.GenericPagination;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Properties.PaginationProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class RegionProperty extends PaginationProperty<Region> {

    public RegionProperty(String id, String name, String description) {
        super(id, name, description, Material.GRASS_BLOCK);
    }


    @Override
    protected List<Region> getItems(HousingWorld house) {
        return house.getRegions();
    }

    @Override
    protected ItemBuilder buildItem(Region item) {
        return ItemBuilder.create(Material.PAPER)
                .name(item.getName())
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW);
    }

    @Override
    protected void onSelect(Region item, Player player) {
//        player.sendMessage("§aSet " + getName() + " to: " + item.getName());
    }
}
