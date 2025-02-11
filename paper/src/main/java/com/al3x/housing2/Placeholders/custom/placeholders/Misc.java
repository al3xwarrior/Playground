package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class Misc {
    public Misc() {
        new UnixTime();
        new UnixDate();
        new ServerTPS();
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
}
