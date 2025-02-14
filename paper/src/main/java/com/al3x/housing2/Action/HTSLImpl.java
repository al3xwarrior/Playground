package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EnumHTSLAlternative;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

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

    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        LinkedHashMap<String, Object> actionData = data();
        List<String> keys = List.copyOf(actionData.keySet());
        int i = 0;
        StringBuilder current = new StringBuilder();
        int keyI = 0;
        while (i < parts.length) {
            String part = parts[i];
            if (part.startsWith("\"")) {
                keyI++;
                current.append(part);
                if (part.endsWith("\"")) {
                    actionData.put(keys.get(keyI - 1), current.toString().replace("\"", ""));
                    current = new StringBuilder();
                }
            } else {
                if (part.endsWith("\"")) {
                    current.append(" ").append(part);
                    actionData.put(keys.get(keyI - 1), current.toString().replace("\"", ""));
                    current = new StringBuilder();
                    i++;
                    continue;
                }

                if (!current.isEmpty()) {
                    current.append(" ").append(part);
                } else {
                    keyI++;
                    actionData.put(keys.get(keyI - 1), part);
                }
            }
            i++;
        }

        for (String key : actionData.keySet()) {
            String value = actionData.get(key).toString();

            if (NumberUtilsKt.isInt(value)) {
                actionData.put(key, Integer.parseInt(value));
            } else if (NumberUtilsKt.isDouble(value)) {
                actionData.put(key, Double.parseDouble(value));
            } else if (value.equals("true") || value.equals("false")) {
                actionData.put(key, Boolean.parseBoolean(value));
            }
        }

        for (String key : actionData.keySet()) {
            try {
                Field field = this.getClass().getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType().isEnum() && actionData.get(key) != null) {
                    if (actionData.get(key) instanceof String) {
                        field.set(this, Enum.valueOf((Class<Enum>) field.getType(), (String) actionData.get(key)));
                        continue;
                    } else if (actionData.get(key) instanceof Enum) {
                        field.set(this, actionData.get(key));
                        continue;
                    }
                }
                field.set(this, actionData.get(key));
            } catch (Exception ignored) {
            }
        }
        return nextLines;
    }
}
