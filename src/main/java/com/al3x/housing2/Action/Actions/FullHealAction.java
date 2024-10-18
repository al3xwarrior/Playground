package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class FullHealAction extends Action {

    public FullHealAction() {
        super("Full Heal Action");
    }

    @Override
    public String toString() {
        return "FullHealAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GOLDEN_APPLE);
        builder.name("&eFull Heal");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GOLDEN_APPLE);
        builder.name("&aFull Heal Event");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.setHealth(player.getMaxHealth());
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
