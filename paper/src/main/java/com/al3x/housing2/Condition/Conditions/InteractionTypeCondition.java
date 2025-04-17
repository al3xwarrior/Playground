package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class InteractionTypeCondition extends CHTSLImpl {

    public InteractionTypeCondition() {
        super(ConditionEnum.INTERACTION_TYPE,
                "Interaction Type",
                "If the player must attack or interact with an npc.",
                Material.OAK_BUTTON,
                List.of("interactionType")
        );

        getProperties().add(
                new BooleanProperty("attack",
                        "Attack",
                        "If the player must attack the npc."
                ).setValue(false)
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        boolean attack = getValue("attack", Boolean.class);
        if (event.cancellable() instanceof EntityDamageByEntityEvent) {
            return attack ? OutputType.TRUE : OutputType.FALSE;
        } else if (event.cancellable() instanceof PlayerInteractEntityEvent) {
            return !attack ? OutputType.TRUE : OutputType.FALSE;
        } else {
            return OutputType.FALSE;
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_DAMAGE, EventType.PLAYER_ATTACK);
    }
}
