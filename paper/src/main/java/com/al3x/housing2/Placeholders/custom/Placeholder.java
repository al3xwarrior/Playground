package com.al3x.housing2.Placeholders.custom;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.placeholders.*;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Placeholder {
    public static final Logger log = LoggerFactory.getLogger(Placeholder.class);
    public static List<Placeholder> placeholders = new ArrayList<>();
    public static List<String> categories = new ArrayList<>();

    public Placeholder() {
        placeholders.add(this);
    }

    public static void registerPlaceholders() {
        new PlayerName();
        new PlayerDisplayname();
        new PlayerLocation();
        new PlayerMisc();
        new StatPlayer();

        new Group();

        new ChatEvent();
        new AttackEvent();
        new ChangeSlot();
        new SplashPotionEvent();

        new Raycast();

        new Touching();
        new House();
        new Region();

        new Misc();
        new Command();

        new MathPlaceholders();

        new Distance();
        new RemoveFormatting();

        new Npc();
        new FormattedStat();

        new Voice();

        for (Placeholder p : placeholders) {
            if (p.getPlaceholder() == null || p.getPlaceholder().isEmpty()) return;
            String category = p.getPlaceholder().replaceAll("%", "").split("/")[0].split("\\.")[0];
            if (!categories.contains(category)) categories.add(category);
        }
    }

    public List<String> getAliases() {
        return new ArrayList<>();
    }

    public abstract String getPlaceholder();

    public boolean hasArgs() {
        return false;
    }

    public String getDisplayName() {
        return getPlaceholder();
    }

    public abstract String handlePlaceholder(String input, HousingWorld house, Player player);

    @Override
    public String toString() {
        return getPlaceholder();
    }

    private static boolean checkValid(Placeholder placeholder, String input) {
        String placeholderName;
        if (placeholder.hasArgs()) {
            placeholderName = input.split("/")[0];
        } else {
            placeholderName = input;
        }
        if (placeholderName.isEmpty()) {
            return false;
        }
        if (placeholder.getPlaceholder().startsWith(placeholderName)) {
            return true;
        }
        for (String alias : placeholder.getAliases()) {
            if (alias.startsWith(placeholderName)) {
                return true;
            }
        }
        return false;
    }

    public static String handlePlaceholders(String input, HousingWorld house, Player player) {
        return handlePlaceholders(input, house, player, false);
    }

    public static String handlePlaceholders(String input, HousingWorld house, Player player, boolean nested) {
        if (input == null) {
            return null;
        }
        if (nested) input = input.replace("[", "%").replace("]", "%");
        String[] split = input.split("%");
        Iterator<String> iterator = Arrays.asList(split).iterator();
        String part = "";
        while (iterator.hasNext()) {
            part = iterator.next();
            if (part.isEmpty()) {
                continue;
            }
            part = "%" + part + "%";
            for (Placeholder placeholder : placeholders) {
                if (!checkValid(placeholder, part)) {
                    continue;
                }
                String handled = placeholder.handlePlaceholder(part.replace("%", ""), house, player);
                if (handled != null) {
                    input = input.replace(part, handled);
                    break;
                }
            }

        }
        return input;
    }
}
