package com.al3x.housing2.Action.Properties.GenericPagination;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Properties.PaginationProperty;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
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

public class FunctionProperty extends PaginationProperty<Function> {

    public FunctionProperty(String id, String name, String description) {
        super(id, name, description, Material.ACTIVATOR_RAIL);
    }


    @Override
    protected List<Function> getItems(HousingWorld house) {
        return house.getFunctions();
    }

    @Override
    protected ItemBuilder buildItem(Function function) {
        return ItemBuilder.create(function.getMaterial())
                .name(function.getName())
                .description(function.getDescription())
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW);
    }

    @Override
    protected void onSelect(Function function, Player player) {
//        player.sendMessage(colorize("&aSet " + getName() + " to: " + function.getName()));
    }
}
