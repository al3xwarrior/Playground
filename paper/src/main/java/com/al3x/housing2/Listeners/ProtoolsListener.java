package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ProtoolsListener
        implements Listener {
    private ProtoolsManager protoolsManager;

    public ProtoolsListener(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        if (item.getItemMeta().getDisplayName().equals("§bRegion Selection Tool")) {
            e.setCancelled(true);
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                protoolsManager.setPos1(player, e.getClickedBlock().getLocation());
                player.sendMessage(Color.colorize("&aPosition 1 set to &2" + e.getClickedBlock().getLocation().getBlockX() + "&a, &2" + e.getClickedBlock().getLocation().getBlockY() + "&a, &2" + e.getClickedBlock().getLocation().getBlockZ()));
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                protoolsManager.setPos2(player, e.getClickedBlock().getLocation());
                player.sendMessage(Color.colorize("&aPosition 2 set to &2" + e.getClickedBlock().getLocation().getBlockX() + "&a, &2" + e.getClickedBlock().getLocation().getBlockY() + "&a, &2" + e.getClickedBlock().getLocation().getBlockZ()));
            }
        }
    }

}