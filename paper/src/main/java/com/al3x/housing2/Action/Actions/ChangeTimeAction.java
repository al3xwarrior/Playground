package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class ChangeTimeAction extends HTSLImpl {

    public ChangeTimeAction() {
        super(
                ActionEnum.CHANGE_TIME,
                "Change Time",
                "Changes the time in the house.",
                Material.CLOCK,
                List.of("time")
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
                ).setValue("6000")
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Double value = getProperty("value", NumberProperty.class).parsedValue(house, player);

        switch (getValue("mode", StatOperation.class)) {
            case INCREASE:
                value += house.getWorld().getTime();
                break;
            case DECREASE:
                value = house.getWorld().getTime() - value;
                break;
            case MULTIPLY:
                value = house.getWorld().getTime() * value;
                break;
            case DIVIDE:
                value = house.getWorld().getTime() / value;
                break;
            case MOD:
                value = house.getWorld().getTime() % value;
                break;
            case FLOOR:
                value = Math.floor(house.getWorld().getTime());
                break;
            case SET:
                break;
        }

        house.getWorld().setTime(value.longValue());
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
