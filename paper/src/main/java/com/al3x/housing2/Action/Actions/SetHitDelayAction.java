package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@ToString
@Getter
@Setter
public class SetHitDelayAction extends HTSLImpl implements NPCAction {

    private double delay = 10;

    public SetHitDelayAction() {
        super(
                "set_hit_delay_action",
                "Set Hit Delay",
                "Sets the hit delay for the player.",
                Material.IRON_SWORD,
                List.of("hitDelay")
        );

        getProperties().add(
                new ActionProperty(
                        "delay",
                        "Delay",
                        "The delay in ticks.",
                        ActionProperty.PropertyType.DOUBLE, 0.0, 100.0
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {

        player.setMaximumNoDamageTicks(NumberUtilsKt.toInt(delay));

        //This is not super simple to use lol :)
        player.setNoDamageTicks(NumberUtilsKt.toInt(delay));

        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            le.setMaximumNoDamageTicks(NumberUtilsKt.toInt(delay));
            le.setNoDamageTicks(NumberUtilsKt.toInt(delay));
        }
    }
}
