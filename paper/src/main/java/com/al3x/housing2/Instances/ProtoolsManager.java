package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.*;
import com.al3x.housing2.Utils.Color;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.tofaa.entitylib.meta.EntityMeta;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ProtoolsManager {
    private Main main;
    private Map<UUID, Long> cooldowns;
    private Map<UUID, Duple<Location, Location>> selections;
    private Map<UUID, Cuboid> copiedRegions;
    private Map<UUID, Stack<List<BlockState>>> undoStacks; // shoutout to chatgpt for information about the Stack Class (i still dont know how it works!)
    private HousesManager housesManager;

    // Queue Stuff
    private final Queue<Runnable> taskQueue;
    private boolean isProcessing;

    public ProtoolsManager(Main main, HousesManager housesManager) {
        this.main = main;
        this.cooldowns = new HashMap<>();
        this.selections = new HashMap<>();
        this.undoStacks = new HashMap<>();
        this.copiedRegions = new HashMap<>();
        this.housesManager = housesManager;

        this.taskQueue = new LinkedList<>();
        this.isProcessing = false;
    }

    public List<Block> removeIllegalBlocks(Player player, List<Block> blocks) {
        HousingWorld house = housesManager.getHouse(player.getWorld());

        List<Location> trashCans = house.getTrashCans();
        List<LaunchPad> launchPads = house.getLaunchPads();

        for (Block block : blocks) {
            if (
                    trashCans.contains(block.getLocation()) ||
                            launchPads.stream().anyMatch(launchPad -> launchPad.getLocation().equals(block.getLocation()))
            ) {
                blocks.remove(block);
            }
        }
        return blocks;
    }

    /* I dont like how this is implemented, it seems like it could be done better, ill come back to it -al3x
    private void enqueueTask(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
        }
        processQueue();
    }

    private void processQueue() {
        if (isProcessing || taskQueue.isEmpty()) return;
        isProcessing = true;

        Bukkit.getScheduler().runTaskLater(main, () -> {
            Runnable task;
            synchronized (taskQueue) {
                task = taskQueue.poll();
            }
            if (task != null) {
                task.run();
            }
            isProcessing = false;
            if (!taskQueue.isEmpty()) {
                processQueue();
            }
        }, 30L);
    }
    */

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

    public void copyRegion(Player player) {
        if (this.selections.containsKey(player.getUniqueId())) {
            Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
            Cuboid cuboid = new Cuboid(selection.getFirst(), selection.getSecond());
            copiedRegions.put(player.getUniqueId(), cuboid);
            player.sendMessage(Color.colorize("&aRegion copied!"));
        } else {
            player.sendMessage(Color.colorize("&cYou must have a selection to do this."));
        }
    }

    public void setRegionTo(Player player, BlockList blockList) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
        Location pos1 = selection.getFirst();
        Location pos2 = selection.getSecond();
        Cuboid cuboid = new Cuboid(pos1, pos2);
        List<Block> blocks = cuboid.getBlocks();
        if (blocks.size() > 2000) {
            player.sendMessage(Color.colorize("&cThe region is too large!"));
            return;
        }

        blocks = removeIllegalBlocks(player, blocks);

        // Save the current state of the blocks to be able to be undone
        List<BlockState> currentState = new ArrayList<>();
        for (Block block : blocks) {
            currentState.add(block.getState());
        }

        addUndoStack(player.getUniqueId(), currentState);

        List<Material> blockOptions = blockList.generateBlocks();
        blocks.forEach(block -> block.setType(blockOptions.get((int) (Math.random() * blockOptions.size()))));
        player.sendMessage(Color.colorize("&aRegion set successfully!"));
    }

    public void setRegionTo(Player player, BlockList from, BlockList to) {
        player.sendMessage(Color.colorize("&aReplacing region..."));
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
        Location pos1 = selection.getFirst();
        Location pos2 = selection.getSecond();
        Cuboid cuboid = new Cuboid(pos1, pos2);
        List<Block> blocks = cuboid.getBlocks();
        if (blocks.size() > 2000) {
            player.sendMessage(Color.colorize("&cThe region is too large!"));
            return;
        }

        blocks = removeIllegalBlocks(player, blocks);

        // Save the current state of the blocks to be able to be undone
        List<BlockState> currentState = new ArrayList<>();
        for (Block block : blocks) {
            currentState.add(block.getState());
        }
        addUndoStack(player.getUniqueId(), currentState);

        List<Material> blockOptions = to.generateBlocks();
        blocks.forEach(block -> {
            if (from.includesMaterial(block.getType())) {
                block.setType(blockOptions.get((int) (Math.random() * blockOptions.size())));
            }
        });
        player.sendMessage(Color.colorize("&aRegion replaced successfully!"));
    }

    public void copyToClipboard(Player player) {
        player.sendMessage(Color.colorize("&aCopying region to clipboard..."));
        Duple<Location, Location> selection = this.selections.get(player.getUniqueId());
        Location pos1 = selection.getFirst();
        Location pos2 = selection.getSecond();
        Cuboid cuboid = new Cuboid(pos1, pos2);
        copiedRegions.put(player.getUniqueId(), cuboid);
        player.sendMessage(Color.colorize("&aRegion copied to clipboard!"));
    }

    public void pasteRegion(Player player) {
        player.sendMessage(Color.colorize("&aPasting region..."));
        if (copiedRegions.containsKey(player.getUniqueId())) {
            Cuboid cuboid = copiedRegions.get(player.getUniqueId());
            List<Block> blocks = cuboid.getBlocks();
            if (blocks.size() > 2000) {
                player.sendMessage(Color.colorize("&cThe region is too large!"));
                return;
            }

            blocks = removeIllegalBlocks(player, blocks);

            // Save the current state of the blocks to be able to be undone
            List<BlockState> currentState = new ArrayList<>();

            for (Block block : blocks) {
                int relX = block.getX() - cuboid.getLowerX();
                int relY = block.getY() - cuboid.getLowerY();
                int relZ = block.getZ() - cuboid.getLowerZ();

                BlockState state = block.getState();
                currentState.add(state);

                Location newLoc = player.getLocation().clone().add(relX, relY, relZ);
                Block newBlock = newLoc.getBlock();
                newBlock.setType(block.getType());
                newBlock.setBlockData(block.getBlockData());
            }
            addUndoStack(player.getUniqueId(), currentState);
            player.sendMessage(Color.colorize("&aRegion pasted successfully!"));
        } else {
            player.sendMessage(Color.colorize("&cYou must copy a region before pasting."));
        }
    }

    // I could not be asked to figure out the math for this method (shoutout to chatgippity)
    public void createSphere(Player player, int radius, BlockList blockList) {
        if (radius > 20) {
            player.sendMessage(Color.colorize("&cThe radius cannot be greater than 20."));
            return;
        }

        player.sendMessage(Color.colorize("&aCreating sphere..."));
        // Save the current state of the blocks to enable undo functionality
        List<BlockState> currentState = new ArrayList<>();

        // Calculate the blocks within the sphere
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Calculate the distance from the center block
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    // Get the current block location
                    Block block = player.getLocation().clone().add(x, y, z).getBlock();
                    // Check if the current block is within the sphere radius
                    if (distance <= radius && !isOutsideBorder(player, block.getLocation())) {
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

        addUndoStack(player.getUniqueId(), currentState);
        player.sendMessage(Color.colorize("&aSphere created successfully!"));
    }

    private void addUndoStack(UUID uuid, List<BlockState> blockStates) {
        Stack<List<BlockState>> undoStack = undoStacks.computeIfAbsent(uuid, k -> new Stack<>());
        if (undoStack.size() >= 25) {
            undoStack.remove(0); // Remove the oldest state
        }
        undoStack.push(blockStates);
    }

    public void undo(Player player) {
        player.sendMessage(Color.colorize("&aUndoing..."));
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
        if (housesManager.hasPermissionInHouse(player, Permissions.PRO_TOOLS)) {
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
                .textWidth(34)
                .build();
    }

    private HashMap<UUID, List<WrapperEntity>> entityList = new HashMap<>();
    private HashMap<UUID, Duple<Location, Location>> lastSelection = new HashMap<>();

    private void createBlockDisplay(Player player, Material material, Vector3f scale, Location location) {
        WrapperEntity entity = new WrapperEntity(EntityTypes.BLOCK_DISPLAY);
        entity.consumeEntityMeta(BlockDisplayMeta.class, meta -> {
            meta.setBlockId(SpigotConversionUtil.fromBukkitBlockData(material.createBlockData()).getGlobalId());
            meta.setScale(scale);
            meta.setGlowing(true);
            meta.setGlowColorOverride(org.bukkit.Color.GREEN.asRGB());
        });
        entity.addViewer(player.getUniqueId());
        entity.spawn(new com.github.retrooper.packetevents.protocol.world.Location(location.getX(), location.getY(), location.getZ(), 0, 0));

        if (!entityList.containsKey(player.getUniqueId())) {
            entityList.put(player.getUniqueId(), new ArrayList<>());
        }
        entityList.get(player.getUniqueId()).add(entity);
    }

    public void drawParticles(Player player, Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return;
        }

        if (lastSelection.containsKey(player.getUniqueId())) {
            Duple<Location, Location> last = lastSelection.get(player.getUniqueId());
            if (last.getFirst().equals(pos1) && last.getSecond().equals(pos2)) {
                return;
            }
            entityList.get(player.getUniqueId()).forEach(WrapperEntity::remove);
            entityList.remove(player.getUniqueId());
        }
        lastSelection.put(player.getUniqueId(), new Duple<>(pos1, pos2));

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        Material material = Material.BLACK_CONCRETE;

        //bottom
        createBlockDisplay(player, material, new Vector3f(0.05f, 0.05f, maxZ - minZ + 1), new Location(null, minX, minY, minZ));
        createBlockDisplay(player, material, new Vector3f(maxX - minX + 1, 0.05f, 0.05f), new Location(null, minX, minY, minZ));
        createBlockDisplay(player, material, new Vector3f(maxX - minX + 1, 0.05f, 0.05f), new Location(null, minX, minY, maxZ + 0.95f));
        createBlockDisplay(player, material, new Vector3f(0.05f, 0.05f, maxZ - minZ + 1), new Location(null, maxX + 0.95f, minY, minZ));

        //top
        createBlockDisplay(player, material, new Vector3f(0.05f, 0.05f, maxZ - minZ + 1), new Location(null, minX, maxY + 0.95f, minZ));
        createBlockDisplay(player, material, new Vector3f(maxX - minX + 1, 0.05f, 0.05f), new Location(null, minX, maxY + 0.95f, minZ));
        createBlockDisplay(player, material, new Vector3f(maxX - minX + 1, 0.05f, 0.05f), new Location(null, minX, maxY + 0.95f, maxZ + 0.95f));
        createBlockDisplay(player, material, new Vector3f(0.05f, 0.05f, maxZ - minZ + 1), new Location(null, maxX + 0.95f, maxY + 0.95f, minZ));

        //connectors
        createBlockDisplay(player, material, new Vector3f(0.05f, maxY - minY + 1, 0.05f), new Location(null, minX, minY, minZ));
        createBlockDisplay(player, material, new Vector3f(0.05f, maxY - minY + 1, 0.05f), new Location(null, minX, minY, maxZ + 0.95f));
        createBlockDisplay(player, material, new Vector3f(0.05f, maxY - minY + 1, 0.05f), new Location(null, maxX + 0.95f, minY, minZ));
        createBlockDisplay(player, material, new Vector3f(0.05f, maxY - minY + 1, 0.05f), new Location(null, maxX + 0.95f, minY, maxZ + 0.95f));
    }

    // Shoutout 2008Choco for the method
    // (we are stealing hypixel code real)
    private boolean isOutsideBorder(Player player) {
        WorldBorder border = player.getWorld().getWorldBorder();
        double radius = border.getSize() / 2;
        Location location = player.getLocation(), center = border.getCenter();
        return center.distanceSquared(location) >= (radius * radius);
    }

    private boolean isOutsideBorder(Player player, Location loc) {
        WorldBorder border = player.getWorld().getWorldBorder();
        double radius = border.getSize() / 2;
        Location center = border.getCenter();
        return center.distanceSquared(loc) >= (radius * radius);
    }

    public Duple<Location, Location> getSelection(Player player) {
        return this.selections.get(player.getUniqueId());
    }

    public void clearSelection(Player player) {
        if (this.selections.containsKey(player.getUniqueId())) {
            lastSelection.remove(player.getUniqueId());
            entityList.get(player.getUniqueId()).forEach(WrapperEntity::remove);
            entityList.remove(player.getUniqueId());
        }
        this.selections.remove(player.getUniqueId());
    }

    public Map<UUID, Long> getCooldowns() {
        return this.cooldowns;
    }
}
