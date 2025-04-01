package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
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
    @Setter
    private StatOperation mode = StatOperation.SET;
    private String value = "6000";

    public ChangeTimeAction() {
        super(
                "change_time_action",
                "Change Time",
                "Changes the time in the house.",
                Material.CLOCK,
                List.of("time")
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

        switch (mode) {
            case INCREASE:
                result += house.getWorld().getTime();
                break;
            case DECREASE:
                result = house.getWorld().getTime() - result;
                break;
            case MULTIPLY:
                result = house.getWorld().getTime() * result;
                break;
            case DIVIDE:
                result = house.getWorld().getTime() / result;
                break;
            case MOD:
                result = house.getWorld().getTime() % result;
                break;
            case FLOOR:
                result = Math.floor(house.getWorld().getTime());
                break;
            case ROUND:
                result = Math.round(house.getWorld().getTime());
                break;
            case SET:
                break;
        }

        house.getWorld().setTime((long) result);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = (String) data.get("value");
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + mode.getAlternative() + " " + Color.removeColor(value);
    }
}
