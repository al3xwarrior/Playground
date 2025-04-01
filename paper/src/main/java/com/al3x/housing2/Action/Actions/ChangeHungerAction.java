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
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@Getter
public class ChangeHungerAction extends HTSLImpl {

    @Setter
    private StatOperation mode;
    private String value;

    public ChangeHungerAction() {
        super(
                "hunger",
                "Change Hunger Level",
                "Changes the player's hunger level.",
                Material.COOKED_BEEF
        );

        mode = StatOperation.SET;
        value = "20";

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
        int val = Integer.parseInt(value);
        player.setFoodLevel(val);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = (String) data.get("value");
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + getId() + " " + mode.getAlternative() + " " + Color.removeColor(value);
    }
}
