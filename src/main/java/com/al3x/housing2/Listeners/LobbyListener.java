package com.al3x.housing2.Listeners;

import com.al3x.housing2.Listeners.HouseEvents.DamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.al3x.housing2.Utils.Color.colorize;

public class LobbyListener implements Listener {

    @EventHandler
    public void socialHeads(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        Block block = e.getClickedBlock();

        if (block.getType() != Material.PLAYER_HEAD) return;

        // Obviously this won't work for other servers where the coords are not the same
        if (block.getLocation().getBlockX() == -14 && block.getLocation().getBlockY() == 66 && block.getLocation().getBlockZ() == 21) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), "block.note_block.pling", 1, 1);
            e.getPlayer().sendMessage(colorize("&7Join the &fHousing2 &7discord here: &f&nhttps://discord.gg/2J7FwNaSat"));
        } else if (block.getLocation().getBlockX() == 0 && block.getLocation().getBlockY() == 66 && block.getLocation().getBlockZ() == 21) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), "block.note_block.pling", 1, 1);
            e.getPlayer().sendMessage(colorize("&7Watch the &fDevlog Series &7on YouTube: &f&nhttps://www.youtube.com/playlist?list=PLfMl37vB75Imh66A-fndddZooejR8qin-"));
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (e.getPlayer().hasPermission("housing2.admin")) return;
        if (e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))) e.setCancelled(true);
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().hasPermission("housing2.admin")) return;
        if (e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))) e.setCancelled(true);
    }

    @EventHandler
    public void pvp(EntityDamageEvent e) {
        if (e.getEntity().getWorld().equals(Bukkit.getWorld("world"))) e.setCancelled(true);
    }

    @EventHandler
    public void hungerChange(FoodLevelChangeEvent e) {
        if (e.getEntity().getWorld().equals(Bukkit.getWorld("world"))) e.setCancelled(true);
    }

}
