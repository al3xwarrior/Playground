package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class FormattedStat {
    public FormattedStat() {
        new GlobalStat();
        new PlayerStat();
        new NPCStat();

        new FormattedPlaceholder();
    }

    private static String formatted(String string, int places) {
        if (NumberUtilsKt.isInt(string)) {
            return NumberUtilsKt.formatNumber(Integer.parseInt(string), places);
        } else if (NumberUtilsKt.isDouble(string)) {
            return NumberUtilsKt.formatNumber(Double.parseDouble(string), places);
        } else {
            return string;
        }
    }

    public static class GlobalStat extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%formattedstat.global/[stat] [places]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String after = StringUtilsKt.substringAfter(input, "/");
            String parsed = Placeholder.handlePlaceholders(after, house, player, true);
            String[] args = parsed.split(" ");
            if (args.length < 2) {
                return "null";
            }

            Stat stat = house.getStatManager().getGlobalStatByName(args[0]);
            if (stat == null) {
                return "null";
            }

            return formatted(stat.formatValue(), Integer.parseInt(args[1]));
        }
    }

    public static class PlayerStat extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%formattedstat.player/[stat] [places]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String after = StringUtilsKt.substringAfter(input, "/");
            String parsed = Placeholder.handlePlaceholders(after, house, player, true);
            String[] args = parsed.split(" ");
            if (args.length < 2) {
                return "Invalid arguments.";
            }

            Stat stat = house.getStatManager().getPlayerStatByName(player, args[0]);
            if (stat == null) {
                return "null";
            }

            return formatted(stat.formatValue(), Integer.parseInt(args[1]));
        }
    }

    public static class NPCStat extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%formattedstat.npc/[npc] [stat] [places]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String after = StringUtilsKt.substringAfter(input, "/");
            String parsed = Placeholder.handlePlaceholders(after, house, player, true);
            String[] args = parsed.split(" ");
            if (args.length < 3) {
                return "null";
            }

            if (!NumberUtilsKt.isInt(args[0]) || house.getNPC(Integer.parseInt(args[0])) == null) {
                return "null";
            }

            HousingNPC npc = house.getNPC(Integer.parseInt(args[0]));
            Stat stat = npc.getStats().stream().filter(s -> s.getStatName().equals(args[1])).findFirst().orElse(null);

            if (stat == null) {
                return "null";
            }

            return formatted(stat.formatValue(), Integer.parseInt(args[2]));
        }
    }

    public static class FormattedPlaceholder extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%formattedstat/[placeholder] [places]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String after = StringUtilsKt.substringAfter(input, "/");
            String parsed = Placeholder.handlePlaceholders(after, house, player, true);
            String[] args = parsed.split(" ");
            if (args.length < 2) {
                return "null";
            }

            return formatted(args[0], Integer.parseInt(args[1]));
        }
    }
}
