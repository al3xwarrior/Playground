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
import java.util.List;

public class ToggleSitAction extends HTSLImpl implements NPCAction {

    public ToggleSitAction() {
        super(
                ActionEnum.TOGGLE_SIT,
                "Toggle Sit",
                "Toggles if the player is sitting down.",
                Material.SPRUCE_STAIRS,
                List.of("sit")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        SeatManager seatManager = house.getPlugin().getSeatManager();
        seatManager.toggleSit(player);

        return OutputType.SUCCESS;
    }
    @Override
    public boolean requiresPlayer() {
        return true;
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
