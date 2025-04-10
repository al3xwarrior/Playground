package com.al3x.housing2.Action;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ActionEditor {
    private int rows = 4;
    private String title = "Action Settings";
    private List<ActionProperty<?>> properties = new ArrayList<>();

    public ActionEditor() {
    }

    public ActionEditor(int rows, String title, List<ActionProperty<?>> properties) {
        this.rows = rows;
        this.title = title;
        this.properties = properties.stream().filter(ActionProperty::isVisible).collect(Collectors.toList());
    }

    public ActionEditor(int rows, String title, ActionProperty<?>... properties) {
        this.rows = rows;
        this.title = title;
        this.properties = Arrays.stream(properties).filter(ActionProperty::isVisible).collect(Collectors.toList());;
    }
}
