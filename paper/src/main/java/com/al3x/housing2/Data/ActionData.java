package com.al3x.housing2.Data;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActionData {
    private String action;
    private HashMap<String, Object> data;
    private String comment;

    public ActionData() {
        //used for serialization
    }

    public ActionData(String action, HashMap<String, Object> data, String comment) {
        this.action = action;
        this.data = data;
        this.comment = comment;
    }

    public static HashMap<String, List<ActionData>> fromHashMap(HashMap<EventType, List<Action>> actionMap, HousingWorld house) {
        HashMap<String, List<ActionData>> map = new HashMap<>();
        for (Map.Entry<EventType, List<Action>> entry : actionMap.entrySet()) {
            List<ActionData> list = entry.getValue().stream()
                    .map(action -> new ActionData(action.getName(), action.data(), action.getComment()))
                    .collect(Collectors.toList());
            map.put(entry.getKey().name(), list);
        }
        return map;
    }

    public static HashMap<String, List<ActionData>> fromHashMap(HashMap<String, List<Action>> actionMap) {
        HashMap<String, List<ActionData>> map = new HashMap<>();
        for (Map.Entry<String, List<Action>> entry : actionMap.entrySet()) {
            List<ActionData> list = entry.getValue().stream()
                    .map(action -> new ActionData(action.getName(), action.data(), action.getComment()))
                    .collect(Collectors.toList());
            map.put(entry.getKey(), list);
        }
        return map;
    }

    public static List<ActionData> fromList(List<Action> actionList) {
        return actionList.stream()
                .map(action -> new ActionData(action.getName(), action.data(), action.getComment()))
                .collect(Collectors.toList());
    }

    public static List<Action> toList(List<ActionData> actionDataList) {
        List<Action> collect = new ArrayList<>();
        for (ActionData data : actionDataList) {
            ActionEnum actionEnum = ActionEnum.getActionByName(data.getAction());
            if (actionEnum == null) {
                continue; //skip invalid actions, rather than freaking out
            }
            collect.add(actionEnum.getActionInstance(data.getData(), data.getComment()));
        }
        return collect;
    }

    public static ActionData toData(Action action) {
        return new ActionData(action.getName(), action.data(), action.getComment());
    }

    public static Action fromData(ActionData data) {
        ActionEnum actionEnum = ActionEnum.getActionByName(data.getAction());
        if (actionEnum == null) {
            throw new IllegalArgumentException("Action " + data.getAction() + " does not exist");
        }
        return actionEnum.getActionInstance(data.getData(), data.getComment());
    }
}