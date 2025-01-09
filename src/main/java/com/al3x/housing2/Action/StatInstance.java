package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingData.MoreStatData;
import com.google.gson.Gson;

import java.util.LinkedHashMap;

public class StatInstance {
    private static final Gson gson = new Gson();
    public StatOperation mode;
    public StatValue value;

    public StatInstance(boolean global) {
        this.mode = StatOperation.INCREASE;
        this.value = new StatValue(global);
    }

    public void fromData(LinkedHashMap<String, Object> data, Class<? extends StatInstance> actionClass) {
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = gson.fromJson(gson.toJson(data.get("value")), MoreStatData.class).toStatValue();
    }
}
