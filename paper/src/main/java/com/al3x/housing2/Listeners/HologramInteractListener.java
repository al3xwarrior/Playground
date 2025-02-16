package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HologramEditorMenu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.maximde.hologramlib.hologram.TextHologram;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;

public class HologramInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public HologramInteractListener(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void handleInteraction(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (!housesManager.hasPermissionInHouse(player, Permissions.ITEM_HOLOGRAM)) return;
        if (housesManager.getHouse(player.getWorld()) == null) return;

        if (e.getRightClicked() instanceof Interaction interaction) {
            for (Hologram hologram : housesManager.getHouse(player.getWorld()).getHolograms()) {
                if (hologram.getInteraction().getUniqueId().equals(interaction.getUniqueId())) {
                    new HologramEditorMenu(main, player, hologram).open();
                    return;
                }
            }
        }
    }

    @EventHandler
    public void armorStandEditor(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!housesManager.hasPermissionInHouse(player, Permissions.ITEM_HOLOGRAM)) return;
        if (housesManager.getHouse(player.getWorld()) == null) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK) && !(e.getAction() == Action.RIGHT_CLICK_AIR)) return;

        List<Hologram> holograms = housesManager.getHouse(player.getWorld()).getHolograms();

        //TODO: evaluate performance and then maybe async it if needed

        //Look between 1 and 5 blocks in front of the player without using spigot raycast
        Location location = player.getEyeLocation();
        Vector dir = location.getDirection();
        Location loc2 = location.clone().add(dir.clone().multiply(5));
        double distance = location.distance(loc2);
        double increment = distance / 10;
        for (double i = 0; i < distance; i += increment) {
            Location loc = location.clone().add(dir.clone().multiply(i));
            for (Hologram hologram : holograms) {
                if (hologram.getHolograms(player) == null) continue;
                for (TextHologram textHologram : hologram.getHolograms(player)) {
                    if (textHologram.getLocation().distance(loc) <= 0.5) {
                        new HologramEditorMenu(main, player, hologram).open();
                        return;
                    }
                }

                if (e.getInteractionPoint() != null && hologram.getLocation().distance(e.getInteractionPoint().clone().add(0, 1, 0)) <= 0.5) {
                    new HologramEditorMenu(main, player, hologram).open();
                    return;
                }
            }
        }
    }

}
