package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IsAttackCooldownCondition extends Condition {
    public IsAttackCooldownCondition() {
        super("Is Attack Cooldown");
    }

    @Override
    public String toString() {
        return "IsAttackCooldownCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CLOCK);
        builder.name("&eIs Attack Cooldown");
        builder.description("Is the player on attack cooldown?");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CLOCK);
        builder.name("&eIs Attack Cooldown");
        builder.description("Is the player on attack cooldown?");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        return player.getAttackCooldown() != 1.0f;
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
    public boolean requiresPlayer() {
        return true;
    }
}
