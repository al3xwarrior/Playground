package com.al3x.housing2.Listeners;

import com.al3x.housing2.Data.PlayerData;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.EditPlayerMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.PlayerListingMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Location;

import java.util.function.Supplier;
import java.util.logging.Level;

import net.kyori.adventure.text.Component;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.al3x.housing2.Utils.Color.colorize;

import com.google.gson.Gson;

public class ChestListener implements Listener {

    private Main main;
    private static final Gson GSON = new Gson();

    public ChestListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        // Only process right-click block interactions
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.hasBlock()) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        BlockState state = block.getState();
        // Only handle blocks with a tile entity that are also containers.
        if (!(state instanceof Container)) return;

        main.getLogger().log(Level.SEVERE, "Inventory block with PDC: " + block.getType());

        TileState tileState;

        // If the block is a Chest, check for main chest in double chests.
        if (state instanceof Chest chest && getMainChest(chest) instanceof TileState canonicalTileState) {
            tileState = canonicalTileState;
        } else {
            tileState = (TileState) state;
        }

        // Get the PersistentDataContainer from this block’s state.
        PersistentDataContainer pdc = tileState.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(main, "container_lock");

        // When the player is sneaking, toggle the container lock state.
        if (e.getPlayer().isSneaking()) {
            main.getLogger().log(Level.SEVERE, "Toggling container lock data!");
            ContainerLock lockData = null;
            // Attempt to read existing JSON lock data.
            if (pdc.has(key, PersistentDataType.STRING)) {
                String json = pdc.get(key, PersistentDataType.STRING);
                try {
                    lockData = GSON.fromJson(json, ContainerLock.class);
                } catch (Exception ex) {
                    // If parsing fails, we'll default below.
                }
            }
            if (lockData == null || lockData.getMode() == null) {
                lockData = new ContainerLock(); // defaults to LOCKED
            }
            // Toggle state: if LOCKED, then set to UNLOCKED; if UNLOCKED, set to LOCKED.
            if (lockData.getStatus() == ContainerStatus.LOCKED) {
                lockData.setStatus(ContainerStatus.UNLOCKED);
            } else if (lockData.getStatus() == ContainerStatus.UNLOCKED) {
                lockData.setStatus(ContainerStatus.LOCKED);
            }
            String json = GSON.toJson(lockData);
            pdc.set(key, PersistentDataType.STRING, json);
            tileState.update(true);
            e.setCancelled(true);
            return;
        }

        // For non-sneaking interactions, read the lock data and possibly cancel the event.
        ContainerLock lockData;
        if (pdc.has(key, PersistentDataType.STRING)) {
            String json = pdc.get(key, PersistentDataType.STRING);
            try {
                lockData = GSON.fromJson(json, ContainerLock.class);
                if (lockData == null || lockData.getMode() == null) {
                    lockData = new ContainerLock();
                }
            } catch (Exception ex) {
                lockData = new ContainerLock();
            }
        } else {
            lockData = new ContainerLock();
        }
        main.getLogger().info("Found container lock mode: " + lockData.getMode());
        if (lockData.getStatus() == ContainerStatus.LOCKED) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(colorize("&cChest is locked."));
        }
    }

    private static Chest getMainChest(Chest clickedChest) {
        Block block = clickedChest.getBlock();
        BlockState state = block.getState();

        if (!(state.getBlockData() instanceof org.bukkit.block.data.type.Chest chestData)) {
            return clickedChest; // Not a real chest block (shouldn't happen)
        }

        // Single chest or already the main chest
        if (chestData.getType() == org.bukkit.block.data.type.Chest.Type.SINGLE ||
            chestData.getType() == org.bukkit.block.data.type.Chest.Type.RIGHT) {
            return clickedChest;
        }

        // If this is the RIGHT side, step toward the LEFT (the canonical side)
        BlockFace leftDirection = switch (chestData.getFacing()) {
            case NORTH -> BlockFace.EAST;
            case EAST  -> BlockFace.SOUTH;
            case SOUTH -> BlockFace.WEST;
            default    -> BlockFace.NORTH;
        };
        Block leftBlock = block.getRelative(leftDirection);

        if (leftBlock.getType() != Material.CHEST) return clickedChest;

        BlockState leftState = leftBlock.getState();
        return (leftState instanceof Chest leftChest) ? leftChest : clickedChest;
    }

    // --- Inner Classes for Container Lock Data ---

    public static enum ContainerStatus {
        LOCKED,
        UNLOCKED,
        DYNAMIC
    }

    public static enum ContainerMode {
        NORMAL,
        REPLICATING,
        READ_ONLY,
    }

    public static class ContainerLock {
        @Getter @Setter private ContainerStatus status;
        @Getter @Setter private ContainerMode mode;

        // Default constructor defaults the lock mode to LOCKED.
        public ContainerLock() {
            this.status = ContainerStatus.LOCKED;
            this.mode = ContainerMode.NORMAL;
        }

        public ContainerLock(ContainerStatus status, ContainerMode mode) {
            this.status = status;
            this.mode = mode;
        }
    }
}
