package com.al3x.housing2.Data;

import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class ConditionalData {
    private String condition;
    private HashMap<String, Object> data;

    public ConditionalData() {

    }

    public ConditionalData(String condition, HashMap<String, Object> data) {
        this.condition = condition;
        this.data = data;
    }

    public static List<ConditionalData> fromList(List<Condition> conditions) {
        List<ConditionalData> list = new ArrayList<>();
        for (Condition condition : conditions) {
            list.add(new ConditionalData(condition.getName(), condition.data()));
        }
        return list;
    }

    public static List<Condition> toList(List<ConditionalData> conditionList) {
        List<Condition> list = new ArrayList<>();
        for (ConditionalData data : conditionList) {
            ConditionEnum conditionEnum = ConditionEnum.getConditionByName(data.getCondition());
            if (conditionEnum == null) {
                throw new IllegalArgumentException("Condition " + data.getCondition() + " does not exist");
            }
            list.add(conditionEnum.getConditionInstance(data.getData()));
        }
        return list;
    }
}