package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingData.NPCData;
import com.al3x.housing2.Instances.HousingData.StatData;
import com.al3x.housing2.Main;
import com.google.gson.Gson;
import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingWorld {
    private static Gson gson = new Gson();

    transient private Main main;
    transient private SlimeLoader loader;
    transient private AdvancedSlimePaperAPI asp;
    transient private World houseWorld;
    transient private SlimeWorld slimeWorld;

    // The actual owners UUID
    private UUID ownerUUID;

    // Randomly generated House UUID
    private UUID houseUUID;

    // Stats about the house. More public data
    private String name;
    private String description;
    private int size;
    private int guests;
    private double cookies;
    private long timeCreated;
    private Location spawn;
    private List<String> scoreboard;

    // NPCs
    private List<HousingNPC> housingNPCS;

    // Action Stuff
    private HashMap<EventType, List<Action>> eventActions;

    // Stats
    private StatManager statManager;

    // Random Seed and Random instance
    private String seed;
    transient private Random random;
    transient private HouseData houseData;


    //Loading a house that already exists
    public HousingWorld(Main main, Player owner, String name) {
        main.getLogger().info("Loading house for " + owner.getName() + "...");
        this.main = main;
        try {
            this.loader = main.getLoader();
            this.asp = AdvancedSlimePaperAPI.instance();
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        File file = new File(main.getDataFolder(), "houses/" + name + ".json");
        if (!file.exists()) {
            owner.sendMessage(colorize("&cFailed to load your house!"));
            return;
        }
        try {
            String json = Files.readString(file.toPath());
            houseData = gson.fromJson(json, HouseData.class);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return;
        }
        this.ownerUUID = owner.getUniqueId();
        this.name = houseData.getHouseName();
        this.houseUUID = UUID.fromString(houseData.getHouseID());
//        this.guests = houseData.getGuests();
        this.cookies = houseData.getCookies();
        this.description = houseData.getDescription();
        this.timeCreated = houseData.getTimeCreated();
        this.housingNPCS = new ArrayList<>();

        this.statManager = new StatManager(this);

        this.statManager.setPlayerStats(StatData.Companion.toHashMap(houseData.getPlayerStats()));

        this.scoreboard = houseData.getScoreboard();

        eventActions = new HashMap<>();
        for (EventType type : EventType.values()) {
            eventActions.put(type, new ArrayList<>());
            List<com.al3x.housing2.Instances.HousingData.ActionData> actions = houseData.getEventActions().get(type);
            if (actions != null) {
                for (com.al3x.housing2.Instances.HousingData.ActionData action : actions) {
                    eventActions.get(type).add(ActionEnum.getActionByName(action.getAction()).getActionInstance(action.getData()));
                }
            }
        }

        this.seed = houseData.getSeed();
        this.random = new Random(seed.hashCode());
        this.size = houseData.getSize();

        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            owner.sendMessage(colorize("&cFailed to load your house!"));
            return;
        }
        slimeWorld = world;
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);

        // Load NPCs into the world
        for (NPCData npc : houseData.getHouseNPCs()) {
            Location location = npc.getNpcLocation().toLocation();
            loadNPC(owner, location, npc); // Eventually change this to a method that creates an npc with the correct data
        }

        save();
    }

    // Creating a new house
    public HousingWorld(Main main, Player owner, HouseSize size) {
        main.getLogger().info("Creating a new house for " + owner.getName() + "...");
        this.main = main;
        try {
            this.loader = main.getLoader();
            this.asp = AdvancedSlimePaperAPI.instance();
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        // Set up the Information
        this.ownerUUID = owner.getUniqueId();
        this.name = owner.getName() + "'s House";
        this.houseUUID = UUID.randomUUID();
        this.guests = 0;
        this.cookies = 0;
        this.description = "";
        this.timeCreated = System.currentTimeMillis();
        this.housingNPCS = new ArrayList<>();
        this.statManager = new StatManager(this);

        // Set up the seed and random instance
        this.seed = UUID.randomUUID().toString();
        this.random = new Random(seed.hashCode());

        switch (size) {case MEDIUM -> this.size = 50;case LARGE -> this.size = 75;case XLARGE -> this.size = 100;case MASSIVE -> this.size = 255;default -> this.size = 30;}

        // Create the actual world

        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            owner.sendMessage(colorize("&cFailed to create your house!"));
            return;
        }
        slimeWorld = world;

        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);
        createTemplatePlatform();

        // Actions, Scoreboard, Default Stuff ya know?
        this.scoreboard = new ArrayList<>();
        this.scoreboard.add("&fName: %player.name%");
        this.scoreboard.add("&fhawk tua! Ping: %player.ping%");
        this.scoreboard.add("&fSprinting! %player.isSprinting%");
        this.scoreboard.add("&f");
        this.scoreboard.add("Twerks: &6%stat.player/twerks%");
        this.scoreboard.add("&r");
        this.scoreboard.add("&aPlayers: &2%house.guests%");
        this.scoreboard.add("&eCookies: &6%house.cookies%");

        eventActions = new HashMap<>();
        //Bad Al3x for not doing this the first time
        // flip you buddy - Al3x
        for (EventType type : EventType.values()) {
            eventActions.put(type, new ArrayList<>());
        }

        // Save the house
        save();
    }

    private void createTemplatePlatform() {
        int platformSize = 15; // 13x13 platform
        int startX = -platformSize / 2;
        int startZ = -platformSize / 2;

        for (int x = startX; x <= -startX; x++) {
            for (int z = startZ; z <= -startZ; z++) {
                // Place stone as the base (one block below the grass)
                houseWorld.getBlockAt(x, 59, z).setType(Material.STONE);

                // Place grass block on top of the stone
                houseWorld.getBlockAt(x, 60, z).setType((Math.random() > 0.25) ? Material.GRASS_BLOCK : Material.COARSE_DIRT);

                // Place grass on top of grass block
                if (Math.random() < 0.2) houseWorld.getBlockAt(x, 61, z).setType(Material.SHORT_GRASS);
            }
        }
    }

    private SlimePropertyMap getProperties() {
        SlimePropertyMap properties = new SlimePropertyMap();

        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 61);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
//            properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
//            properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
//            properties.setValue(SlimeProperties.DRAGON_BATTLE, false);
//            properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.ENVIRONMENT, "normal");
        properties.setValue(SlimeProperties.WORLD_TYPE, "default");
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "minecraft:plains");

        return properties;
    }

    private SlimeWorld createOrReadWorld() {
        SlimeWorld world = null;

        try {
            if (!loader.worldExists(houseUUID.toString())) {
                world = asp.createEmptyWorld(houseUUID.toString(), false, getProperties(), loader);
                asp.saveWorld(world);
            } else {
                world = asp.readWorld(loader, houseUUID.toString(), false, getProperties());
            }

            world = asp.loadWorld(world, true);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }

        return world;
    }

    public void save() {
        try {
            houseData = HouseData.Companion.fromHousingWorld(this);

            File file = new File(main.getDataFolder(), "houses/" + houseUUID + ".json");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            String json = gson.toJson(houseData);
            Files.write(file.toPath(), json.getBytes());

            asp.saveWorld(slimeWorld);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addEventAction(EventType eventType, Action action) {
        // Shoutout to chatgippity cause i have 0 clue what this means
        eventActions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(action);
    }

    public void executeEventActions(EventType eventType, Player player, Cancellable event) {
        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                for (Action action : actions) {
                    // Check if the action is null or if the event is allowed
                    if (action.allowedEvents() != null && !action.allowedEvents().contains(eventType)) return;
                    // Execute the action either cancelling the event or not
                    if (event != null && !action.execute(player, this)) event.setCancelled(true);
                    if (event == null) action.execute(player, this);
                }
            });
        }
    }

    public void createNPC(Player player, Location location) {
        HousingNPC npc = new HousingNPC(main, player, location, this);
        housingNPCS.add(npc);
    }

    public void loadNPC(Player player, Location location, NPCData data) {
        HousingNPC npc = new HousingNPC(main, player, location, this, data);
        housingNPCS.add(npc);
    }

    public HousingNPC getNPC(int id) {
        for (HousingNPC npc : housingNPCS) {
            if (npc.getNpcID() == id) {
                return npc;
            }
        }
        return null;
    }

    public void removeNPC(int id) {
        for (HousingNPC npc : housingNPCS) {
            if (npc.getNpcID() == id) {
                NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(id);
                if (citizensNPC == null) {
                    Bukkit.getLogger().info("NPC is null...");
                    return;
                }
                citizensNPC.destroy();
                CitizensAPI.getNPCRegistry().deregister(citizensNPC);
                housingNPCS.remove(npc);
                return;
            }
        }
    }

    public List<HousingNPC> getNPCs() {
        return housingNPCS;
    }

    // Helper Method for delete()
    private boolean deleteWorld(File path) {
        for (HousingNPC npc : housingNPCS) {
            removeNPC(npc.getNpcID());
        }

        File file = new File(main.getDataFolder(), "houses/" + houseUUID.toString() + ".json");
        if (file.exists()) {
            file.delete();
        }

        try {
            loader.deleteWorld(houseUUID.toString());
            return true;
        } catch (UnknownWorldException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (houseWorld.getPlayers().contains(player)) {
                kickPlayerFromHouse(player);
                player.sendMessage(colorize("&e&lThis house has been deleted!"));
            }
        }

        Bukkit.unloadWorld(houseWorld, false);
        deleteWorld(houseWorld.getWorldFolder());
    }

    public void broadcast(String s) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals(houseUUID)) {
                player.sendMessage(colorize(s));
            }
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setEventActions(EventType type, List<Action> actions) {
        eventActions.put(type, actions);
    }
    public List<Action> getEventActions(EventType type) {
        return eventActions.get(type);
    }
    public void sendPlayerToHouse(Player player) {
        player.teleport(spawn);
        player.sendMessage(colorize("&aSending you to " + name + "&a..."));
    }
    public StatManager getStatManager() {
        return statManager;
    }
    public void kickPlayerFromHouse(Player player) {
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
    public void incGuests() {
        guests++;
    }
    public void decGuests() {
        guests--;
    }
    public World getWorld() {
        return houseWorld;
    }
    public void setName(String s) {
        name = s;
    }
    public String getName() {
        return name;
    }
    public Long getTimeCreated() {
        return timeCreated;
    }
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    public UUID getHouseUUID() {
        return houseUUID;
    }
    public Location getSpawn() {
        return spawn;
    }
    public void setScoreboard(List<String> scoreboard) {
        this.scoreboard = scoreboard;
    }
    public List<String> getScoreboard() {
        return scoreboard;
    }
    public int getGuests() {
        return guests;
    }
    public double getCookies() {
        return cookies;
    }
    public int getSize() {
        return size;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public String getSeed() {
        return seed;
    }

    public HashMap<EventType, List<Action>> getEventActions() {
        return eventActions;
    }
}
