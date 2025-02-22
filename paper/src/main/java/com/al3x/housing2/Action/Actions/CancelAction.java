package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Enums.EventType.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class CancelAction extends HTSLImpl implements NPCAction {

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
        builder.name("&eCancel Event");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.changeOrderLore(true);
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT);
        builder.name("&aCancel Event");
        builder.description("Cancels the event from running.");
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
        return Arrays.asList(FISH_CAUGHT, PLAYER_ENTER_PORTAL, PLAYER_BLOCK_BREAK,
                PLAYER_BLOCK_PLACE, PLAYER_DROP_ITEM, PLAYER_DEATH, PLAYER_KILL,
                PLAYER_PICKUP_ITEM, PLAYER_TOGGLE_FLIGHT, PLAYER_CHAT, PLAYER_DAMAGE,
                PLAYER_ATTACK, PLAYER_CHANGE_HELD_ITEM, PLAYER_JUMP, PLAYER_SWAP_TO_OFFHAND);
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
        return "cancelEvent";
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        event.setCancelled(true);
    }
}
