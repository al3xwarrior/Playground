package com.al3x.housing2.Utils;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//Thanks IxNoah and ChatGippity
public class HypixelLoreFormatter {
    private static final Pattern HYPX_LORE_PUNCTUATION = Pattern.compile("(.*)[!.?]");
    private static final int MAX_LENGTH = 28;

    /**
     * Generates Hypixel lores with faithful formatting guidelines, all fact-checked using an auto-wrapped part of lore in Housing.
     * - Wraps using the same word-by-word system as Hypixel
     * - Adds punctuation automatically if needed like Hypixel
     * <p>
     * - Todo: Add support for custom colours
     * - Todo: Add support for custom wrapping lengths
     * - Todo: Add support for custom punctuation
     *
     * @param description    The string to add wrapping to
     * @param info   Additional info to add to the middle
     *               (e.g. "Settings:", "Message: Hello World!")
     * @param labels Additional labels for the bottom (e.g. "Left Click to edit!")
     * @return The formatted lines with colour codes
     */
    public static List<String> hypixelLore(String description, List<Duple<String, Object>> info, List<String> labels, boolean punctuation) {
        List<String> lines = new ArrayList<>();

        for (String str : description.split("\n")) {
            if (description.isEmpty()) {
                continue;
            }
            if (str.isEmpty()) {
                lines.add("");
                continue;
            }

            if (!HYPX_LORE_PUNCTUATION.matcher(str).matches() && punctuation) {
                str += ".";
            }

            StringBuilder line = new StringBuilder();
            for (String word : str.split(" ")) {
                line.append(word).append(" ");

                if (line.length() > MAX_LENGTH) {
                    lines.add(line.toString().trim());
                    line.setLength(0);
                }
            }
            if (!line.isEmpty()) {
                lines.add(line.toString().trim());
            }

            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, "§7" + lines.get(i));
            }
        }

        if (info != null && !info.isEmpty()) {
            lines.add("");
            for (Duple<String, Object> key : info) {
                if (key.getFirst() == null) {
                    String[] split = key.getSecond().toString().split("\n");
                    for (String s : split) {
                        lines.add("§f" + s);
                    }
                    continue;
                }
                switch (key.getSecond()) {
                    case String s -> lines.add("§f" + key.getFirst() + ": " + s);
                    case Integer i -> lines.add("§f" + key.getFirst() + ": " + i);
                    case Double v -> lines.add("§f" + key.getFirst() + ": " + v);
                    case null -> lines.add("§f" + key.getFirst());
                    default -> lines.add("§f" + key.getFirst() + ": " + key.getSecond());
                }
            }
        }

        if (labels != null && !labels.isEmpty()) {
            lines.add("");
            lines.addAll(labels);
        }

        return lines;
    }
}