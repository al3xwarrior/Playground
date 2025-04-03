package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class SeatManager {

    private final Main plugin;

    public SeatManager(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if the given location is valid for a seat by temporarily spawning an ArmorStand.
     */
    public boolean isSitLocationValid(Location location) {
        try {
            Consumer<ArmorStand> consumer = armorStand -> {
                try {
                    armorStand.setInvisible(true);
                } catch (Throwable e) {
                    try {
                        ArmorStand.class.getMethod("setVisible", boolean.class)
                                .invoke(armorStand, false);
                    } catch (Throwable ignored) {}
                }
                try {
                    armorStand.setMarker(true);
                } catch (Throwable ignored) {}
            };

            World world = location.getWorld();
            Method spawnMethod = world.getClass().getMethod("spawn", Location.class, Class.class, Consumer.class);
            Entity tempSeat = (Entity) spawnMethod.invoke(world, location, ArmorStand.class, consumer);
            boolean valid = tempSeat.isValid();
            tempSeat.remove();
            return valid;
        } catch (Exception e) {
            System.err.println("Error checking sit location: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if the location is valid for a player to sit by temporarily spawning an AreaEffectCloud.
     */
    public boolean isPlayerSitLocationValid(Location location) {
        try {
            Consumer<Object> consumer = cloud -> {
                try {
                    ((org.bukkit.entity.AreaEffectCloud) cloud).setRadius(0);
                } catch (Throwable ignored) {}
            };

            World world = location.getWorld();
            Method spawnMethod = world.getClass().getMethod("spawn", Location.class, Class.class, Consumer.class);
            Entity tempCloud = (Entity) spawnMethod.invoke(world, location, org.bukkit.entity.AreaEffectCloud.class, consumer);
            boolean valid = tempCloud.isValid();
            tempCloud.remove();
            return valid;
        } catch (Exception e) {
            System.err.println("Error checking player sit location: " + e.getMessage());
        }
        return false;
    }

    /**
     * Creates a single seat entity (an invisible ArmorStand) at the specified location and adds the given entity as its passenger.
     * The seat is tagged with "SimpleSeatEntity" for later cleanup.
     */
    public Entity createSingleSeatEntity(Location location, Entity entity, boolean canRotate) {
        try {
            final boolean[] riding = { true };

            Consumer<ArmorStand> consumer = armorStand -> {
                try {
                    armorStand.setInvisible(true);
                } catch (Throwable e) {
                    try {
                        ArmorStand.class.getMethod("setVisible", boolean.class).invoke(armorStand, false);
                    } catch (Throwable ignored) {}
                }
                try {
                    armorStand.setGravity(false);
                } catch (Throwable ignored) {}
                try {
                    armorStand.setMarker(true);
                } catch (Throwable ignored) {}
                try {
                    armorStand.setInvulnerable(true);
                } catch (Throwable ignored) {}
                try {
                    armorStand.setSmall(true);
                } catch (Throwable ignored) {}
                try {
                    armorStand.setBasePlate(false);
                } catch (Throwable ignored) {}

                // Tag the seat entity for later removal
                armorStand.addScoreboardTag("SimpleSeatEntity");

                if (entity != null && entity.isValid()) {
                    riding[0] = armorStand.addPassenger(entity);
                }
            };

            World world = location.getWorld();
            Method spawnMethod = world.getClass().getMethod("spawn", Location.class, Class.class, Consumer.class);
            Entity seatEntity = (Entity) spawnMethod.invoke(world, location, ArmorStand.class, consumer);

            if (entity != null && entity.isValid() && (!riding[0] || !seatEntity.getPassengers().contains(entity))) {
                seatEntity.remove();
                return null;
            }

            return seatEntity;
        } catch (Exception e) {
            System.err.println("Error creating seat entity: " + e.getMessage());
        }
        return null;
    }

    /**
     * Combined function: Tests the provided location for both seat and player validity,
     * then creates the seat entity if the tests pass.
     *
     * @param location  The location where the seat should be created.
     * @param entity    The entity (usually a player) that will sit on the seat.
     * @param canRotate Unused in this simplified version, kept for compatibility.
     * @return The created seat entity or null if the location is invalid.
     */
    public Entity createSeat(Location location, Entity entity, boolean canRotate) {
        if (!isSitLocationValid(location) || !isPlayerSitLocationValid(location)) {
            return null;
        }
        return createSingleSeatEntity(location, entity, canRotate);
    }

    /**
     * Makes the given entity sit down.
     * This method uses the entity's current location (adjusted with a less aggressive offset)
     * for seat creation. Returns true if the seat was successfully created and the entity is seated.
     *
     * @param entity The entity (usually a player) to sit down.
     * @return true if successful; false otherwise.
     */
    public boolean sitDown(LivingEntity entity) {
        if (entity == null || !entity.isValid()) return false;

        // Adjust the seat location with a less aggressive offset (0.5 block down instead of 1 block)
        Location seatLocation = entity.getLocation().clone();
        Entity seat = createSeat(seatLocation, entity, true);
        return seat != null;
    }

    /**
     * If the entity is already sitting, this method makes it stand up.
     * It safely dismounts the entity and then teleports it slightly upward to avoid being stuck in the block.
     *
     * @param entity The entity to unsit.
     * @return true if the entity left the seat; false otherwise.
     */
    public boolean unsit(LivingEntity entity) {
        if (entity == null || !entity.isValid()) return false;
        if (!entity.isInsideVehicle()) return false;
        boolean left = entity.leaveVehicle();
        if (left) {
            handleSafeSeatDismount(entity);
        }
        return left;
    }

    /**
     * Toggles the sit state.
     * If the entity is sitting, it will unsit (with safe dismount); if not, it will attempt to sit.
     *
     * @param entity The entity to toggle.
     * @return true if the action succeeded; false otherwise.
     */
    public boolean toggleSit(LivingEntity entity) {
        if (entity == null || !entity.isValid()) return false;
        if (entity.isInsideVehicle()) {
            return unsit(entity);
        } else {
            return sitDown(entity);
        }
    }

    /**
     * Handles safe dismount by teleporting the entity slightly upward after a short delay.
     * This prevents the entity from being stuck in a block.
     *
     * @param entity The entity to safely dismount.
     */
    public void handleSafeSeatDismount(LivingEntity entity) {
        if (entity == null || !entity.isValid()) return;
        // Calculate a safe location—here we simply add one block upward.
        Location currentLoc = entity.getLocation();
        Location safeLoc = currentLoc.clone().add(0, 1, 0);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (entity.isValid()) {
                entity.teleport(safeLoc);
            }
        }, 1L); // Delay of 1 tick
    }
}
