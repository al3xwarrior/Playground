package com.al3x.housing2.Listeners;

import com.al3x.housing2.Action.Actions.NpcPathAction;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.LocationData;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.NbtItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NpcItems implements Listener {
    public static HashMap<UUID, NpcPathAction> npcPathActionHashMap = new HashMap<>();

    HousesManager housesManager;

    public NpcItems(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void playerInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        HousingWorld house = housesManager.getHouse(player.getWorld());

        if (house == null) {
            return;
        }

        ItemStack item = e.getItem();

        if (item == null) {
            return;
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemMeta itemMeta = item.getItemMeta();
            Block block = e.getClickedBlock();

            if (house.hasPermission(player, Permissions.ITEM_NPCS)) {
                NamespacedKey key = new NamespacedKey(Main.getInstance(), "isPath");
                NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "npc");
                if (itemMeta != null && itemMeta.getPersistentDataContainer().has(key) && itemMeta.getPersistentDataContainer().has(key2)) {
                    e.setCancelled(true);
                    int id = itemMeta.getPersistentDataContainer().get(key2, PersistentDataType.INTEGER);
                    HousingNPC npc = house.getNPC(id);
                    npc.addPath(block.getLocation().add(0.5, 1, 0.5));
                    player.sendMessage("§aAdded path node (" + (npc.getPath().size()) + ") at " + block.getX() + ", " + block.getY() + ", " + block.getZ());
                }


                NbtItemBuilder nbtItemBuilder = NbtItemBuilder.fromItemStack(item);
                if (nbtItemBuilder.getBoolean("isPathForAction")) {
                    e.setCancelled(true);
                    if (npcPathActionHashMap.containsKey(player.getUniqueId())) {
                        NpcPathAction npcPathAction = npcPathActionHashMap.get(player.getUniqueId());
                        List<LocationData> path = npcPathAction.getPath();
                        path.add(LocationData.Companion.fromLocation(block.getLocation().add(0.5, 1, 0.5)));
                        player.sendMessage("§aAdded path node (" + (path.size()) + ") at " + block.getX() + ", " + block.getY() + ", " + block.getZ());
                    }
                }
            }
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemMeta itemMeta = item.getItemMeta();

            if (house.hasPermission(player, Permissions.ITEM_NPCS)) {
                NamespacedKey key = new NamespacedKey(Main.getInstance(), "isPath");
                NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "npc");
                if (itemMeta != null && itemMeta.getPersistentDataContainer().has(key) && itemMeta.getPersistentDataContainer().has(key2)) {
                    e.setCancelled(true);
                    int id = itemMeta.getPersistentDataContainer().get(key2, PersistentDataType.INTEGER);
                    HousingNPC npc = housesManager.getHouse(player.getWorld()).getNPC(id);
                    if (npc.getPath() != null && !npc.getPath().isEmpty()) {
                        Location loc = npc.removePath();
                        player.sendMessage("§aRemoved path node (" + (npc.getPath().size() + 1) + ") at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                    }
                }

                NbtItemBuilder nbtItemBuilder = NbtItemBuilder.fromItemStack(item);
                if (nbtItemBuilder.getBoolean("ispathforaction")) {
                    e.setCancelled(true);
                    if (npcPathActionHashMap.containsKey(player.getUniqueId())) {
                        NpcPathAction npcPathAction = npcPathActionHashMap.get(player.getUniqueId());
                        List<LocationData> path = npcPathAction.getPath();
                        if (path != null && !path.isEmpty()) {
                            LocationData loc = path.removeLast();
                            player.sendMessage("§aRemoved path node (" + (path.size() + 1) + ") at " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                        }
                    }
                }
            }

        }

        if (e.getAction().isRightClick() && player.isSneaking()) {
            NbtItemBuilder nbtItemBuilder = NbtItemBuilder.fromItemStack(item);
            if (nbtItemBuilder.getBoolean("isPathForAction")) {
                e.setCancelled(true);
                if (npcPathActionHashMap.containsKey(player.getUniqueId())) {
                    NpcPathAction npcPathAction = npcPathActionHashMap.get(player.getUniqueId());
                    npcPathAction.getPath().clear();
                    player.sendMessage("§aCleared path nodes");
                }
            }
        }

        if (e.getAction().isLeftClick() && player.isSneaking()) {
            NbtItemBuilder nbtItemBuilder = NbtItemBuilder.fromItemStack(item);
            if (nbtItemBuilder.getBoolean("isPathForAction")) {
                e.setCancelled(true);
                if (npcPathActionHashMap.containsKey(player.getUniqueId())) {
                    MenuManager.getPlayerMenu(player).open();
                    npcPathActionHashMap.remove(player.getUniqueId());
                    player.getInventory().remove(item);
                    player.sendMessage("§cCancelled path editing");
                }
            }
        }
    }
}
