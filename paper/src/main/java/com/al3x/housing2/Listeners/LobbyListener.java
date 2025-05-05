package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Listeners.HouseEvents.DamageEvent;
import com.al3x.housing2.Listeners.HouseEvents.PlayerDropItem;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.al3x.housing2.Utils.Color.colorize;

public class LobbyListener implements Listener {
    private static World lobby = Bukkit.getWorld("world"); // main world must be called world (by default it is)

    // TODO: prob should move this somewhere else
    private static ItemStack browserItem = ItemBuilder.create(Material.COMPASS).name("&aHousing Browser &7(Right-Click)").build();
    private static ItemStack myHouses = ItemBuilder.create(Material.GRASS_BLOCK).name("&aMy Houses &7(Right-Click)").build();
    private static ItemStack randomHouse = ItemBuilder.create(Material.PLAYER_HEAD).skullTexture("8a084d0a1c6fc2163de30d8b148ab4d363220d5c972d5f88eb8dc86176ccdb3e").name("&aRandom House &7(Right-Click)").build();
    private static ItemStack ownerMenu = ItemBuilder.create(Material.NETHER_STAR).name("&dHousing Menu &7(Right-Click)").build();
    private static ItemStack playerMenu = ItemBuilder.create(Material.DARK_OAK_DOOR).name("&aHousing Menu &7(Right-Click)").build();

    // Lobby Items
    public static void lobbyItems(Main main) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // They are in a house
            if (player.isDead()) continue;
            //Because they have a split Menu open
            if (MenuManager.getWindowOpen(player) != null && MenuManager.getWindowOpen(player) == MenuManager.getPlayerMenu(player)) continue;
            if (!(player.getWorld().equals(lobby))) {
                World world = player.getWorld();
                HousingWorld house = main.getHousesManager().getHouse(world);

                if (house == null) continue;

                PlayerInventory inv = player.getInventory();
                if (inv.contains(browserItem) || inv.contains(myHouses) || inv.contains(randomHouse)) {
                    inv.remove(browserItem);
                    inv.remove(myHouses);
                    inv.remove(randomHouse);
                }

                // don't replace the slot
                if (inv.getItem(8) != null) continue;

                // Player Owns House
                if (house.hasPermission(player, Permissions.HOUSING_MENU)) {
                    if (inv.contains(ownerMenu)) continue;
                    inv.setItem(8, ownerMenu);
                } else { // Doesn't own house
                    if (inv.contains(playerMenu)) continue;
                    inv.setItem(8, playerMenu);
                }
            } else {
                PlayerInventory inv = player.getInventory();

                if (inv.contains(ownerMenu) || inv.contains(playerMenu)) {
                    inv.remove(ownerMenu);
                    inv.remove(playerMenu);
                }

                HousingTabList.lobbyTabList(player);

                if (!inv.contains(browserItem)) {
                    inv.setItem(0, browserItem);
                }
                if (!inv.contains(myHouses)) {
                    inv.setItem(1, myHouses);
                }
                if (!inv.contains(randomHouse)) {
                    inv.setItem(2, randomHouse);
                }
            }
        }
    }

    @EventHandler
    public void socialHeads(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        Block block = e.getClickedBlock();

        if (block.getType() != Material.PLAYER_HEAD) return;

        // Obviously this won't work for other servers where the coords are not the same
        if (block.getLocation().getBlockX() == -14 && block.getLocation().getBlockY() == 66 && block.getLocation().getBlockZ() == 21) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), "block.note_block.pling", 1, 1);
            e.getPlayer().sendMessage(colorize("&7Join the &fPlayground &7discord here: &f&nhttps://discord.gg/2J7FwNaSat"));
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
        e.setCancelled(true);
        if (!e.getEntity().getWorld().equals(Bukkit.getWorld("world"))) {
            Player player = (Player) e.getEntity();
            if (player.getFoodLevel() < 7 && player.isSprinting()) player.setSprinting(false);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().hasPermission("housing2.admin")) return;
        if (e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))) e.setCancelled(true);
    }

}
