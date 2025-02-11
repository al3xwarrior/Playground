package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class KillPlayerAction extends HTSLImpl {

    public KillPlayerAction() {
        super("Kill Player Action");
    }

    @Override
    public String toString() {
        return "KillPlayerAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&eKill Player");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&aKill Player");
        builder.description("Kills the player");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.setHealth(0.0);
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "kill";
    }
}
