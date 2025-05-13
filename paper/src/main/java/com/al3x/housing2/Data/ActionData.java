package com.al3x.housing2.Data;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
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
                    .map(action -> new ActionData(action.getId(), action.data(), action.getComment()))
                    .collect(Collectors.toList());
            map.put(entry.getKey().name(), list);
        }
        return map;
    }

    public static HashMap<String, List<ActionData>> fromHashMap(HashMap<String, List<Action>> actionMap) {
        HashMap<String, List<ActionData>> map = new HashMap<>();
        for (Map.Entry<String, List<Action>> entry : actionMap.entrySet()) {
            List<ActionData> list = entry.getValue().stream()
                    .map(action -> new ActionData(action.getId(), action.data(), action.getComment()))
                    .collect(Collectors.toList());
            map.put(entry.getKey(), list);
        }
        return map;
    }

    public static List<ActionData> fromList(List<Action> actionList) {
        return actionList.stream()
                .map(action -> new ActionData(action.getId(), action.data(), action.getComment()))
                .collect(Collectors.toList());
    }

    public static List<Action> toList(List<ActionData> actionDataList, HousingWorld house) {
        List<Action> collect = new ArrayList<>();
        for (ActionData data : actionDataList) {
            ActionEnum actionEnum = ActionEnum.getActionById(data.getAction());
            if (actionEnum == null) {
                Main.getInstance().getLogger().warning("Action " + data.action + " not found");
                continue; //skip invalid actions, rather than freaking out
            }
            collect.add(actionEnum.getActionInstance(data.getData(), data.getComment(), house));
        }
        return collect;
    }

    public static ActionData toData(Action action) {
        if (action == null) {
            return null;
        }
        return new ActionData(action.getName(), action.data(), action.getComment());
    }

    public static Action fromData(ActionData data, HousingWorld house) {
        if (data == null) {
            return null;
        }
        ActionEnum actionEnum = ActionEnum.getActionById(data.getAction());
        if (actionEnum == null) {
            Main.getInstance().getLogger().warning("Action " + data.action + " not found");
            return null; //skip invalid actions, rather than freaking out
        }
        return actionEnum.getActionInstance(data.getData(), data.getComment(), house);
    }
}