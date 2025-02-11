package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveFormatting extends Placeholder {
    @Override
    public String getPlaceholder() {
        return "%removeFormatting/[placeholder]%";
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
        String placeholder = StringUtilsKt.substringAfter(input, "/");
        String value = Placeholder.handlePlaceholders(placeholder, house, player, true);
        return StringUtilsKt.removeStringFormatting(value, house, player);
    }
}
