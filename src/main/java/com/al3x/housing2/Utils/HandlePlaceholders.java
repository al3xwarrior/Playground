package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
I just vomited opening this file. It's a mess.
- Sin_ender

 */
public class HandlePlaceholders {

    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        StringBuilder result = new StringBuilder(s);

        // Player placeholders
        if (player != null) {
            replaceAll(result, "%%player%%", player.getName()); //Pjma wanted this so I added it
            replaceAll(result, "%player.name%", player.getName());
            replaceAll(result, "%player.displayname%", player.getDisplayName());
            replaceAll(result, "%player.ping%", String.valueOf(player.getPing()));
            replaceAll(result, "%player.isSprinting%", String.valueOf(player.isSprinting()));
            replaceAll(result, "%player.location.x%", String.valueOf(player.getLocation().getX()));
            replaceAll(result, "%player.location.y%", String.valueOf(player.getLocation().getY()));
            replaceAll(result, "%player.location.z%", String.valueOf(player.getLocation().getZ()));
            replaceAll(result, "%player.location.pitch%", String.valueOf(player.getLocation().getPitch()));
            replaceAll(result, "%player.location.yaw%", String.valueOf(player.getLocation().getYaw()));
        }

        // House placeholders
        replaceAll(result, "%%house%%", house.getName()); //Added this one just because :)
        replaceAll(result, "%house.name%", house.getName());
        replaceAll(result, "%house.cookies%", String.valueOf(house.getCookies()));
        replaceAll(result, "%house.guests%", String.valueOf(house.getGuests()));

        // Misc
        replaceAll(result, "%unix.time%", String.valueOf(System.currentTimeMillis() / 1000));

        // Regex for capturing stat placeholders like %stat.player/<stat>%
        Pattern statPattern = Pattern.compile("%stat\\.player/([a-zA-Z0-9_]+)%");
        Matcher statMatcher = statPattern.matcher(result);
        while (statMatcher.find()) {
            String statName = statMatcher.group(1); // The captured <stat>
            Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            String replacement = (stat == null) ? "0" : df.format(stat.getStatNum());
            replaceAll(result, statMatcher.group(), replacement);
        }

        // Regex for capturing stat placeholders like %stat.global/<stat>%
        Pattern globalStatPattern = Pattern.compile("%stat\\.global/([a-zA-Z0-9_]+)%");
        Matcher globalStatMatcher = globalStatPattern.matcher(result);
        while (globalStatMatcher.find()) {
            String statName = globalStatMatcher.group(1); // The captured <stat>
            Stat stat = house.getStatManager().getGlobalStatByName(statName);
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            String replacement = (stat == null) ? "0" : df.format(stat.getStatNum());
            replaceAll(result, globalStatMatcher.group(), replacement);
        }

        return result.toString();
    }

    private static void replaceAll(StringBuilder builder, String target, String replacement) {
        int start = 0;
        while ((start = builder.indexOf(target, start)) != -1) {
            builder.replace(start, start + target.length(), replacement);
            start += replacement.length();
        }
    }
}