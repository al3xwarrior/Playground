package com.al3x.housing2.Condition;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public abstract class Condition extends Action {
    public Condition(String id, String name, String description, Material icon, List<String> scriptingKeywords) {
        super(id, name, description, icon, scriptingKeywords);
    }

    public abstract OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor);

    public Condition clone() {
        Condition condition;
        ConditionEnum conditionEnum = ConditionEnum.getConditionByName(getName());
        if (conditionEnum == null) {
            return null;
        }
        condition = conditionEnum.getConditionInstance(data);
        return condition;
    }
}
