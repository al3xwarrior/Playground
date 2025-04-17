package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Action.Properties.ArgumentsProperty;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class Misc {
    public Misc() {
        new UnixTime();
        new UnixDate();
        new ServerTPS();
        new FunctionArgs();
    }

    private static class UnixTime extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%unix.time%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%time.unix%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            return String.valueOf(System.currentTimeMillis() / 1000);
        }
    }

    private static class UnixDate extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%unix.date%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%date.unix%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            return String.valueOf(System.currentTimeMillis());
        }
    }

    private static class ServerTPS extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%server.tps%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%tps%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            return new DecimalFormat("#.##").format(Bukkit.getServerTickManager().getTickRate());
        }
    }

    private static class FunctionArgs extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%function.args/[key]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%function.arg/[key]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }
            String arg1 = StringUtilsKt.substringAfter(input, "/");
            arg1 = Placeholder.handlePlaceholders(arg1, house, player, true);
            List<ArgumentsProperty.Argument> args = Function.functionArguments.get(player.getUniqueId());
            if (args == null) {
                return "null";
            }
            String finalArg = arg1;
            String arg = args.stream().filter((argument) -> argument.getName().equals(finalArg))
                    .map(ArgumentsProperty.Argument::getValue)
                    .findFirst()
                    .orElse("null");
            arg = Placeholder.handlePlaceholders(arg, house, player, false);
            return arg;
        }
    }
}
