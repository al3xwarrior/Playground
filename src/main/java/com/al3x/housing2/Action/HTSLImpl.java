package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EnumHTSLAlternative;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class HTSLImpl extends Action {
    public HTSLImpl(String name) {
        super(name);
    }

    public abstract String keyword();

    public String syntax() {
        HashMap<String, Object> actionData = data();
        StringBuilder builder = new StringBuilder();
        List<String> keys = List.copyOf(actionData.keySet());
        for (String key : keys) {
            builder.append("<").append(key).append(">");
            if (actionData.get(key) != keys.getLast()) builder.append(" ");
        }
        return keyword() + (!builder.isEmpty() ? " " + builder : "");
    }

    public String export() {
        return export(0);
    }

    public String export(int indent) {
        LinkedHashMap<String, Object> actionData = data();
        StringBuilder builder = new StringBuilder();
        List<String> keys = List.copyOf(actionData.keySet());
        for (String key : keys) {
            Object value = actionData.get(key);
            switch (value) {
                case String s -> {
                    if (s.contains(" ")) builder.append("\"").append(value).append("\"");
                    else builder.append(value);
                }

                case Integer i -> builder.append(i);
                case Boolean b -> builder.append(b);
                case Double d -> builder.append(d);

                case Enum<?> e -> {
                    if (e instanceof EnumHTSLAlternative) {
                        builder.append(((EnumHTSLAlternative) e).getAlternative());
                    } else {
                        builder.append(e.name());
                    }
                }
                case null, default -> {
                }
            }
            if (actionData.get(key) != keys.getLast()) builder.append(" ");
        }
        return " ".repeat(indent) + keyword() + (!builder.isEmpty() ? " " + builder : "");
    }

    public void importAction (String action) {
        String[] parts = action.split(" ");
        HashMap<String, Object> actionData = data();
        List<String> keys = List.copyOf(actionData.keySet());
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("\"")) {
                StringBuilder builder = new StringBuilder();
                while (!part.endsWith("\"")) {
                    builder.append(part).append(" ");
                    i++;
                    part = parts[i];
                }
                builder.append(part);
                actionData.put(keys.get(i - 1), builder.toString());
            } else {
                actionData.put(keys.get(i - 1), part);
            }
        }
    }
}
