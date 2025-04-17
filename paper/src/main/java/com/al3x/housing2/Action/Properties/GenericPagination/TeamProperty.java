package com.al3x.housing2.Action.Properties.GenericPagination;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Properties.PaginationProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Team;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class TeamProperty extends PaginationProperty<Team> {

    public TeamProperty(String id, String name, String description) {
        super(id, name, description, Material.BEACON);
    }

    @Override
    protected List<Team> getItems(HousingWorld house) {
        return house.getTeams();
    }

    @Override
    protected ItemBuilder buildItem(Team item) {
        return ItemBuilder.create(Material.PAPER)
                .name(item.getName())
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW);
    }

    @Override
    protected void onSelect(Team item, Player player) {
//        player.sendMessage("§aSet " + getName() + " to: " + item.getName());
    }
}
