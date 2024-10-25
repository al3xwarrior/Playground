package com.al3x.housing2.Utils.tablist;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingTabList {

    public static void setTabList(Player player, HousingWorld world) {
        String header = "&bYou are playing on &e&lIDFK.AL3X.COM\n";
        String footer = "\n&fYou are in &a" + world.getName() + "&f, by &b" + world.getOwnerName() + "\n\n" +
                "&fVisiting Rules: " + world.getPrivacy().asString() + "\n\n" +
                "&fGuests: &7" + world.getGuests() + " | &fCookies: &a" + world.getCookies() + "\n\n" +
                "&aNothing, Nothing & NOTHING! &c&lNOPE.NET";

        player.setPlayerListHeaderFooter(colorize(header), colorize(footer));
    }
}
