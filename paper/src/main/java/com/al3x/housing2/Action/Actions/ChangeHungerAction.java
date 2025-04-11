package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@Getter
public class ChangeHungerAction extends HTSLImpl {

    public ChangeHungerAction() {
        super(
                "change_hunger_action",
                "Change Hunger Level",
                "Changes the player's hunger level.",
                Material.COOKED_BEEF,
                List.of("hunger")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "mode",
                        "Mode",
                        "The mode to use.", 
                        StatOperation.class
                ).setValue(StatOperation.SET),
                new NumberProperty(
                        "value",
                        "Value",
                        "The value to use."
                ).setValue("20")
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Double val = getProperty("value", NumberProperty.class).parsedValue(house, player);
        StatOperation mode = getValue("mode", StatOperation.class);
        switch (mode) {
            case INCREASE:
                val += player.getFoodLevel();
                break;
            case DECREASE:
                val = player.getFoodLevel() - val;
                break;
            case MULTIPLY:
                val = player.getFoodLevel() * val;
                break;
            case DIVIDE:
                val = player.getFoodLevel() / val;
                break;
            case MOD:
                val = player.getFoodLevel() % val;
                break;
            case FLOOR:
                val = Math.floor(player.getFoodLevel());
                break;
            case ROUND:
                break;
            case SET:
                break;
        }
        player.setFoodLevel(val.intValue());
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
