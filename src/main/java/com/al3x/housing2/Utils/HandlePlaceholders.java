package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

/*
    * This class is deprecated and will be removed in the future.
    * It is recommended to use the new placeholder system.
    * This class is only used for backwards compatibility.
 */
@Deprecated
public class HandlePlaceholders {
    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        if (true) {
            return Placeholder.handlePlaceholders(s, house, player);
        }
        return s;
    }
}