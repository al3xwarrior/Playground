package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;

public class HandlePlaceholders {

    public static String parsePlaceholders(Player player, HousingWorld house, String s) {
        return s
                .replaceAll("%player.name%", player.getName())
                .replaceAll("%player.displayname%", player.getDisplayName())
                .replaceAll("%player.ping%", String.valueOf(player.getPing()))
                .replaceAll("%player.isSprinting%", String.valueOf(player.isSprinting()))
                .replaceAll("%player.location.x%", String.valueOf(player.getLocation().getX()))
                .replaceAll("%player.location.y%", String.valueOf(player.getLocation().getY()))
                .replaceAll("%player.location.z%", String.valueOf(player.getLocation().getZ()))
                .replaceAll("%player.location.pitch%", String.valueOf(player.getLocation().getPitch()))
                .replaceAll("%player.location.yaw%", String.valueOf(player.getLocation().getYaw()))

                .replaceAll("%house.name%", house.getName())
                .replaceAll("%house.cookies%", String.valueOf(house.getCookies()))
                .replaceAll("%house.guests%", String.valueOf(house.getGuests()));
    }

}
