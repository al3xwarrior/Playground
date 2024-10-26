package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.CommandRegistry;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Listeners.HouseEvents.AttackEvent;
import com.al3x.housing2.Listeners.HouseEvents.ChangeHeldItem;
import com.al3x.housing2.Listeners.HouseEvents.ChatEvent;
import kotlin.Function;
import kotlin.sequences.Sequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
I just vomited opening this file. It's a mess.
- Sin_ender

 */
public class HandlePlaceholders {
    public static class Placeholder {
        String placeholder;
        String displayName;
        TriFunction<Player, HousingWorld, MatchResult, String> runnable;

        public Placeholder(String placeholder, String displayName, TriFunction<Player, HousingWorld, MatchResult, String> runnable) {
            this.placeholder = placeholder;
            this.displayName = displayName;
            this.runnable = runnable;
        }

        public Placeholder(String placeholder, TriFunction<Player, HousingWorld, MatchResult, String> runnable) {
            this.placeholder = placeholder;
            this.displayName = placeholder;
            this.runnable = runnable;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public String run(Player player, HousingWorld house, MatchResult match) {
            return runnable.apply(player, house, match);
        }
    }

    private static List<Placeholder> placeholders = new ArrayList<>();

    public static void registerPlaceholder(String placeholder, TriFunction<Player, HousingWorld, MatchResult, String> runnable) {
        placeholders.add(new Placeholder(placeholder, runnable));
    }

    public static void registerPlaceholder(String placeholder, BiFunction<Player, HousingWorld, String> runnable) {
        placeholders.add(new Placeholder(placeholder, (player, house, match) -> runnable.apply(player, house)));
    }

    public static void registerPlaceholder(String placeholder, String displayName, TriFunction<Player, HousingWorld, MatchResult, String> runnable) {
        placeholders.add(new Placeholder(placeholder, displayName, runnable));
    }

    public static List<Placeholder> getPlaceholders() {
        List<Placeholder> list = new ArrayList<>(placeholders);
        return list;
    }

    public static void registerPlaceholders() {
        registerPlaceholder("%%player%%", (player, house) -> {
            //Pjma wanted this so I added it
            return player.getName();
        });
        registerPlaceholder("%player.name%", (player, house) -> player.getName());
        registerPlaceholder("%player.displayname%", (player, house) -> player.getDisplayName());
        registerPlaceholder("%player.ping%", (player, house) -> String.valueOf(player.getPing()));
        registerPlaceholder("%player.isSprinting%", (player, house) -> String.valueOf(player.isSprinting()));
        registerPlaceholder("%player.location.x%", (player, house) -> String.valueOf(player.getLocation().getX()));
        registerPlaceholder("%player.location.y%", (player, house) -> String.valueOf(player.getLocation().getY()));
        registerPlaceholder("%player.location.z%", (player, house) -> String.valueOf(player.getLocation().getZ()));
        registerPlaceholder("%player.location.pitch%", (player, house) -> String.valueOf(player.getLocation().getPitch()));
        registerPlaceholder("%player.location.yaw%", (player, house) -> String.valueOf(player.getLocation().getYaw()));

        // Regex for capturing stat placeholders like %stat.player/<stat>%
        registerPlaceholder("regex:%stat\\.player/([a-zA-Z0-9_]+)%", "&6%stat.player/&7[stat]&6%", (player, house, match) -> {
            String statName = match.getGroups().get(1).getValue(); // The captured <stat>
            Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
            return (stat == null) ? "0" : stat.formatValue();
        });

        // Event Data per player
        registerPlaceholder("%event.chat/message%", (player, house) -> {
            if (ChatEvent.lastChatEvent.containsKey(player.getUniqueId())) {
                AsyncPlayerChatEvent event = ChatEvent.lastChatEvent.get(player.getUniqueId());
                return event.getMessage();
            }
            return "";
        });

        registerPlaceholder("%event.attack/attacker%", (player, house) -> {
            if (AttackEvent.lastAttacked.containsKey(player.getUniqueId())) return player.getName();
            return "";
        });
        registerPlaceholder("%event.attack/victim%", (player, house) -> {
            if (AttackEvent.lastAttacked.containsKey(player.getUniqueId())) {
                Entity entity = AttackEvent.lastAttacked.get(player.getUniqueId());
                return entity.getName();
            }
            return "";
        });
        registerPlaceholder("%event.attack/victim.type%", (player, house) -> {
            if (AttackEvent.lastAttacked.containsKey(player.getUniqueId())) {
                Entity entity = AttackEvent.lastAttacked.get(player.getUniqueId());
                return (entity.hasMetadata("NPC")) ? "NPC" : entity.getType().name();
            }
            return "";
        });

        // ChangeHeldItem event data per player (this is a lot of placeholders :P)
        registerPlaceholder("%event.change/previousSlot%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                return String.valueOf(event.getPreviousSlot());
            }
            return "";
        });
        registerPlaceholder("%event.change/newSlot%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                return String.valueOf(event.getNewSlot());
            }
            return "";
        });
        registerPlaceholder("%event.change/previousItem%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
                return (previousItem != null && previousItem.hasItemMeta()) ? previousItem.getItemMeta().getDisplayName() : "null";
            }
            return "";
        });
        registerPlaceholder("%event.change/newItem%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
                return (newItem != null && newItem.hasItemMeta()) ? newItem.getItemMeta().getDisplayName() : "null";
            }
            return "";
        });
        registerPlaceholder("%event.change/previousItem.type%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
                return (previousItem == null) ? "null" : previousItem.getType().name();
            }
            return "";
        });
        registerPlaceholder("%event.change/newItem.type%", (player, house) -> {
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
                return (newItem == null) ? "null" : newItem.getType().name();
            }
            return "";
        });

        // House placeholders
        registerPlaceholder("%%house%%", (player, house) -> house.getName());
        registerPlaceholder("%house.name%", (player, house) -> house.getName());
        registerPlaceholder("%house.cookies%", (player, house) -> String.valueOf(house.getCookies()));
        registerPlaceholder("%house.guests%", (player, house) -> String.valueOf(house.getGuests()));

        // Misc
        registerPlaceholder("%unix.time%", (player, house) -> String.valueOf(System.currentTimeMillis() / 1000));
        registerPlaceholder("%time.unix%", (player, house) -> String.valueOf(System.currentTimeMillis() / 1000));
        registerPlaceholder("%unix.date%", (player, house) -> String.valueOf(System.currentTimeMillis()));
        registerPlaceholder("%date.unix%", (player, house) -> String.valueOf(System.currentTimeMillis()));
        registerPlaceholder("%server.tps%", (player, house) -> new DecimalFormat("#.##").format(Bukkit.getServerTickManager().getTickRate()));

        // Regex for capturing stat placeholders like %stat.global/<stat>%
        registerPlaceholder("regex:%stat\\.global/([a-zA-Z0-9_]+)%", "&6%stat.global/&7[stat]&6%", (player, house, match) -> {
            String statName = match.getGroupValues().get(1); // The captured <stat>
            Stat stat = house.getStatManager().getGlobalStatByName(statName);
            return (stat == null) ? "0" : stat.formatValue();
        });

        // Regex for command args placeholders like %command.args/<index>%
        registerPlaceholder("regex:%command\\.args/([0-9]+)%", "&6%command.args/&7[index 0-∞]&6%", (player, house, match) -> {
            int index = Integer.parseInt(match.getGroups().get(1).getValue());
            if (CommandRegistry.commandArgsResults.containsKey(player.getUniqueId())) {
                List<String> args = CommandRegistry.commandArgsResults.get(player.getUniqueId());
                if (args.size() > index) {
                    return args.get(index);
                }
            }
            return "";
        });

        registerPlaceholder("regex:%round/(.+) ([0-9]+)%", "&6%round/&7[placeholder] [places]&6%", (player, house, match) -> {
            String value = parsePlaceholders(player, house, match.getGroups().get(1).getValue());
            if (NumberUtilsKt.isDouble(value)) {
                double val = Double.parseDouble(value);
                int places = Integer.parseInt(match.getGroups().get(2).getValue());
                if (places == 0) {//If the places is 0, round the value to the nearest whole number
                    return String.valueOf((int) Math.round(val));
                }

                //Round the value to the specified number of decimal places
                String returning =  String.valueOf(Math.round(val * Math.pow(10, places)) / Math.pow(10, places));
                //If the value is a decimal, and the decimal is not the same as the places, add 0's to the end
                if (returning.contains(".") && returning.split("\\.")[1].length() < places) {
                    returning += "0".repeat(places - returning.split("\\.")[1].length());
                }
                return returning;
            }
            return value;// If the value is not a number, return the original value
        });

        //random number between 2 numbers
        registerPlaceholder("regex:%random.int/([0-9]+) ([0-9]+)%", "&6%random.int/&7[min] [max]&6%", (player, house, match) -> {
            int min = Integer.parseInt(match.getGroups().get(1).getValue());
            int max = Integer.parseInt(match.getGroups().get(2).getValue());
            return String.valueOf((int) (Math.random() * (max - min + 1) + min));
        });
        registerPlaceholder("regex:%random.double/([0-9.]+) ([0-9.]+)%", "&6%random.double/&7[min] [max]&6%", (player, house, match) -> {
            double min = Integer.parseInt(match.getGroups().get(1).getValue());
            double max = Integer.parseInt(match.getGroups().get(2).getValue());
            return String.valueOf(Math.random() * (max - min) + min);
        });
    }


    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        StringBuilder result = new StringBuilder(s);

        for (Placeholder placeholder : placeholders) {
            if (placeholder.getPlaceholder().startsWith("regex:")) {
                String regex = placeholder.getPlaceholder().substring(6);
                Regex pattern = new Regex(regex);
                MatchResult match = pattern.find(result.toString(), 0);
                while (match != null) {
                    replaceAll(result, match.getValue(), placeholder.run(player, house, match));
                    match = match.next();
                }
            } else {
                replaceAll(result, placeholder.getPlaceholder(), placeholder.run(player, house, null));
            }
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