package com.al3x.housing2.Axiom;

import com.al3x.housing2.Main;
import com.moulberry.axiom.integration.Integration;
import com.moulberry.axiom.integration.SectionPermissionChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlaygroundIntegration implements Integration.CustomIntegration {

    public static void init() {
        Integration.registerCustomIntegration(Main.getInstance(), new PlaygroundIntegration());
    }
    @Override
    public boolean canBreakBlock(Player player, Block block) {
        return PlaygroundIntegrationImpl.canPlaceBlock(player, block.getLocation());
    }

    public boolean canPlaceBlock(Player player, Location loc) {
        return PlaygroundIntegrationImpl.canPlaceBlock(player, loc);
    }

    public SectionPermissionChecker checkSection(Player player, World world, int cx, int cy, int cz) {
        return PlaygroundIntegrationImpl.checkSection(player, world, cx, cy, cz);
    }

}
