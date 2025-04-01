package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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

    private double health = 20.0;
    private StatOperation operation = StatOperation.INCREASE;
    private boolean healOnChange = true;

    public ChangeMaxHealthAction() {
        super(
                "change_max_health_action",
                "Change Max Health",
                "Adjusts the player's maximum health.",
                Material.DANDELION,
                List.of("maxHealth")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "health",
                        "Health",
                        "The amount to change the max health by.",
                        ActionProperty.PropertyType.DOUBLE
                ),
                new ActionProperty(
                        "operation",
                        "Operation",
                        "The operation to use.",
                        ActionProperty.PropertyType.ENUM,
                        StatOperation.class
                ),
                new ActionProperty(
                        "healOnChange",
                        "Heal On Change",
                        "Should the player be fully healed when this action is ran",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("health", health);
        data.put("operation", operation);
        data.put("healOnChange", healOnChange);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
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