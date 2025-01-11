package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousingData.LocationData;
import com.al3x.housing2.Instances.HousingData.NPCData;
import com.al3x.housing2.Main;
import com.al3x.housing2.MineSkin.BiggerSkinData;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.Utils.Serialization;
import com.google.gson.Gson;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.FollowTrait;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;
import static com.al3x.housing2.MineSkin.MineskinHandler.getSkinData;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

public class HousingNPC {

    private static final Gson gson = new Gson();

    public static ItemStack getNPCItem() {
        ItemStack npc = getCustomSkull("a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        ItemMeta meta = npc.getItemMeta();
        meta.setDisplayName(colorize("&aNPC"));
        npc.setItemMeta(meta);
        return npc;
    }

    public static List<SkinData> loadedSkins = new ArrayList<>();

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
    private String skinUUID;

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

    private static final String[] NPC_NAMES = {"&aAlex", "&2Baldrick", "&cD&6i&ed&ad&by", "&5Ben Dover", "&7Loading...", "&eUpdog", "&cConnorLinfoot", "&bCookie Monster", "&c❤"};

    public HousingNPC(Main main, OfflinePlayer player, Location location, HousingWorld house, NPCData data) {
        long start = System.currentTimeMillis();

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
        configureLookCloseTrait();
        configureEquipment(data.getEquipment());
        configureNavigation(data);

        setSkin(data.getNpcSkin());

        citizensNPC.spawn(location);
        startFollowTask();

    }

    public HousingNPC(Main main, Player player, Location location, HousingWorld house) {
        this.main = main;
        this.house = house;
        this.name = NPC_NAMES[new Random().nextInt(NPC_NAMES.length)];
        this.lookAtPlayer = true;
        this.location = location;
        this.creatorUUID = player.getUniqueId();
        this.entityType = EntityType.PLAYER;
        this.speed = 1.0;
        this.navigationType = NavigationType.STATIONARY;
        this.actions = new ArrayList<>();

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, this.name);
        configureLookCloseTrait();
        citizensNPC.spawn(location);
        citizensNPC.faceLocation(player.getLocation());
        this.npcID = citizensNPC.getId();
        this.npcUUID = citizensNPC.getUniqueId();

        startFollowTask();
    }

    private void configureLookCloseTrait() {
        LookClose trait = citizensNPC.getOrAddTrait(LookClose.class);
        if (lookAtPlayer && !trait.isEnabled()) trait.toggle();
        else if (!lookAtPlayer && trait.isEnabled()) trait.toggle();
    }

    private void configureEquipment(List<String> dataEquipment) {
        Equipment equipment = citizensNPC.getOrAddTrait(Equipment.class);
        if (dataEquipment != null) {
            for (int i = 0; i < dataEquipment.size(); i++) {
                String base64 = dataEquipment.get(i);
                if (base64 != null) {
                    try {
                        ItemStack item = Serialization.itemStackFromBase64(base64);
                        setEquipmentSlot(equipment, i, item);
                    } catch (IOException e) {
                        main.getLogger().warning("Failed to load equipment for NPC: " + name);
                    }
                }
            }
        }
    }

    private void setEquipmentSlot(Equipment equipment, int slot, ItemStack item) {
        switch (slot) {
            case 0 -> helmet = item;
            case 1 -> chestplate = item;
            case 2 -> leggings = item;
            case 3 -> boots = item;
            case 4 -> hand = item;
            case 5 -> offHand = item;
        }
        equipment.set(Equipment.EquipmentSlot.values()[slot], item);
    }

    private void configureNavigation(NPCData data) {
        navigationType = data.getNavigationType() == null ? NavigationType.STATIONARY : NavigationType.valueOf(data.getNavigationType());
        speed = data.getSpeed() == null ? 1.0 : data.getSpeed();
        citizensNPC.getNavigator().getDefaultParameters().speedModifier((float) speed);
        configureWaypoints(data.getWaypoints());
        updateNavigationState();
    }

    private void configureWaypoints(List<LocationData> dataWaypoints) {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        waypoints.setWaypointProvider("linear");
        if (dataWaypoints != null) {
            for (Location loc : dataWaypoints.stream().map(LocationData::toLocation).toList()) {
                ((LinearWaypointProvider) waypoints.getCurrentProvider()).addWaypoint(new Waypoint(loc));
            }
        }
    }

    private void updateNavigationState() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (navigationType == NavigationType.WANDER) {
            waypoints.setWaypointProvider("wander");
            waypoints.getCurrentProvider().setPaused(false);
            citizensNPC.getNavigator().setPaused(false);
        } else if (navigationType == NavigationType.PATH) {
            waypoints.setWaypointProvider("linear");
            waypoints.getCurrentProvider().setPaused(false);
            citizensNPC.getNavigator().setPaused(false);
        } else {
            //I dont know why I need to do this, but it works :shrug:
            Bukkit.getScheduler().runTaskLater(main, () -> {
                waypoints.getCurrentProvider().setPaused(true);
                citizensNPC.getNavigator().setPaused(true);
            }, 1);
        }
    }

    private void startFollowTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (citizensNPC.isSpawned()) {
                    if (citizensNPC.getEntity().getLocation().getY() < -64) {
                        citizensNPC.despawn();
                        citizensNPC.spawn(location);
                    }

                    FollowTrait followTrait = citizensNPC.getOrAddTrait(FollowTrait.class);
                    followTrait.setProtect(false);
                    if (navigationType == NavigationType.PLAYER) {
                        Player closest = getClosestPlayer();
                        if (closest != null) {
                            followTrait.follow(closest);
                        }
                    } else {
                        followTrait.follow(null);
                    }
                }
            }
        }.runTaskTimer(main, 0, 20);
    }

    private Player getClosestPlayer() {
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
        return closest;
    }

    public void setEntity(EntityType entityType) {
        this.entityType = entityType;
        citizensNPC.getEntity().setMetadata("NPC", new FixedMetadataValue(main, true));
        citizensNPC.setBukkitEntityType(entityType);
    }

    public void sendExecuteActions(HousingWorld house, Player player) {
        if (actions != null) {
            new ActionExecutor(actions).execute(player, house, null);
        }
    }

    public void setHologramLines(List<String> lines) {
        HologramTrait hologram = citizensNPC.getOrAddTrait(HologramTrait.class);
        hologram.clear();
        lines.forEach(hologram::addLine);
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

    public String getSkinUUID() {
        return skinUUID;
    }

    public Location getLocation() {
        return citizensNPC.isSpawned() ? citizensNPC.getEntity().getLocation() : location;
    }

    public void setLocation(Location location) {
        this.location = location;
        citizensNPC.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    public void setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
        configureLookCloseTrait();
    }

    public void setName(String newMessage) {
        this.name = newMessage;
        citizensNPC.setName(newMessage);
    }

    public void setEquipment(String type, ItemStack item) {
        Equipment equipment = citizensNPC.getOrAddTrait(Equipment.class);
        switch (type) {
            case "Helmet" -> helmet = item;
            case "Chestplate" -> chestplate = item;
            case "Leggings" -> leggings = item;
            case "Boots" -> boots = item;
            case "Main Hand" -> hand = item;
            case "Off Hand" -> offHand = item;
        }
        if (type.equals("Main Hand")) {
            equipment.set(Equipment.EquipmentSlot.HAND, item);
        } else if (type.equals("Off Hand")) {
            equipment.set(Equipment.EquipmentSlot.OFF_HAND, item);
        } else {
            equipment.set(Equipment.EquipmentSlot.valueOf(type.toUpperCase().replace(" ", "_")), item);
        }
    }

    public ItemStack getEquipment(String type) {
        return switch (type) {
            case "Helmet" -> helmet;
            case "Chestplate" -> chestplate;
            case "Leggings" -> leggings;
            case "Boots" -> boots;
            case "Main Hand" -> hand;
            case "Off Hand" -> offHand;
            default -> null;
        };
    }

    public List<String> getEquipment() {
        return Arrays.asList(
                helmet != null ? Serialization.itemStackToBase64(helmet) : null,
                chestplate != null ? Serialization.itemStackToBase64(chestplate) : null,
                leggings != null ? Serialization.itemStackToBase64(leggings) : null,
                boots != null ? Serialization.itemStackToBase64(boots) : null,
                hand != null ? Serialization.itemStackToBase64(hand) : null,
                offHand != null ? Serialization.itemStackToBase64(offHand) : null
        );
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
        if (waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider) {
            provider.addWaypoint(new Waypoint(loc));
        }
    }

    public Location removePath() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider) {
            List<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
            if (!path.isEmpty()) {
                return path.remove(path.size() - 1).getLocation();
            }
        }
        return null;
    }

    public List<Waypoint> getPath() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider) {
            return (AbstractList<Waypoint>) provider.waypoints();
        }
        return null;
    }

    public List<Location> getWaypoints() {
        List<Location> waypoints = new ArrayList<>();
        if (getPath() != null) {
            for (Waypoint waypoint : getPath()) {
                waypoints.add(waypoint.getLocation());
            }
        }
        return waypoints;
    }

    public void setSkin(String skin) {
        this.skinUUID = skin;
        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            BiggerSkinData skinData = getSkinData(skin);
            main.getServer().getScheduler().runTask(main, () -> {
                if (skinData != null && skinData.getTexture() != null) {
                    SkinTrait skinTrait = citizensNPC.getOrAddTrait(SkinTrait.class);
                    skinTrait.setSkinPersistent(skinData.getUuid(), skinData.getTexture().getData().getSignature(), skinData.getTexture().getData().getValue());
                }
            });
        });
    }


}