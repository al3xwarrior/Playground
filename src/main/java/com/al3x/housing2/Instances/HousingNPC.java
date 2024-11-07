package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousingData.LocationData;
import com.al3x.housing2.Instances.HousingData.NPCData;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.FollowTrait;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

public class HousingNPC {

    public static ItemStack getNPCItem() {
        ItemStack npc = getCustomSkull("a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        ItemMeta meta = npc.getItemMeta();
        meta.setItemName(colorize("&aNPC"));
        npc.setItemMeta(meta);
        return npc;
    }

    private NPC citizensNPC;
    private HousingWorld house;
    private Main main;

    // Npc Properties
    private int npcID;
    private UUID npcUUID;
    private UUID creatorUUID;
    private String name;
    private boolean lookAtPlayer;
    private Location location;
    private EntityType entityType;
    
    // Equipment
    private ItemStack hand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    //Navigation properties
    private NavigationType navigationType;
    private NavigationType previousNavigationType;
    private double speed;

    private List<Action> actions;

    private String[] npcNames = {"&aAlex", "&2Baldrick", "&cD&6i&ed&ad&by", "&5Ben Dover", "&7Loading...", "&eUpdog", "&cConnorLinfoot", "&bCookie Monster", "&c❤"};

    public HousingNPC(Main main, OfflinePlayer player, Location location, HousingWorld house, NPCData data) {
        this.main = main;
        this.house = house;
        this.name = data.getNpcName();
        this.lookAtPlayer = data.getLookAtPlayer();
        this.location = location;
        this.npcID = data.getNpcID();
        this.npcUUID = UUID.fromString(data.getNpcUUID());
        this.entityType = EntityType.valueOf(data.getNpcType());

        this.creatorUUID = player.getUniqueId();
        this.actions = Companion.toList(data.getActions());


        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, npcUUID, npcID, this.name);
        LookClose trait = citizensNPC.getOrAddTrait(LookClose.class);
        if (lookAtPlayer && !trait.isEnabled()) trait.toggle(); else if (!lookAtPlayer && trait.isEnabled()) trait.toggle();

        Equipment equipment = citizensNPC.getOrAddTrait(Equipment.class);
        if (data.getEquipment() != null) {
            List<String> dataEquipment = data.getEquipment();
            for (int i = 0; i < dataEquipment.size(); i++) {
                String base64 = dataEquipment.get(i);
                if (base64 != null) {
                    try {
                        ItemStack item = Serialization.itemStackFromBase64(base64);
                        switch (i) {
                            case 0: helmet = item; equipment.set(Equipment.EquipmentSlot.HELMET, item); break;
                            case 1: chestplate = item; equipment.set(Equipment.EquipmentSlot.CHESTPLATE, item); break;
                            case 2: leggings = item; equipment.set(Equipment.EquipmentSlot.LEGGINGS, item); break;
                            case 3: boots = item; equipment.set(Equipment.EquipmentSlot.BOOTS, item); break;
                            case 4: hand = item; equipment.set(Equipment.EquipmentSlot.HAND, item); break;
                            case 5: offHand = item; equipment.set(Equipment.EquipmentSlot.OFF_HAND, item); break;
                        }
                    } catch (IOException e) {
                        main.getLogger().warning("Failed to load equipment for NPC: " + name);
                    }
                }
            }
        }

        navigationType = data.getNavigationType() == null ? NavigationType.STATIONARY : NavigationType.valueOf(data.getNavigationType());
        speed = data.getSpeed() == null ? 1.0 : data.getSpeed();

        citizensNPC.getNavigator().getDefaultParameters().speedModifier((float) speed);
        // Set waypoints
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        waypoints.setWaypointProvider("linear");
        if (data.getWaypoints() != null) {
            List<Location> dataWaypoints = data.getWaypoints().stream().map(LocationData::toLocation).toList();
            for (Location loc : dataWaypoints) {
                ((LinearWaypointProvider) waypoints.getCurrentProvider()).addWaypoint(new Waypoint(loc));
            }
        }
        if (navigationType == NavigationType.WANDER) {
            waypoints.setWaypointProvider("wander");
            waypoints.getCurrentProvider().setPaused(false);
        } else if (navigationType == NavigationType.PATH) {
            waypoints.setWaypointProvider("linear");
            waypoints.getCurrentProvider().setPaused(false);
        } else {
            waypoints.getCurrentProvider().setPaused(true);
        }


        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (citizensNPC.isSpawned()) {

                    if (navigationType == NavigationType.PLAYER) {
                        FollowTrait followTrait = citizensNPC.getOrAddTrait(FollowTrait.class);
                        followTrait.setProtect(false);
                        //get closest player to the npc but only if they are 20 blocks away
                        Player closest = null;
                        double closestDistance = 20;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getWorld().equals(citizensNPC.getEntity().getWorld())) {
                                double distance = player.getLocation().distance(citizensNPC.getEntity().getLocation());
                                if (distance < closestDistance) {
                                    closest = player;
                                    closestDistance = distance;
                                }
                            }
                        }

                        if (closest != null) {
                            followTrait.follow(closest);
                        }
                    } else {
                        FollowTrait followTrait = citizensNPC.getOrAddTrait(FollowTrait.class);
                        followTrait.setProtect(false);
                        followTrait.follow(null);
                    }
                }
            }
        };

        runnable.runTaskTimer(main, 0, 20);

        citizensNPC.spawn(location);
    }

    public HousingNPC(Main main, Player player, Location location, HousingWorld house) {
        this.main = main;
        this.house = house;
        this.name = npcNames[new Random().nextInt(npcNames.length)];
        this.lookAtPlayer = true;
        this.location = location;
        this.creatorUUID = player.getUniqueId();
        this.entityType = EntityType.PLAYER;

        this.speed = 1.0;
        this.navigationType = NavigationType.STATIONARY;

        this.actions = new ArrayList<>();

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, this.name);
        LookClose trait = citizensNPC.getOrAddTrait(LookClose.class);
        if (lookAtPlayer && !trait.isEnabled()) trait.toggle(); else if (!lookAtPlayer && trait.isEnabled()) trait.toggle();
        citizensNPC.spawn(location);
        citizensNPC.faceLocation(player.getLocation());
        // Update it because weird
        this.npcID = citizensNPC.getId();
        this.npcUUID = citizensNPC.getUniqueId();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (citizensNPC.isSpawned()) {

                    if (navigationType == NavigationType.PLAYER) {
                        FollowTrait followTrait = citizensNPC.getOrAddTrait(FollowTrait.class);
                        followTrait.setProtect(false);
                        //get closest player to the npc but only if they are 20 blocks away
                        Player closest = null;
                        double closestDistance = 20;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getWorld().equals(citizensNPC.getEntity().getWorld())) {
                                double distance = player.getLocation().distance(citizensNPC.getEntity().getLocation());
                                if (distance < closestDistance) {
                                    closest = player;
                                    closestDistance = distance;
                                }
                            }
                        }

                        if (closest != null) {
                            followTrait.follow(closest);
                        }
                    } else {
                        FollowTrait followTrait = citizensNPC.getOrAddTrait(FollowTrait.class);
                        followTrait.setProtect(false);
                        followTrait.follow(null);
                    }
                }
            }
        };

        runnable.runTaskTimer(main, 0, 20);
    }

    public void setEntity(EntityType entityType) {
        this.entityType = entityType;
        citizensNPC.getEntity().setMetadata("NPC", new FixedMetadataValue(main, true));
        citizensNPC.setBukkitEntityType(entityType);
    }

    public void sendExecuteActions(HousingWorld house, Player player) {
        if (actions != null) {
            for (Action action : actions) {
                action.execute(player, house, null);
            }
        }
    }

    public void setHologramLines(List<String> lines) {
        HologramTrait hologram = citizensNPC.getOrAddTrait(HologramTrait.class);
        hologram.clear();

        for (String line : lines) {
            hologram.addLine(line);
        }

        citizensNPC.addTrait(hologram);
    }

    public List<String> getHologramLines() {
        return citizensNPC.getOrAddTrait(HologramTrait.class).getLines();
    }

    public List<String> getHolograms() {
        return citizensNPC.getOrAddTrait(HologramTrait.class).getLines();
    }

    public NPC getCitizensNPC() {
        return citizensNPC;
    }

    public int getNpcID() {
        return npcID;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public List<Action> getActions() {
        return actions;
    }

    public HousingWorld getHouse() {
        return house;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        if (citizensNPC.isSpawned()) return citizensNPC.getEntity().getLocation();
        return location;
    }

    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    public void setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
        LookClose trait = citizensNPC.getOrAddTrait(LookClose.class);
        if (trait.isEnabled() != lookAtPlayer) trait.toggle();
    }

    public void setName(String newMessage) {
        this.name = newMessage;
        citizensNPC.setName(newMessage);
    }

    public void setEquipment(String type, ItemStack item) {
        Equipment equipment = citizensNPC.getOrAddTrait(Equipment.class);
        switch (type) {
            case "Helmet": helmet = item; equipment.set(Equipment.EquipmentSlot.HELMET, item); break;
            case "Chestplate": chestplate = item; equipment.set(Equipment.EquipmentSlot.CHESTPLATE, item); break;
            case "Leggings": leggings = item; equipment.set(Equipment.EquipmentSlot.LEGGINGS, item); break;
            case "Boots": boots = item; equipment.set(Equipment.EquipmentSlot.BOOTS, item); break;
            case "Main Hand": hand = item; equipment.set(Equipment.EquipmentSlot.HAND, item); break;
            case "Off Hand": offHand = item; equipment.set(Equipment.EquipmentSlot.OFF_HAND, item); break;
        }
    }

    public ItemStack getEquipment(String type) {
        switch (type) {
            case "Helmet": return helmet;
            case "Chestplate": return chestplate;
            case "Leggings": return leggings;
            case "Boots": return boots;
            case "Main Hand": return hand;
            case "Off Hand": return offHand;
        }
        return null;
    }

    public List<String> getEquipment() {
        List<String> equipment = new ArrayList<>();
        if (helmet != null) equipment.add(Serialization.itemStackToBase64(helmet)); else equipment.add(null);
        if (chestplate != null) equipment.add(Serialization.itemStackToBase64(chestplate)); else equipment.add(null);
        if (leggings != null) equipment.add(Serialization.itemStackToBase64(leggings)); else equipment.add(null);
        if (boots != null) equipment.add(Serialization.itemStackToBase64(boots)); else equipment.add(null);
        if (hand != null) equipment.add(Serialization.itemStackToBase64(hand)); else equipment.add(null);
        if (offHand != null) equipment.add(Serialization.itemStackToBase64(offHand)); else equipment.add(null);
        return equipment;
    }

    public NavigationType getNavigationType() {
        return navigationType;
    }

    public NavigationType getPreviousNavigationType() {
        return previousNavigationType;
    }

    public void setNavigationType(NavigationType mode) {
        this.previousNavigationType = this.navigationType;
        this.navigationType = mode;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public void addPath(Location loc) {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (!(waypoints.getCurrentProvider() instanceof LinearWaypointProvider)) return;

        LinearWaypointProvider provider = (LinearWaypointProvider) waypoints.getCurrentProvider();
        provider.addWaypoint(new Waypoint(loc));
    }

    public Location removePath() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (!(waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider)) return null;
        AbstractList<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
        if (path.isEmpty()) return null;
        Location loc = path.get(path.size() - 1).getLocation();
        path.remove(path.size() - 1);
        return loc;
    }

    public AbstractList<Waypoint> getPath() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (!(waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider)) return null;
        return (AbstractList<Waypoint>) provider.waypoints();
    }

    public List<Location> getWaypoints() {
        List<Location> waypoints = new ArrayList<>();
        if (getPath() == null) return waypoints;
        for (Waypoint waypoint : getPath()) {
            waypoints.add(waypoint.getLocation());
        }
        return waypoints;
    }
}
