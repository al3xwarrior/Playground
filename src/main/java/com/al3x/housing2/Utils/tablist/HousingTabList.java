package com.al3x.housing2.Utils.tablist;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingTabList {

    public static void setTabList(Player player, HousingWorld world) {
        String header = "&bYou are playing on &e&lIDFK.AL3X.COM\n";
        String tps = Bukkit.getServer().getTPS()[0] > 18 ? "&a" : Bukkit.getServer().getTPS()[0] > 15 ? "&e" : "&c";
        tps += Math.round(Bukkit.getServer().getTPS()[0] * 10.0) / 10.0;
        String footer = "\n&fYou are in &a" + world.getName() + "&f, by &b" + world.getOwnerName() + "\n\n" +
                "&fVisiting Rules: " + world.getPrivacy().asString() + "\n\n" +
                "&fGuests: &7" + world.getGuests() + " | &fCookies: &a" + world.getCookies() + "\n" +
                "&cTPS: &f" + tps + " &7| &cPing: &f" + player.getPing() +
                "\n" +
                "&aNothing, Nothing & NOTHING! &c&lNOPE.NET";

        player.setPlayerListHeaderFooter(colorize(header), colorize(footer));
    }
}
