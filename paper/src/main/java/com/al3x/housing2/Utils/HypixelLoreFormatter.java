package com.al3x.housing2.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//Thanks IxNoah and ChatGippity
public class HypixelLoreFormatter {
    private static final Pattern HYPX_LORE_PUNCTUATION = Pattern.compile("(.*)[!.?]");
    private static final int MAX_LENGTH = 28;

    public static final int LINE_LENGTH = 28;
    private static final Style nullStyle = Style.style()
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .build();

    public static List<Component> splitComponent(Component root, int maxWidth) {
        List<Component> result = new ArrayList<Component>();
        TextComponent textRoot = (TextComponent) root;

        List<TextComponent> orphanedComponents = unstackComponents(textRoot);
        Component building = Component.empty();
        orphanedComponents.addFirst(textRoot.children(List.of()));
        int currentLength = 0;
        for (TextComponent component : orphanedComponents) {
            if (currentLength + component.content().length() <= maxWidth) { // No issues with length
                building = building.append(component.content(component.content()));
                currentLength += component.content().length();
                continue;
            }
            List<String> words = new ArrayList<>();
            String[] content = component.content().split("(?<=\\s)");
            for (int i = 0; i < content.length; i++) {
                String word = content[i];
                if (word.length() + currentLength > maxWidth) {
                    building = building.append(component.content(String.join("", words))).compact();
                    result.add(building);

                    building = Component.empty().mergeStyle(component);
                    words.clear();
                    words.add(word);
                    currentLength = i == content.length - 1 ? word.length() : word.length() + 1;
                } else {
                    currentLength += word.length();
                    words.add(word);
                }
            }
            building = building.append(component.content(String.join("", words))).compact();
        }
        result.add(building.compact());

        return result;
    }

    private static List<TextComponent> unstackComponents(TextComponent root) {
        List<Component> children = root.children();
        List<TextComponent> orphanedComponents = new ArrayList<TextComponent>();
        for (Component child : children) {
            Style style = child.style();
            style = style.merge(root.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
            style = style.merge(nullStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET); // Set "not_set" to null
            child = child.style(style);
            orphanedComponents.add((TextComponent) child.children(List.of()));
            if (!child.children().isEmpty()) {
                orphanedComponents.addAll(unstackComponents((TextComponent) child));
            }
        }
        return orphanedComponents;
    }

    public static List<Component> hypixelLore(String root, List<Duple<String, Object>> info, List<String> labels, boolean punctuation, int maxLength) {
        List<Component> lines = new ArrayList<>();

        if (root != null && !root.isEmpty()) {
            for (String s : root.split("\n")) {
                if (s.isEmpty()) {
                    lines.add(Component.empty());
                    continue;
                }
                if (!HYPX_LORE_PUNCTUATION.matcher(s).matches() && punctuation) {
                    s += ".";
                }

                List<Component> split = splitComponent(StringUtilsKt.housingStringFormatter("§7" + s), maxLength);
                lines.addAll(split);
            }
        }


        if (info != null && !info.isEmpty()) {
            lines.add(Component.empty());
            for (Duple<String, Object> key : info) {
                if (key.getFirst() == null) {
                    String[] split = key.getSecond().toString().split("\n");
                    for (String s : split) {
                        lines.add(format("§f" + s));
                    }
                    continue;
                }
                switch (key.getSecond()) {
                    case String s -> lines.add(format("§f" + key.getFirst() + ": " + s));
                    case Integer i -> lines.add(format("§f" + key.getFirst() + ": " + i));
                    case Double v -> lines.add(format("§f" + key.getFirst() + ": " + v));
                    case null -> lines.add(format("§f" + key.getFirst()));
                    default -> lines.add(format("§f" + key.getFirst() + ": " + key.getSecond().toString()));
                }
            }
        }

        if (labels != null && !labels.isEmpty()) {
            if (!lines.isEmpty()) {
                lines.add(Component.empty());
            }
            lines.addAll(labels.stream().map(Component::text).toList());
        }

        return lines;
    }

    private static Component format(String str) {
        return StringUtilsKt.housingStringFormatter(str);
    }
}