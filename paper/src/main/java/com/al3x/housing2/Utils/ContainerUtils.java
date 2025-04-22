package com.al3x.housing2.Utils;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ContainerUtils {
    public static Chest getMainChest(Chest clickedChest) {
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

    public static Container getMainContainerFromHolder(InventoryHolder holder) {
        switch (holder) {
            case Chest chest -> {
                return getMainChest(chest);
            }
            case DoubleChest doubleChest -> {
                // Try left side first; if not, the right side.
                InventoryHolder right = doubleChest.getRightSide();
                if (right instanceof Chest rightChest) return getMainChest(rightChest);
            }
            case Container container -> {
                return container;
            }
            default -> {}
        }

        return null;
    }

    public static ContainerLock readLock(PersistentDataContainer pdc, NamespacedKey key, Gson gson) {
        if (pdc.has(key, PersistentDataType.STRING)) {
            try {
                return gson.fromJson(pdc.get(key, PersistentDataType.STRING), ContainerLock.class);
            } catch (Exception ignored) {}
        }
        return new ContainerLock();
    }

    // --- Inner Classes for Container Lock Data ---

    public static enum ContainerStatus {
        LOCKED, UNLOCKED, DYNAMIC;

        public ContainerStatus toggle() {
            return this == LOCKED ? UNLOCKED : LOCKED;
        }
    }

    public static enum ContainerMode {
        NORMAL, REPLICATING, READ_ONLY,
    }

    @Getter @Setter
    public static class ContainerLock {
        private ContainerStatus status;
        private ContainerMode mode;

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
