package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.LaunchPad;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import static com.al3x.housing2.Utils.Color.colorize;

public class LaunchPadListener implements Listener {

    private Main main;

    public LaunchPadListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void stepOnLaunchPad(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Block block = e.getTo().getBlock().getRelative(0, -1, 0);
        if (block.getType() != Material.SLIME_BLOCK) return;

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        if (house.launchPadAtLocation(block.getLocation())) {
            LaunchPad launchPad = house.getLaunchPadAtLocation(block.getLocation());
            launchPad.launchPlayer(player);
        }
    }

    @EventHandler
    public void clickLaunchPad(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null) return;

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        if (e.getAction().isLeftClick()) return;

        if (house.launchPadAtLocation(block.getLocation())) {
            LaunchPad launchPad = house.getLaunchPadAtLocation(block.getLocation());
            launchPad.openConfigGUI(player);
        }
    }

    @EventHandler
    public void placeLaunchPad(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack item = e.getItemInHand();

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        boolean ownerOfHouse = house.getOwnerUUID().equals(player.getUniqueId());

        String name = item.getItemMeta().getDisplayName();
        Material itemType = item.getType();

        if (name.equals("§aLaunch Pad") && itemType.equals(Material.SLIME_BLOCK) && ownerOfHouse) {
            Block blockPlaced = e.getBlockPlaced();
            house.addLaunchPad(blockPlaced.getLocation());
            player.sendMessage(colorize("&aLaunch Pad placed!"));
        }
    }

    @EventHandler
    public void breakLaunchPad(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        if (house.launchPadAtLocation(block.getLocation())) {
            house.removeLaunchPad(block.getLocation());
            player.sendMessage(colorize("&cLaunch Pad removed!"));
        }
    }

}