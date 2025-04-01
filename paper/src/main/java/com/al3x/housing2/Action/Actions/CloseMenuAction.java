package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class CloseMenuAction extends HTSLImpl {

    public CloseMenuAction() {
        super(
                "close_menu_action",
                "Close Menu",
                "Closes the player's currently open menu.",
                Material.CHEST,
                List.of("closeMenu")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.closeInventory();
        return OutputType.SUCCESS;
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
