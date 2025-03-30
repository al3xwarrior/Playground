package com.al3x.housing2.Action;

import lombok.Getter;

public class ActionProperty {

    @Getter private final String id;
    @Getter private String displayName;
    @Getter private ActionEditor.ActionItem.ActionType type;
    @Getter private Object value;

    ActionProperty(String id) {
        this.id = id;
        this.displayName = id;
        this.type = ActionEditor.ActionItem.ActionType.STRING;
    }

    public ActionProperty displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ActionProperty type(ActionEditor.ActionItem.ActionType type) {
        this.type = type;
        return this;
    }

    public ActionProperty value(Object value) {
        this.value = value;
        return this;
    }

}
