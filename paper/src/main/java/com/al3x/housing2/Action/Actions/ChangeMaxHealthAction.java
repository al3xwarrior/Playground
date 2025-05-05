package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.DoubleProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ScaledMaxHealthTrait;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class ChangeMaxHealthAction extends HTSLImpl implements NPCAction {
    public ChangeMaxHealthAction() {
        super(
                ActionEnum.CHANGE_MAX_HEALTH,
                "Change Max Health",
                "Adjusts the player's maximum health.",
                Material.DANDELION,
                List.of("maxHealth")
        );

        getProperties().addAll(List.of(
                new DoubleProperty(
                        "health",
                        "Health",
                        "The amount to change the max health by."
                ).setValue(20.0),
                new EnumProperty<>(
                        "operation",
                        "Operation",
                        "The operation to use.",
                        StatOperation.class
                ).setValue(StatOperation.INCREASE),
                new BooleanProperty(
                        "healOnChange",
                        "Heal On Change",
                        "Should the player be fully healed when this action is ran"
                ).setValue(true)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        double health = getValue("health", Double.class);
        StatOperation operation = getValue("operation", StatOperation.class);
        boolean healOnChange = getValue("healOnChange", Boolean.class);
        if (player != null) {
            double currentMax = player.getMaxHealth();

            switch (operation) {
                case INCREASE:
                    currentMax += health;
                    break;
                case DECREASE:
                    currentMax -= health;
                    break;
                case SET:
                    currentMax = health;
                    break;
            }

            // Ensure CurrentMax is a value between 1 and 2000
            currentMax = Math.min(2000, Math.max(1, currentMax));
            player.setMaxHealth(currentMax);
            if (healOnChange) {
                player.setHealth(currentMax);
            }
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        double health = getValue("health", Double.class);
        StatOperation operation = getValue("operation", StatOperation.class);
        boolean healOnChange = getValue("healOnChange", Boolean.class);
        if (npc != null) {
            double currentMax = npc.getOrAddTrait(ScaledMaxHealthTrait.class).getMaxHealth();

            switch (operation) {
                case INCREASE:
                    currentMax += health;
                    break;
                case DECREASE:
                    currentMax -= health;
                    break;
                case SET:
                    currentMax = health;
                    break;
            }

            // Ensure CurrentMax is a value between 1 and 2000
            currentMax = Math.min(2000, Math.max(1, currentMax));
            npc.getOrAddTrait(ScaledMaxHealthTrait.class).setMaxHealth(currentMax);
            if (healOnChange && npc.getEntity() instanceof LivingEntity le) {
                le.setHealth(currentMax);
            }
        }
    }
}