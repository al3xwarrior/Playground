package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.CommandRegistry;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class Command {
    public Command() {
        new Args();
    }

    private static class Args extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%command.args/[index]%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }

            try {
                int index = Integer.parseInt(a[1].replace("%", ""));
                if (CommandRegistry.commandArgsResults.containsKey(player.getUniqueId())) {
                    List<String> args = CommandRegistry.commandArgsResults.get(player.getUniqueId());
                    if (args.size() > index) {
                        return args.get(index);
                    }
                }
            } catch (Exception e) {
                return "null";
            }
            return "null";
        }
    }
}
