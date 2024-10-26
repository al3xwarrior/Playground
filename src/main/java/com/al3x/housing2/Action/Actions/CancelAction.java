package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Enums.EventType.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class CancelAction extends Action {

    public CancelAction() {
        super("Cancel Action");
    }

    @Override
    public String toString() {
        return "CancelAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT);
        builder.name("&eCancel Action");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.changeOrderLore(true);
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT);
        builder.name("&aCancel Event");
        builder.description("Apply this action to cancel this event.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return true;
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        event.setCancelled(true);
        return true;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(FISH_CAUGHT, PLAYER_ENTER_PORTAL, PLAYER_BLOCK_BREAK, PLAYER_BLOCK_PLACE, PLAYER_DROP_ITEM, PLAYER_PICKUP_ITEM, PLAYER_TOGGLE_FLIGHT, PLAYER_CHAT, PLAYER_DAMAGE, PLAYER_ATTACK, PLAYER_CHANGE_HELD_ITEM);
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
