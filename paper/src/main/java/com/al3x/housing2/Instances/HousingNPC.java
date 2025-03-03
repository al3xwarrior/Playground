package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousingData.HologramData;
import com.al3x.housing2.Instances.HousingData.LocationData;
import com.al3x.housing2.Instances.HousingData.NPCData;
import com.al3x.housing2.Instances.HousingData.StatData;
import com.al3x.housing2.Instances.npc.FollowTrait;
import com.al3x.housing2.Instances.npc.RespawnTrait;
import com.al3x.housing2.Main;
import com.al3x.housing2.MineSkin.BiggerSkinData;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.Utils.Serialization;
import com.google.gson.Gson;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.trait.versioned.DisplayTrait;
import net.citizensnpcs.trait.versioned.TextDisplayTrait;
import net.citizensnpcs.trait.waypoint.LinearWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
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
    public boolean deleted;

    public static ItemStack getNPCItem() {
        ItemStack npc = getCustomSkull("a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        ItemMeta meta = npc.getItemMeta();
        meta.setDisplayName(colorize("&aNPC"));
        npc.setItemMeta(meta);
        return npc;
    }

    public static List<SkinData> loadedSkins = new ArrayList<>();

    private boolean spawned = false;
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

    private boolean canBeDamaged;
    private int respawnTime = 20;
    private double maxHealth = 20;
    private boolean minecraftAI;
    private boolean isBaby;

    // Equipment
    private ItemStack hand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private List<Location> waypoints;

    //Navigation properties
    private NavigationType navigationType;
    private NavigationType previousNavigationType;
    private double speed;

    private Hologram hologram;

    private List<Action> actions;
    private HashMap<EventType, List<Action>> eventActions = new HashMap<>();

    private List<Stat> stats = new ArrayList<>();

    private static final String[] NPC_NAMES = {"&aAlex", "&2Baldrick", "&cD&6i&ed&ad&by", "&5Ben Dover", "&7Loading...", "&eUpdog", "&cConnorLinfoot", "&bCookie Monster", "&c❤"};
    private static final EventType[] NPC_EVENTS = {EventType.NPC_DEATH, EventType.NPC_DAMAGE};
    public HousingNPC(Main main, OfflinePlayer player, Location location, HousingWorld house, NPCData data) {
        this.main = main;
        this.house = house;
        this.name = data.getNpcName();
        this.lookAtPlayer = data.getLookAtPlayer();
        this.location = location;
        this.entityType = EntityType.valueOf(data.getNpcType());
        this.creatorUUID = player.getUniqueId();
        this.actions = Companion.toList(data.getActions());
        this.stats = StatData.Companion.toList(data.getStats() == null ? new ArrayList<>() : data.getStats());

        this.eventActions = new HashMap<>();
        if (data.getEventActions() == null) {
            for (EventType type : NPC_EVENTS) {
                this.eventActions.put(type, new ArrayList<>());
            }
        } else {
            for (EventType type : NPC_EVENTS) {
                if (data.getEventActions().get(type.name()) == null) {
                    this.eventActions.put(type, new ArrayList<>());
                    continue;
                }
                this.eventActions.put(type, Companion.toList(data.getEventActions().get(type.name())));
            }
        }

        this.hologram = data.getHologramData() != null ? HologramData.Companion.toData(data.getHologramData()) : new Hologram(
                main, null, house, location.clone().add(0, 2.5, 0)
        );
        this.canBeDamaged = data.getCanBeDamaged();
        this.maxHealth = data.getMaxHealth();
        this.minecraftAI = data.getMinecraftAI();
        this.respawnTime = data.getRespawnTime();
        this.isBaby = data.isBaby();
        this.hologram.setHouse(house);

        if (data.getWaypoints() != null) this.waypoints = data.getWaypoints().stream().map(LocationData::toLocation).toList();
        else this.waypoints = new ArrayList<>();

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, this.name, this.location);
        this.npcID = citizensNPC.getId();
        this.npcUUID = citizensNPC.getUniqueId();
        configureLookCloseTrait();
        configureEquipment(data.getEquipment());
        configureNavigation(data);
        configureEntitySettings();
        setSkin(data.getNpcSkin());
        citizensNPC.setProtected(!isCanBeDamaged());
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

        this.eventActions = new HashMap<>();
        for (EventType type : NPC_EVENTS) {
            this.eventActions.put(type, new ArrayList<>());
        }
        this.stats = new ArrayList<>();

        this.waypoints = new ArrayList<>();

        this.canBeDamaged = false;
        this.minecraftAI = false;
        this.maxHealth = 20;
        this.respawnTime = 20;
        this.isBaby = false;

        this.hologram = new Hologram(main, player, house, location.clone().add(0, 2.5, 0));
        this.hologram.removeLine(1);
        this.hologram.removeLine(0);

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, this.name);
        configureLookCloseTrait();
        configureEntitySettings();
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
        if (dataEquipment != null) {
            for (int i = 0; i < dataEquipment.size(); i++) {
                String base64 = dataEquipment.get(i);
                if (base64 != null) {
                    try {
                        ItemStack item = Serialization.itemStackFromBase64(base64);
                        setEquipmentSlot(i, item);
                    } catch (IOException e) {
                        main.getLogger().warning("Failed to load equipment for NPC: " + name);
                    }
                }
            }
        }
    }

    private void setEquipmentSlot(int slot, ItemStack item) {
        String name = switch (slot) {
            case 0 -> "Helmet";
            case 1 -> "Chestplate";
            case 2 -> "Leggings";
            case 3 -> "Boots";
            case 4 -> "Main Hand";
            case 5 -> "Off Hand";
            default -> null;
        };
        if (name != null) {
            setEquipment(name, item);
        }
    }

    private void configureNavigation(NPCData data) {
        navigationType = data.getNavigationType() == null ? NavigationType.STATIONARY : NavigationType.valueOf(data.getNavigationType());
        speed = data.getSpeed() == null ? 1.0 : data.getSpeed();
        citizensNPC.getNavigator().getDefaultParameters().speedModifier((float) speed);
        configureWaypoints(data.getWaypoints());
        updateNavigationState();

        if (citizensNPC.hasTrait(FollowTrait.class)) citizensNPC.removeTrait(FollowTrait.class);
        citizensNPC.addTrait(new FollowTrait(this));
    }

    public void configureEntitySettings() {
        citizensNPC.getOrAddTrait(ScaledMaxHealthTrait.class).setMaxHealth(maxHealth);
        citizensNPC.setUseMinecraftAI(minecraftAI);
        citizensNPC.setProtected(!canBeDamaged);
        citizensNPC.getOrAddTrait(Age.class).setAge(isBaby ? -1 : 0);

        if (citizensNPC.hasTrait(RespawnTrait.class)) citizensNPC.removeTrait(RespawnTrait.class);
        citizensNPC.addTrait(new RespawnTrait(this));
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
                if (deleted) {
                    citizensNPC.despawn();
                    citizensNPC.destroy();
                    cancel();
                    return;
                }
                if (citizensNPC.isSpawned()) {
                    if (hologram != null) {
                        Location loc = citizensNPC.getEntity().getLocation().clone();
                        hologram.setLocation(loc.set(loc.getX(), hologram.getLocation().getY(), loc.getZ()));
                    }
                }
            }
        }.runTaskTimer(main, 0, 1);
    }

    public void setEntity(EntityType entityType) {
        this.entityType = entityType;
        citizensNPC.getEntity().setMetadata("NPC", new FixedMetadataValue(main, true));
        citizensNPC.setBukkitEntityType(entityType);
    }

    public void sendExecuteActions(HousingWorld house, Player player, Cancellable event) {
        if (actions != null) {
            ActionExecutor executor = new ActionExecutor("npc");
            executor.addActions(actions);
            executor.execute(player, house, event);
        }
    }

    public boolean isCanBeDamaged() {
        return canBeDamaged;
    }

    public NPC getCitizensNPC() {
        return citizensNPC;
    }

    public HashMap<EventType, List<Action>> getEventActions() {
        return eventActions;
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

    public List<Action> getActions(EventType type) {
        if (!eventActions.containsKey(type)) {
            eventActions.put(type, new ArrayList<>());
        }
        return eventActions.get(type);
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

    public Hologram getHologram() {
        return hologram;
    }

    public String getSkinUUID() {
        return skinUUID;
    }

    public Location getLocation() {
        return citizensNPC.isSpawned() ? citizensNPC.getEntity().getLocation() : location;
    }

    public void setCanBeDamaged(boolean canBeDamaged) {
        this.canBeDamaged = canBeDamaged;
        this.citizensNPC.setProtected(!canBeDamaged);
        this.citizensNPC.despawn();
        this.citizensNPC.spawn(location);
    }

    public boolean executeEventActions(HousingWorld house, EventType eventType, Player player, Cancellable event) {
        if (house.isAdminMode(player)) return false;

        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            ActionExecutor executor = new ActionExecutor("event");
            executor.addActions(actions);
            executor.execute(player, house, event);
            return event != null && event.isCancelled();
        }
        return false;
    }

    public void setMaxHealth(double health) {
        this.maxHealth = health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMinecraftAI(boolean ai) {
        this.minecraftAI = ai;
    }

    public boolean getMinecraftAI() {
        return minecraftAI;
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

            List<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
            this.waypoints = new ArrayList<>(path.stream().map(Waypoint::getLocation).toList());
        }
    }

    public Location removePath() {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider) {
            List<Waypoint> path = (AbstractList<Waypoint>) provider.waypoints();
            if (!path.isEmpty()) {
                this.waypoints = new ArrayList<>(path.stream().map(Waypoint::getLocation).toList());
            }
            return path.remove(path.size() - 1).getLocation();
        }
        return null;
    }

    public List<Waypoint> getPath() {
        try {
        Waypoints waypoints = citizensNPC.getOrAddTrait(Waypoints.class);
        if (waypoints.getCurrentProvider() instanceof LinearWaypointProvider provider) {
            return (AbstractList<Waypoint>) provider.waypoints();
        }
        return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Location> getWaypoints() {
        List<Location> waypoints = new ArrayList<>();
        if (Bukkit.isPrimaryThread()) {
            if (getPath() != null) {
                for (Waypoint waypoint : getPath()) {
                    waypoints.add(waypoint.getLocation());
                }
            } else {
                waypoints = new ArrayList<>();
            }
        } else {
            waypoints = this.waypoints != null ? this.waypoints : new ArrayList<>();
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

    public int getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public boolean isBaby() {
        return isBaby;
    }

    public void setBaby(boolean baby) {
        isBaby = baby;
        citizensNPC.getOrAddTrait(Age.class).setAge(baby ? -1 : 0);
    }
}