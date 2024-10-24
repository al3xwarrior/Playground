package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Listeners.HouseEvents.AttackEvent;
import com.al3x.housing2.Listeners.HouseEvents.ChatEvent;
import kotlin.sequences.Sequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.DecimalFormat;
import java.util.Iterator;
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

            // Regex for capturing stat placeholders like %stat.player/<stat>%
            Regex statPattern = new Regex("%stat\\.player/([a-zA-Z0-9_]+)%");
            MatchResult playerMatch = statPattern.find(result.toString(), 0);
            while (playerMatch != null) {
                String statName = playerMatch.getGroups().get(1).getValue(); // The captured <stat>
                Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
                String replacement = (stat == null) ? "0" : stat.formatValue();
                replaceAll(result, playerMatch.getValue(), replacement);
                playerMatch = playerMatch.next();
            }

            // Event Data per player
            if (ChatEvent.lastChatEvent.containsKey(player.getUniqueId())) {
                AsyncPlayerChatEvent event = ChatEvent.lastChatEvent.get(player.getUniqueId());
                replaceAll(result, "%event.chat/message%", event.getMessage());
            }

            if (AttackEvent.lastAttacked.containsKey(player.getUniqueId())) {
                Entity entity = AttackEvent.lastAttacked.get(player.getUniqueId());
                replaceAll(result, "%event.attack/attacker%", player.getName());
                replaceAll(result, "%event.attack/victim%", entity.getName());
                replaceAll(result, "%event.attack/victim.type%", (entity.hasMetadata("NPC")) ? "NPC" : entity.getType().name());
            }
        }

        // House placeholders
        replaceAll(result, "%%house%%", house.getName()); //Added this one just because :)
        replaceAll(result, "%house.name%", house.getName());
        replaceAll(result, "%house.cookies%", String.valueOf(house.getCookies()));
        replaceAll(result, "%house.guests%", String.valueOf(house.getGuests()));

        // Misc
        replaceAll(result, "%unix.time%", String.valueOf(System.currentTimeMillis() / 1000));

        // Regex for capturing stat placeholders like %stat.global/<stat>%
        Regex globalPattern = new Regex("%stat\\.global/([a-zA-Z0-9_]+)%");
        MatchResult globalMatch = globalPattern.find(result.toString(), 0);
        while (globalMatch != null) {
            String statName = globalMatch.getGroups().get(1).getValue(); // The captured <stat>
            Stat stat = house.getStatManager().getGlobalStatByName(statName);
            String replacement = (stat == null) ? "0" : stat.formatValue();
            replaceAll(result, globalMatch.getValue(), replacement);
            globalMatch = globalMatch.next();
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