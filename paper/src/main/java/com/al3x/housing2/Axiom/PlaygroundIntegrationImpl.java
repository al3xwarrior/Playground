package com.al3x.housing2.Axiom;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.moulberry.axiom.integration.SectionPermissionChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlaygroundIntegrationImpl {

    static boolean canBreakBlock(Player player, Location loc) {
        return testBuild(player, loc);
    }

    static boolean canPlaceBlock(Player player, Location loc) {
        return testBuild(player, loc);
    }

    private static boolean testBuild(Player player, Location loc) {
        try {
            HousesManager housesManager = Main.getInstance().getHousesManager();
            if (housesManager == null) {
                return true;
            }

            HousingWorld house = housesManager.getHouse(loc.getWorld());

            if (house == null) {
                return true;
            }

            if (!(house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) && !(house.hasPermission(player, Permissions.BUILD) && house.hasPermission(player, Permissions.OFFLINE_BUILD))) {
                return false;
            }

            return house.getWorld().getWorldBorder().isInside(loc);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static SectionPermissionChecker checkSection(Player player, World world, int cx, int cy, int cz) {
        int minX = cx * 16;
        int minY = cy * 16;
        int minZ = cz * 16;
        int maxX = cx * 16 + 15;
        int maxY = cy * 16 + 15;
        int maxZ = cz * 16 + 15;

        HousesManager housesManager = Main.getInstance().getHousesManager();
        if (housesManager == null) {
            return SectionPermissionChecker.ALL_ALLOWED;
        }

        HousingWorld house = housesManager.getHouse(world);
        if (house == null) {
            return SectionPermissionChecker.ALL_ALLOWED;
        }


        Location min = new Location(world, minX, minY, minZ);
        Location max = new Location(world, maxX, maxY, maxZ);

        if (!(house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) && !(house.hasPermission(player, Permissions.BUILD) && house.hasPermission(player, Permissions.OFFLINE_BUILD))) {
            return SectionPermissionChecker.NONE_ALLOWED;
        }

        if (!house.getWorld().getWorldBorder().isInside(min) || !house.getWorld().getWorldBorder().isInside(max)) {
            return SectionPermissionChecker.NONE_ALLOWED;
        }

        return SectionPermissionChecker.ALL_ALLOWED;
    }

}
