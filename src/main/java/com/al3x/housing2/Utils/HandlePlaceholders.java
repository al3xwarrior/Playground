package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.CommandRegistry;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Listeners.HouseEvents.AttackEvent;
import com.al3x.housing2.Listeners.HouseEvents.ChangeHeldItem;
import com.al3x.housing2.Listeners.HouseEvents.ChatEvent;
import com.al3x.housing2.Runnables;
import kotlin.Function;
import kotlin.sequences.Sequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.al3x.housing2.Utils.Color.removeColor;
import static java.lang.Math.max;
import static java.lang.Math.min;

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
            try {
                return runnable.apply(player, house, match);
            } catch (Exception e) {
                return "null";
            }
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
            return player.getName();
        });
        registerPlaceholder("%player.name%", (player, house) -> {
            return player.getName();
        });
        registerPlaceholder("%player.displayname%", (player, house) -> player.getDisplayName());
        registerPlaceholder("%player.ping%", (player, house) -> String.valueOf(player.getPing()));
        registerPlaceholder("%player.isSprinting%", (player, house) -> String.valueOf(player.isSprinting()));
        registerPlaceholder("%player.location.x%", (player, house) -> String.valueOf(player.getLocation().getX()));
        registerPlaceholder("%player.location.y%", (player, house) -> String.valueOf(player.getLocation().getY()));
        registerPlaceholder("%player.location.z%", (player, house) -> String.valueOf(player.getLocation().getZ()));
        registerPlaceholder("%player.location.pitch%", (player, house) -> String.valueOf(player.getLocation().getPitch()));
        registerPlaceholder("%player.location.yaw%", (player, house) -> String.valueOf(player.getLocation().getYaw()));
        registerPlaceholder("%player.health%", (player, house) -> String.valueOf(player.getHealth()));
        registerPlaceholder("%player.maxHealth%", (player, house) -> String.valueOf(player.getMaxHealth()));
        registerPlaceholder("%player.hunger%", (player, house) -> String.valueOf(player.getFoodLevel()));
        registerPlaceholder("%player.level%", (player, house) -> String.valueOf(player.getLevel()));

        // Group placeholders
        registerPlaceholder("%group.name%", (player, house) -> house.loadOrCreatePlayerData(player).getGroupInstance(house).getName());
        registerPlaceholder("%group.prefix%", (player, house) -> house.loadOrCreatePlayerData(player).getGroupInstance(house).getPrefix());
        registerPlaceholder("%group.color%", (player, house) -> house.loadOrCreatePlayerData(player).getGroupInstance(house).getColor());
        registerPlaceholder("%group.suffix%", (player, house) -> house.loadOrCreatePlayerData(player).getGroupInstance(house).getSuffix());
        registerPlaceholder("%group.priority%", (player, house) -> String.valueOf(house.loadOrCreatePlayerData(player).getGroupInstance(house).getPriority()));
        registerPlaceholder("%group.displayname%", (player, house) -> house.loadOrCreatePlayerData(player).getGroupInstance(house).getDisplayName());

        // Regex for capturing stat placeholders like %stat.player/<stat>%
        registerPlaceholder("regex:%stat\\.player/([a-zA-Z0-9_#]+)%", "&6%stat.player/&7[player_stat]&6%", (player, house, match) -> {
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


        // Raycast placeholders
        registerPlaceholder("regex:%raycast.block/([0-9]+)%", "&6%raycast.block/&7[range]&6%", (player, house, match) -> {
            int range = Integer.parseInt(match.getGroups().get(1).getValue());

            Location loc = player.getTargetBlock(null, range).getLocation();
            if (loc == null) return "null";
            return loc.getBlock().getType().name();
        });
        registerPlaceholder("regex:%raycast.block.x/([0-9]+)%", "&6%raycast.block.x/&7[range]&6%", (player, house, match) -> {
            int range = Integer.parseInt(match.getGroups().get(1).getValue());
            Location loc = player.getTargetBlock(null, range).getLocation();
            if (loc == null) return "null";
            return String.valueOf(loc.getBlockX());
        });
        registerPlaceholder("regex:%raycast.block.y/([0-9]+)%", "&6%raycast.block.y/&7[range]&6%", (player, house, match) -> {
            int range = Integer.parseInt(match.getGroups().get(1).getValue());
            Location loc = player.getTargetBlock(null, range).getLocation();
            if (loc == null) return "null";
            return String.valueOf(loc.getBlockY());
        });
        registerPlaceholder("regex:%raycast.block.z/([0-9]+)%", "&6%raycast.block.z/&7[range]&6%", (player, house, match) -> {
            int range = Integer.parseInt(match.getGroups().get(1).getValue());
            Location loc = player.getTargetBlock(null, range).getLocation();
            if (loc == null) return "null";
            return String.valueOf(loc.getBlockZ());
        });
        registerPlaceholder("regex:%raycast.block.type/([0-9]+)%", "&6%raycast.block.type/&7[range]&6%", (player, house, match) -> {
            int range = Integer.parseInt(match.getGroups().get(1).getValue());
            Location loc = player.getTargetBlock(null, range).getLocation();
            if (loc == null) return "null";
            return loc.getBlock().getType().name();
        });

        registerPlaceholder("regex:%raycast.entity/([0-9]+)%", "&6%raycast.entity/&7[range]&6%", (player, house, match) -> {
            // Get the entity the player is looking at within 5 blocks
            Entity entity = getEntityLookingAt(player, Integer.parseInt(match.getGroups().get(1).getValue()));
            if (entity == null) return "null";
            return entity.getName();
        });
        registerPlaceholder("regex:%raycast.entity.type/([0-9]+)%", "&6%raycast.entity.type/&7[range]&6%", (player, house, match) -> {
            // Get the entity the player is looking at within 5 blocks
            Entity entity = getEntityLookingAt(player, Integer.parseInt(match.getGroups().get(1).getValue()));
            if (entity == null) return "null";
            return (entity.hasMetadata("NPC")) ? "NPC" : entity.getType().name();
        });
        registerPlaceholder("regex:%raycast.entity.x/([0-9]+)%", "&6%raycast.entity.x/&7[range]&6%", (player, house, match) -> {
            // Get the entity the player is looking at within 5 blocks
            Entity entity = getEntityLookingAt(player, Integer.parseInt(match.getGroups().get(1).getValue()));
            if (entity == null) return "null";
            return String.valueOf((int) entity.getLocation().getX());
        });
        registerPlaceholder("regex:%raycast.entity.y/([0-9]+)%", "&6%raycast.entity.y/&7[range]&6%", (player, house, match) -> {
            // Get the entity the player is looking at within 5 blocks
            Entity entity = getEntityLookingAt(player, Integer.parseInt(match.getGroups().get(1).getValue()));
            if (entity == null) return "null";
            return String.valueOf((int) entity.getLocation().getY());
        });
        registerPlaceholder("regex:%raycast.entity.z/([0-9]+)%", "&6%raycast.entity.z/&7[range]&6%", (player, house, match) -> {
            // Get the entity the player is looking at within 5 blocks
            Entity entity = getEntityLookingAt(player, Integer.parseInt(match.getGroups().get(1).getValue()));
            if (entity == null) return "null";
            return String.valueOf((int) entity.getLocation().getZ());
        });

        // Standing on placeholders
        registerPlaceholder("%standing.block%", (player, house) -> {
            Location loc = player.getLocation().clone().add(0, -1, 0);
            return loc.getBlock().getType().name();
        });
        //Touching block (left, right, front, back, up, down)
        registerPlaceholder("%touching.block/left%", (player, house) -> {
            Location loc = player.getLocation();
            Location left = loc.clone().add(-1, 0, 0);
            return left.getBlock().getType().name();
        });
        registerPlaceholder("%touching.block/right%", (player, house) -> {
            Location loc = player.getLocation();
            Location right = loc.clone().add(1, 0, 0);
            return right.getBlock().getType().name();
        });
        registerPlaceholder("%touching.block/front%", (player, house) -> {
            Location loc = player.getLocation();
            Location front = loc.clone().add(0, 0, -1);
            return front.getBlock().getType().name();
        });
        registerPlaceholder("%touching.block/back%", (player, house) -> {
            Location loc = player.getLocation();
            Location back = loc.clone().add(0, 0, 1);
            return back.getBlock().getType().name();
        });
        registerPlaceholder("%touching.block/up%", (player, house) -> {
            Location loc = player.getLocation();
            Location up = loc.clone().add(0, 2, 0);
            return up.getBlock().getType().name();
        });
        registerPlaceholder("%touching.block/down%", (player, house) -> {
            Location loc = player.getLocation();
            Location down = loc.clone().add(0, -1, 0);
            return down.getBlock().getType().name();
        });

        // House placeholders
        registerPlaceholder("%%house%%", (player, house) -> house.getName());
        registerPlaceholder("%house.name%", (player, house) -> house.getName());
        registerPlaceholder("%house.cookies%", (player, house) -> String.valueOf(house.getCookies()));
        registerPlaceholder("%house.guests%", (player, house) -> String.valueOf(house.getGuests()));

        // Cookie Placeholders
        registerPlaceholder("%house.cookies.player%", (player, house) -> (!house.getCookieGivers().isEmpty()) ? String.valueOf(house.getCookieGivers().getLast()) : "null");

        // Misc
        registerPlaceholder("%unix.time%", (player, house) -> String.valueOf(System.currentTimeMillis() / 1000));
        registerPlaceholder("%time.unix%", (player, house) -> String.valueOf(System.currentTimeMillis() / 1000));
        registerPlaceholder("%unix.date%", (player, house) -> String.valueOf(System.currentTimeMillis()));
        registerPlaceholder("%date.unix%", (player, house) -> String.valueOf(System.currentTimeMillis()));
        registerPlaceholder("%server.tps%", (player, house) -> new DecimalFormat("#.##").format(Bukkit.getServerTickManager().getTickRate()));

        // Region
        registerPlaceholder("%region.name%", (player, house) -> house.getRegions().stream().filter(region -> region.getPlayersInRegion().contains(player.getUniqueId())).map(Region::getName).findFirst().orElse("none"));

        // Regex for capturing stat placeholders like %stat.global/<stat>%
        registerPlaceholder("regex:%stat\\.global/([a-zA-Z0-9_#]+)%", "&6%stat.global/&7[global_stat]&6%", (player, house, match) -> {
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
                String returning = String.valueOf(Math.round(val * Math.pow(10, places)) / Math.pow(10, places));
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

        //Regex for placeholders in placeholders
        registerPlaceholder("regex:%(.+)%(.+)%%", "&6%&7[placeholder]%&7[placeholder]&6%", (player, house, match) -> {
            String placeholder = match.getGroups().get(1).getValue();
            String value = parsePlaceholders(player, house, match.getGroups().get(2).getValue());
            return parsePlaceholders(player, house, "%" + placeholder + value + "%");
        });

        //Regex for math between placeholders and numbers
        registerPlaceholder("regex:%math.double/(.+) ([+\\-*/]) (.+)%", "&6%math/&7[placeholder] [+ - * / % ^] [placeholder/number]&6%", (player, house, match) -> {
            String value1 = parsePlaceholders(player, house, match.getGroups().get(1).getValue());
            String operator = match.getGroups().get(2).getValue();
            String value2 = parsePlaceholders(player, house, match.getGroups().get(3).getValue());
            if (NumberUtilsKt.isDouble(value1) && NumberUtilsKt.isDouble(value2)) {
                double val1 = Double.parseDouble(value1);
                double val2 = Double.parseDouble(value2);
                switch (operator) {
                    case "+":
                        return String.valueOf(val1 + val2);
                    case "-":
                        return String.valueOf(val1 - val2);
                    case "*":
                        return String.valueOf(val1 * val2);
                    case "/":
                        return String.valueOf(val1 / val2);
                    case "%":
                        return String.valueOf(val1 % val2);
                    case "^":
                        return String.valueOf(Math.pow(val1, val2));
                }
            }
            return value1 + operator + value2;
        });

        //Regex for math between placeholders and numbers
        registerPlaceholder("regex:%math.int/(.+) ([+\\-*/]) (.+)%", "&6%math/&7[placeholder] [+ - * / % ^] [placeholder/number]&6%", (player, house, match) -> {
            String value1 = parsePlaceholders(player, house, match.getGroups().get(1).getValue());
            String operator = match.getGroups().get(2).getValue();
            String value2 = parsePlaceholders(player, house, match.getGroups().get(3).getValue());
            if (NumberUtilsKt.isInt(value1) && NumberUtilsKt.isInt(value2)) {
                int val1 = Integer.parseInt(value1);
                int val2 = Integer.parseInt(value2);
                switch (operator) {
                    case "+":
                        return String.valueOf(val1 + val2);
                    case "-":
                        return String.valueOf(val1 - val2);
                    case "*":
                        return String.valueOf(val1 * val2);
                    case "/":
                        return String.valueOf(val1 / val2);
                    case "%":
                        return String.valueOf(val1 % val2);
                    case "^":
                        return String.valueOf(Math.pow(val1, val2));
                }
            }
            return value1 + operator + value2;
        });
    }


    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        StringBuilder result = new StringBuilder(s);

        for (Placeholder placeholder : placeholders) {
            if (placeholder.getPlaceholder().startsWith("regex:")) {
                String regex = placeholder.getPlaceholder().substring(6);
                Regex pattern = new Regex(regex);
                removeColorCodesInPlaceholders(result);
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

    private static void removeColorCodesInPlaceholders(StringBuilder builder) {
        Pattern pattern = Pattern.compile("%[^%]+%");
        Matcher matcher = pattern.matcher(builder.toString());
        while (matcher.find()) {
            String match = matcher.group();
            builder.replace(matcher.start(), matcher.end(), removeColor(match));
        }
    }

    private static void replaceAll(StringBuilder builder, String target, String replacement) {
        int start = 0;
        while ((start = builder.indexOf(target, start)) != -1) {
            builder.replace(start, start + target.length(), replacement);
            start += replacement.length();
        }
    }

    private static Entity getEntityLookingAt(Player player, int range) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection();
        RayTraceResult result = player.getWorld().rayTrace(eye, direction, range, FluidCollisionMode.NEVER, true, 0.0D, (entity) -> !entity.equals(player));
        if (result == null || result.getHitEntity() == null) return null;
        return result.getHitEntity();
    }
}