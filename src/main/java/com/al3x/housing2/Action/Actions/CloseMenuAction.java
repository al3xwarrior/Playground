package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class CloseMenuAction extends Action {

    public CloseMenuAction() {
        super("Close Menu Action");
    }

    @Override
    public String toString() {
        return "CloseMenuAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&eClose Menu");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&aClose Menu");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.closeInventory();
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }

    @Override
    public List<EventType> allowedEvents() {
        return List.of(EventType.INVENTORY_CLICK);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
