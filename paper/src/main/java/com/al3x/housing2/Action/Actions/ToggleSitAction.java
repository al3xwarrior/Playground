package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.SeatManager;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

public class ToggleSitAction extends HTSLImpl implements NPCAction {

    public ToggleSitAction() {
        super("Toggle Sit Action");
    }

    @Override
    public String toString() {
        return "ToggleSitAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.SPRUCE_STAIRS);
        builder.name("&eToggle Sit Action");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.SPRUCE_STAIRS);
        builder.name("&aToggle Sit Action");
        builder.description("Toggles if the sit sitting down.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        SeatManager seatManager = house.getPlugin().getSeatManager();
        seatManager.toggleSit(player);

        return OutputType.SUCCESS;
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
        return "sit";
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        SeatManager seatManager = house.getPlugin().getSeatManager();

        if (npc.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) npc.getEntity();
            seatManager.toggleSit(entity);
        }
    }
}
