package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EnumHTSLAlternative;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Material;

import java.lang.reflect.Field;
import java.util.*;

public abstract class HTSLImpl extends Action {
    public HTSLImpl(String id, String name, String description, Material icon, List<String> scriptingKeywords) {
        super(id, name, description, icon, scriptingKeywords);
    }

    public String syntax() {
        HashMap<String, Object> actionData = data();
        StringBuilder builder = new StringBuilder();
        List<String> keys = List.copyOf(actionData.keySet());
        for (String key : keys) {
            builder.append("<").append(key).append(">");
            if (actionData.get(key) != keys.getLast()) builder.append(" ");
        }
        return getScriptingKeywords().getFirst() + (!builder.isEmpty() ? " " + builder : "");
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
        String output = builder.toString();
        if (output.endsWith(" ")) output = output.substring(0, output.length() - 1);
        return " ".repeat(indent) + getId() + (!output.isEmpty() ? " " + output: "");
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
            if (part.isEmpty()) {
                i++;
                continue;
            }
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
            if (actionData.get(key) == null) {
                System.out.println("Key " + key + " is null");
                continue;
            }
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

    public static Duple<String[], String> handleArg(String[] args, int index) {
        if (args.length <= index) return null;
        String arg = args[index];
        if (arg.startsWith("\"")) {
            StringBuilder builder = new StringBuilder();
            builder.append(arg);
            while (!arg.endsWith("\"")) {
                index++;
                if (args.length <= index) break;
                arg = args[index];
                builder.append(" ").append(arg);
            }

            args = Arrays.copyOfRange(args, index + 1, args.length);

            return new Duple<>(args, builder.toString().replace("\"", ""));
        }
        return new Duple<>(Arrays.copyOfRange(args, index + 1, args.length), arg);
    }
}
