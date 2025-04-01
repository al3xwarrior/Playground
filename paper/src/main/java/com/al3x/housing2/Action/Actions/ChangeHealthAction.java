package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.NumberUtilsKt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class ChangeHealthAction extends HTSLImpl implements NPCAction {
    private StatOperation mode = StatOperation.SET;
    private String value = "20";

    public ChangeHealthAction() {
        super(
                "change_health_action",
                "Change Health",
                "Changes the player's health.",
                Material.APPLE,
                List.of("health")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "mode",
                        "Mode",
                        "The mode to use.",
                        ActionProperty.PropertyType.ENUM,
                        StatOperation.class
                ),
                new ActionProperty(
                        "value",
                        "Value",
                        "The value to use.",
                        ActionProperty.PropertyType.STRING
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value)) {
            return OutputType.ERROR;
        }
        double result = Double.parseDouble(value);

        player.setHealth(handleHealth(result, player));
        return OutputType.SUCCESS;
    }

    private Double handleHealth(double result, LivingEntity le) {
        switch (mode) {
            case INCREASE:
                result += le.getHealth();
                break;
            case DECREASE:
                result = le.getHealth() - result;
                break;
            case MULTIPLY:
                result = le.getHealth() * result;
                break;
            case DIVIDE:
                result = le.getHealth() / result;
                break;
            case MOD:
                result = le.getHealth() % result;
                break;
            case FLOOR:
                result = Math.floor(le.getHealth());
                break;
            case ROUND:
                result = Math.round(le.getHealth());
                break;
            case SET:
                break;
        }

        return result;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + mode.getAlternative() + " " + Color.removeColor(value.toString());
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value)) {
            return;
        }
        double result = Double.parseDouble(value);

        if (!(npc.getEntity() instanceof LivingEntity le)) {
            return;
        }

        le.setHealth(handleHealth(result, le));
    }
}
