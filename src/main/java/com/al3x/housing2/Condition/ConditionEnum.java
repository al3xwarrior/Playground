package com.al3x.housing2.Condition;

import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.Conditions.*;
import com.al3x.housing2.Condition.Conditions.IsSneakingCondition;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ConditionEnum {
    IS_SNEAKING("Is Sneaking", IsSneakingCondition.class),
    IS_FLYING("Is Flying", IsFlyingCondition.class),
    PLACEHOLDER_REQUIREMENT("Placeholder Requirement", PlaceholderRequirement.class),
    ;
    
    private String name;
    private Class<? extends Condition> condition;

    ConditionEnum(String name, Class<? extends Condition> condition) {
        this.condition = condition;
        this.name = name;
    }

    public Class<? extends Condition> getCondition() {
        return condition;
    }

    public String getName() {
        return name;
    }

    public Condition getConditionInstance(HashMap<String, Object> data) {
        try {
            Condition condition = this.condition.getDeclaredConstructor().newInstance();
            condition.fromData(data, this.condition);
            return condition;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Condition getConditionInstance() {
        try {
            return this.condition.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConditionEnum getConditionByName(String name) {
        for (ConditionEnum condition : ConditionEnum.values()) {
            if (condition.name.equals(name)) {
                return condition;
            }
        }
        return null;
    }
}
