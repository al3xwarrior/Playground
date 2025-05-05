package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Action.OutputType.SUCCESS;
import static com.al3x.housing2.Enums.EventType.*;

@ToString
public class CancelAction extends HTSLImpl implements NPCAction {

    public CancelAction() {
        super(
                ActionEnum.CANCEL,
                "Cancel",
                "Cancels the event from running.",
                Material.TNT,
                List.of("cancel")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return SUCCESS;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event) {
        event.setCancelled(true);
        return SUCCESS;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(FISH_CAUGHT, PLAYER_ENTER_PORTAL, PLAYER_BLOCK_BREAK,
                PLAYER_BLOCK_PLACE, PLAYER_DROP_ITEM, PLAYER_DEATH, PLAYER_KILL,
                PLAYER_PICKUP_ITEM, PLAYER_TOGGLE_FLIGHT, PLAYER_CHAT, PLAYER_DAMAGE,
                PLAYER_ATTACK, PLAYER_CHANGE_HELD_ITEM, PLAYER_JUMP, PLAYER_SWAP_TO_OFFHAND,
                PLAYER_CREATE_VOICE_GROUP, PLAYER_JOIN_VOICE_GROUP, SPLASH_POTION);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        event.setCancelled(true);
    }
}
