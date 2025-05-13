package com.al3x.housing2.Condition;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Condition extends Action.InternalAction {
    public Condition(ConditionEnum condition, String name, String description, Material icon, List<String> scriptingKeywords) {
        super(condition.getId(), name, description, icon, scriptingKeywords);

        getProperties().add(
                new BooleanProperty(
                        "inverted",
                        "Inverted",
                        "If true, the condition will be inverted."
                ).setValue(false)
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return null;
    }

    public abstract OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor);

    public Condition clone(HousingWorld house) {
        Condition condition;
        ConditionEnum conditionEnum = ConditionEnum.getConditionById(getId());
        if (conditionEnum == null) {
            return null;
        }
        condition = conditionEnum.getConditionInstance(data(), house);
        return condition;
    }
}
