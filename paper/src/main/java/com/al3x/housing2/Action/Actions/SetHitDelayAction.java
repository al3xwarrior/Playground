package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.DoubleProperty;
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
    public SetHitDelayAction() {
        super(
                ActionEnum.SET_HITDELAY,
                "Set Hit Delay",
                "Sets the hit delay for the player.",
                Material.IRON_SWORD,
                List.of("hitDelay")
        );

        getProperties().add(
                new DoubleProperty(
                        "delay",
                        "Delay",
                        "The delay in ticks.",
                        0.0, 100.0
                ).setValue(10.0)
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {

        player.setMaximumNoDamageTicks(NumberUtilsKt.toInt(getValue("delay", DoubleProperty.class).getValue()));

        //This is not super simple to use lol :)
        player.setNoDamageTicks(NumberUtilsKt.toInt(getValue("delay", DoubleProperty.class).getValue()));

        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            le.setMaximumNoDamageTicks(NumberUtilsKt.toInt(getValue("delay", DoubleProperty.class).getValue()));
            le.setNoDamageTicks(NumberUtilsKt.toInt(getValue("delay", DoubleProperty.class).getValue()));
        }
    }
}
