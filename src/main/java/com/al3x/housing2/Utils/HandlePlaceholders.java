package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlePlaceholders {

    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        // Basic placeholders
        s = s
                .replaceAll("%player.name%", player.getName())
                .replaceAll("%player.displayname%", player.getDisplayName())
                .replaceAll("%player.ping%", String.valueOf(player.getPing()))
                .replaceAll("%player.isSprinting%", String.valueOf(player.isSprinting()))
                .replaceAll("%player.location.x%", String.valueOf(player.getLocation().getX()))
                .replaceAll("%player.location.y%", String.valueOf(player.getLocation().getY()))
                .replaceAll("%player.location.z%", String.valueOf(player.getLocation().getZ()))
                .replaceAll("%player.location.pitch%", String.valueOf(player.getLocation().getPitch()))
                .replaceAll("%player.location.yaw%", String.valueOf(player.getLocation().getYaw()))


                .replaceAll("%house.name%", house.getName())
                .replaceAll("%house.cookies%", String.valueOf(house.getCookies()))
                .replaceAll("%house.guests%", String.valueOf(house.getGuests()));

        // Regex for capturing stat placeholders like %stat.player/<stat>%
        Pattern statPattern = Pattern.compile("%stat\\.player/([a-zA-Z0-9_]+)%");
        Matcher statMatcher = statPattern.matcher(s);

        // Process each found stat placeholder
        while (statMatcher.find()) {
            String statName = statMatcher.group(1); // The captured <stat>
            Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
            if (stat == null) {
                s = s.replace(statMatcher.group(), "0"); // Handle null case if stat not found
            } else {
                s = s.replace(statMatcher.group(), String.valueOf(stat.getStatNum())); // Replace the placeholder with the actual stat value
            }

        }

        return s;
    }
}