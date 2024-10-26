package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Enums.EventType.*;

public class ExitAction extends Action {

    public ExitAction() {
        super("Exit Action");
    }

    @Override
    public String toString() {
        return "ExitAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.BEDROCK);
        builder.name("&eExit");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.BEDROCK);
        builder.name("&aExit");
        builder.description("Stops executing the remaining actions.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }

    @Override
    public int limit() {
        return 1;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
