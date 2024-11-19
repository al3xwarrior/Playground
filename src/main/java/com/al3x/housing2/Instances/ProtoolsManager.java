package com.al3x.housing2.Instances;

import com.al3x.housing2.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ProtoolsManager {
    private Map<UUID, Long> cooldowns;
    private Map<UUID, Duple<Location, Location>> selections;
    private Map<UUID, Stack<List<BlockState>>> undoStacks; // shoutout to chatgpt for information about the Stack Class (i still dont know how it works!)
    private HousesManager housesManager;

    public ProtoolsManager(HousesManager housesManager) {
        this.cooldowns = new HashMap<>();
        this.selections = new HashMap<>();
        this.undoStacks = new HashMap<>();
        this.housesManager = housesManager;
    }

    public void setPos1(Player player, Location pos1) {
        if (this.selections.containsKey(player.getUniqueId())) {
            Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
            this.selections.put(player.getUniqueId(), new Duple<>(pos1, selection.getSecond()));
        } else {
            this.selections.put(player.getUniqueId(), new Duple<>(pos1, null));
        }
    }

    public void setPos2(Player player, Location pos2) {
        if (this.selections.containsKey(player.getUniqueId())) {
            Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
            this.selections.put(player.getUniqueId(), new Duple<>(selection.getFirst(), pos2));
        } else {
            this.selections.put(player.getUniqueId(), new Duple<>(null, pos2));
        }
    }

    public void setPositions(Player player, Location pos1, Location pos2) {
        this.selections.put(player.getUniqueId(), new Duple<>(pos1, pos2));
    }

    public void setRegionTo(Player player, BlockList blockList) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
        Location pos1 = selection.getFirst();
        Location pos2 = selection.getSecond();
        Cuboid cuboid = new Cuboid(pos1, pos2);
        List<Block> blocks = cuboid.getBlocks();

        // Save the current state of the blocks to be able to be undone
        List<BlockState> currentState = new ArrayList<>();
        for (Block block : blocks) {
            currentState.add(block.getState());
        }
        addUndoStack(player, currentState);

        List<Material> blockOptions = blockList.generateBlocks();
        blocks.forEach(block -> block.setType(blockOptions.get((int) (Math.random() * blockOptions.size()))));
    }

    // I could not be asked to figure out the math for this method (shoutout to chatgippity)
    public void createSphere(Player player, int radius, BlockList blockList) {
        // Save the current state of the blocks to enable undo functionality
        List<BlockState> currentState = new ArrayList<>();

        // Calculate the blocks within the sphere
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Calculate the distance from the center block
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    // Check if the current block is within the sphere radius
                    if (distance <= radius) {
                        // Get the current block location
                        Block block = player.getLocation().clone().add(x, y, z).getBlock();

                        // Save the current block state
                        currentState.add(block.getState());

                        // Randomly select a block material from the BlockList and set the block type
                        List<Material> blockOptions = blockList.generateBlocks();
                        Material material = blockOptions.get((int) (Math.random() * blockOptions.size()));
                        block.setType(material);
                    }
                }
            }
        }

        addUndoStack(player, currentState);
        player.sendMessage(Color.colorize("&aSphere created successfully!"));
    }

    private void addUndoStack(Player player, List<BlockState> blockStates) {
        Stack<List<BlockState>> undoStack = undoStacks.computeIfAbsent(player.getUniqueId(), k -> new Stack<>());
        if (undoStack.size() >= 20) {
            undoStack.remove(0); // Remove the oldest state
        }
        undoStack.push(blockStates);
    }

    public void undo(Player player) {
        UUID uuid = player.getUniqueId();
        if (undoStacks.containsKey(uuid) && !undoStacks.get(uuid).isEmpty()) {
            List<BlockState> previousState = undoStacks.get(uuid).pop();
            for (BlockState state : previousState) {
                state.update(true, false);
            }
            player.sendMessage(Color.colorize("&aUndo successful."));
        } else {
            player.sendMessage(Color.colorize("&cNothing to undo."));
        }
    }

    public boolean canUseProtools(Player player, boolean ignoreSelection) {
        if (housesManager.playerIsInOwnHouse(player)) {
            if (offCooldown(player)) {
                if (ignoreSelection || hasSelection(player)) {
                    return true;
                } else {
                    player.sendMessage(Color.colorize("&cYou must have a selection to do this."));
                    return false;
                }
            } else {
                player.sendMessage(Color.colorize("&cYou are on cooldown."));
                return false;
            }
        } else {
            player.sendMessage(Color.colorize("&cYou must be in your own house to do this."));
            return false;
        }
    }

    private boolean offCooldown(Player player) {
        if (this.cooldowns.containsKey(player.getUniqueId())) {
            return System.currentTimeMillis() - this.cooldowns.get(player.getUniqueId()) > 1000;
        }
        return true;
    }

    private boolean hasSelection(Player player) {
        if (this.selections.containsKey(player.getUniqueId())) {
            Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
            return selection.getFirst() != null && selection.getSecond() != null;
        }
        return false;
    }

    public static ItemStack getWand() {
        return ItemBuilder.create(Material.STICK)
                .name("&bRegion Selection Tool")
                .description("&7Selects a region with left and right clicks, which can them be modified with other tools.\n\n&7Command alias: &b//\n\n&eLeft click to select point A.\n&eRight click to select point B.")
                .punctuation(false)
                .textWitdh(34)
                .build();
    }

    public void drawParticles(Player player, Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return;
        }
        //Draw the outline of the region
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        player.spawnParticle(Particle.DUST, x + 0.5, y + 0.5, z + 0.5, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.RED, 2));
                    }
                }
            }
        }

    }

    public Duple<Location, Location> getSelection(Player player) {
        return this.selections.get(player.getUniqueId());
    }

    public Map<UUID, Long> getCooldowns() {
        return this.cooldowns;
    }
}
