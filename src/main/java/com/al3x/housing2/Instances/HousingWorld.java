package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousingData.*;
import com.al3x.housing2.Main;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingWorld {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private transient Main main;
    private transient SlimeLoader loader;
    private transient AdvancedSlimePaperAPI asp;
    private transient World houseWorld;
    private transient SlimeWorld slimeWorld;

    private UUID ownerUUID;
    private UUID houseUUID;
    private String name;
    private String description;
    private int size;
    private int guests;
    private int cookies;
    private long timeCreated;
    private Location spawn;
    private List<String> scoreboard;
    private HousePrivacy privacy;
    private Material icon;
    private ConcurrentHashMultiset<HousingNPC> housingNPCS;
    private List<Hologram> holograms;
    private HashMap<EventType, List<Action>> eventActions;
    private List<Function> functions;
    private StatManager statManager;
    private List<Command> commands;
    private List<Region> regions;
    private List<Layout> layouts;
    private String seed;
    private Random random;
    public HouseData houseData;

    public HousingWorld(Main main, OfflinePlayer owner, String houseID) {
        initialize(main, owner, null);
        loadHouseData(owner, houseID);
        setupHouseData(owner);
        loadWorld(owner);
        setupDataAfterLoad(owner);
        loadNPCs(owner);
        // loadHolograms(); still not 100% sure how saving and crap works - al3x
        save();
    }

    public HousingWorld(Main main, Player owner, HouseSize size) {
        initialize(main, owner, owner.getName() + "'s House");
        setupNewHouse(owner, size);
        createTemplatePlatform();
        setupWorldBorder();
        setupDefaultScoreboard();
        save();
    }

    private void initialize(Main main, OfflinePlayer owner, String name) {
        this.main = main;
        this.name = name;
        this.ownerUUID = owner.getUniqueId();
        this.housingNPCS = ConcurrentHashMultiset.create();
        this.eventActions = new HashMap<>();
        this.functions = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.regions = new ArrayList<>();
        this.layouts = new ArrayList<>();
        this.holograms = new ArrayList<>();
        this.statManager = new StatManager(this);
        try {
            this.loader = main.getLoader();
            this.asp = AdvancedSlimePaperAPI.instance();
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void loadHouseData(OfflinePlayer owner, String name) {
        File file = new File(main.getDataFolder(), "houses/" + name + ".json");
        if (!file.exists()) {
            notifyOwnerOfFailure(owner);
            return;
        }
        try {
            String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            houseData = GSON.fromJson(json, HouseData.class);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void setupHouseData(OfflinePlayer owner) {
        this.houseUUID = UUID.fromString(houseData.getHouseID());
        this.name = houseData.getHouseName();
        this.cookies = (int) houseData.getCookies();
        this.description = houseData.getDescription();
        this.timeCreated = houseData.getTimeCreated();
        this.privacy = houseData.getPrivacy() != null ? HousePrivacy.valueOf(houseData.getPrivacy()) : HousePrivacy.PRIVATE;
        this.icon = houseData.getIcon() != null ? Material.valueOf(houseData.getIcon()) : Material.OAK_DOOR;
        this.statManager.setPlayerStats(StatData.Companion.toHashMap(houseData.getPlayerStats()));
        this.statManager.setGlobalStats(StatData.Companion.toList(houseData.getGlobalStats()));
        this.commands = houseData.getCommands() != null ? CommandData.Companion.toList(houseData.getCommands()) : new ArrayList<>();
        this.layouts = houseData.getLayouts() != null ? LayoutData.Companion.toList(houseData.getLayouts()) : new ArrayList<>();
        this.scoreboard = houseData.getScoreboard();
        loadEventActions();
        this.functions = houseData.getFunctions() != null ? FunctionData.Companion.toList(houseData.getFunctions()) : new ArrayList<>();
        this.seed = houseData.getSeed();
        this.random = new Random(seed.hashCode());
        this.size = houseData.getSize();
    }

    private void setupDataAfterLoad(OfflinePlayer owner) {
        this.regions = houseData.getRegions() != null ? RegionData.Companion.toList(houseData.getRegions()) : new ArrayList<>();
        this.holograms = houseData.getHolograms() != null ? HologramData.Companion.toList(houseData.getHolograms(), this) : new ArrayList<>();
    }

    private void loadEventActions() {
        for (EventType type : EventType.values()) {
            eventActions.put(type, new ArrayList<>());
            List<ActionData> actions = houseData.getEventActions().get(type);
            if (actions != null) {
                for (ActionData action : actions) {
                    eventActions.get(type).add(ActionEnum.getActionByName(action.getAction()).getActionInstance(action.getData()));
                }
            }
        }
    }

    private void loadWorld(OfflinePlayer owner) {
        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            notifyOwnerOfFailure(owner);
            return;
        }
        slimeWorld = world;
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);
    }

    private void loadNPCs(OfflinePlayer owner) {
        for (NPCData npc : houseData.getHouseNPCs()) {
            Location location = npc.getNpcLocation().toLocation();
            loadNPC(owner, location, npc);
        }
    }

    private void setupNewHouse(Player owner, HouseSize size) {
        this.houseUUID = UUID.randomUUID();
        ensureUniqueHouseUUID();
        this.description = "&7This is a default description!";
        this.timeCreated = System.currentTimeMillis();
        this.privacy = HousePrivacy.PRIVATE;
        this.icon = Material.OAK_DOOR;
        this.seed = UUID.randomUUID().toString();
        this.random = new Random(seed.hashCode());
        this.size = determineHouseSize(size);
        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            owner.sendMessage(colorize("&cFailed to create your house!"));
            return;
        }
        slimeWorld = world;
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);
    }

    private void ensureUniqueHouseUUID() {
        if (main.getHousesManager().getHouseData(houseUUID.toString()) != null) {
            this.houseUUID = UUID.randomUUID();
        }
    }

    private int determineHouseSize(HouseSize size) {
        return switch (size) {
            case MEDIUM -> 50;
            case LARGE -> 75;
            case XLARGE -> 100;
            case MASSIVE -> 255;
            default -> 30;
        };
    }

    private void setupWorldBorder() {
        this.houseWorld.getWorldBorder().setCenter(this.spawn);
        this.houseWorld.getWorldBorder().setSize(this.size);
    }

    private void setupDefaultScoreboard() {
        this.scoreboard = new ArrayList<>();
        this.scoreboard.add("%house.name%:");
        this.scoreboard.add("&7- &eCookies: &6%house.cookies%");
        this.scoreboard.add("&7- &aPlayers: &2%house.guests%");
        this.scoreboard.add("&7");
        this.scoreboard.add("&7Edit the scoreboard in the");
        this.scoreboard.add("&7systems menu!");
    }

    private void notifyOwnerOfFailure(OfflinePlayer owner) {
        if (owner.isOnline()) {
            owner.getPlayer().sendMessage(colorize("&cFailed to load your house!"));
        } else {
            Bukkit.getLogger().info("Failed to load house for " + owner.getName() + "!");
        }
    }

    private void createTemplatePlatform() {
        int platformSize = 15;
        int startX = -platformSize / 2;
        int startZ = -platformSize / 2;

        for (int x = startX; x <= -startX; x++) {
            for (int z = startZ; z <= -startZ; z++) {
                houseWorld.getBlockAt(x, 59, z).setType(Material.STONE);
                houseWorld.getBlockAt(x, 60, z).setType((Math.random() > 0.25) ? Material.GRASS_BLOCK : Material.COARSE_DIRT);
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
            String json = GSON.toJson(houseData);
            Files.writeString(file.toPath(), json, StandardCharsets.UTF_8);
            asp.saveWorld(slimeWorld);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void unload() {
        houseWorld.getPlayers().forEach(player -> {
            player.sendMessage(colorize("&e&lHouse is being unloaded!"));
            kickPlayerFromHouse(player);
        });
        housingNPCS.forEach(npc -> npc.getCitizensNPC().destroy());
        killAllEntities();
        Bukkit.unloadWorld(houseWorld, false);
    }

    public void delete() {
        houseWorld.getPlayers().forEach(player -> {
            kickPlayerFromHouse(player);
            player.sendMessage(colorize("&e&lThis house has been deleted!"));
        });
        Bukkit.unloadWorld(houseWorld, false);
        deleteWorld();
    }

    private void killAllEntities() {
        houseWorld.getEntities().forEach(entity -> {
            if (!(entity instanceof Player)) entity.remove();
        });
    }

    private boolean deleteWorld() {
        housingNPCS.forEach(npc -> removeNPC(npc.getNpcID()));
        commands.forEach(command -> main.getCommandFramework().unregisterCommand(command.getCommand(), this));
        commands.clear();
        File file = new File(main.getDataFolder(), "houses/" + houseUUID + ".json");
        if (file.exists()) file.delete();
        try {
            loader.deleteWorld(houseUUID.toString());
            return true;
        } catch (UnknownWorldException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public void broadcast(String message) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().getName().equals(houseUUID.toString()))
                .forEach(player -> player.sendMessage(colorize(message)));
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
        return eventActions.getOrDefault(type, new ArrayList<>());
    }

    public void sendPlayerToHouse(Player player) {
        player.teleport(spawn);
        player.sendMessage(colorize("&aSending you to " + name + "&a..."));
    }

    public List<Command> getCommands() {
        return commands;
    }

    public StatManager getStatManager() {
        return statManager;
    }

    public void kickPlayerFromHouse(Player player) {
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(ownerUUID).getName();
    }

    public void setGuests() {
        guests = Bukkit.getWorld(houseUUID.toString()).getPlayers().size();
    }

    public World getWorld() {
        return houseWorld;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCookies() {
        return cookies;
    }

    public int getSize() {
        return size;
    }

    public HousePrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(HousePrivacy privacy) {
        this.privacy = privacy;
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

    public List<Function> getFunctions() {
        return functions;
    }

    public Function getFunction(String name) {
        return functions.stream().filter(function -> function.getName().equals(name)).findFirst().orElse(null);
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setMaterial(Material material) {
        this.icon = material;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public List<Layout> getLayouts() {
        return layouts;
    }

    public void createNPC(Player player, Location location) {
        HousingNPC npc = new HousingNPC(main, player, location, this);
        housingNPCS.add(npc);
    }

    public void loadNPC(OfflinePlayer player, Location location, NPCData data) {
        HousingNPC npc = new HousingNPC(main, player, location, this, data);
        housingNPCS.add(npc);
    }

    public HousingNPC getNPC(int id) {
        return housingNPCS.stream().filter(npc -> npc.getNpcID() == id).findFirst().orElse(null);
    }

    public void removeNPC(int id) {
        housingNPCS.stream().filter(npc -> npc.getNpcID() == id).findFirst().ifPresent(npc -> {
            NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(id);
            if (citizensNPC != null) {
                citizensNPC.destroy();
                CitizensAPI.getNPCRegistry().deregister(citizensNPC);
                housingNPCS.remove(npc);
            }
        });
    }

    public List<HousingNPC> getNPCs() {
        return new ArrayList<>(housingNPCS);
    }

    public Hologram createHologram(Player player, Location location) {
        Hologram hologram = new Hologram(main, player, this, location);
        holograms.add(hologram);
        return hologram;
    }

    public Hologram getHologramInstance(int id) {
        for (Hologram hologram : holograms) {
            if (hologram.getEntitys().contains(id)) return hologram;
        }
        return null;
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }

    public void removeHologram(Hologram hologram) {
        holograms.remove(hologram);
    }

    public Function createFunction(String name) {
        if (name == null || functions.stream().anyMatch(function -> function.getName().equals(name))) return null;
        Function function = new Function(name);
        functions.add(function);
        return function;
    }

    public Command createCommand(String name) {
        if (name == null || !name.matches("^[a-zA-Z0-9]*$") || main.getCommandFramework().hasCommand(name) || commands.stream().anyMatch(command -> command.getName().equals(name))) return null;
        Command command = new Command(name);
        commands.add(command);
        main.getCommandFramework().registerCommand(houseUUID.toString(), command.getCommand());
        return command;
    }

    public Region createRegion(String name, Location posA, Location posB) {
        if (name == null || regions.stream().anyMatch(region -> region.getName().equals(name))) return null;
        Region region = new Region(posA, posB, name);
        regions.add(region);
        return region;
    }

    public Layout createLayout(String name) {
        if (name == null || layouts.stream().anyMatch(layout -> layout.getName().equals(name))) return null;
        Layout layout = new Layout(name);
        layouts.add(layout);
        return layout;
    }

    public boolean executeEventActions(EventType eventType, Player player, Cancellable event) {
        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            ActionExecutor executor = new ActionExecutor();
            executor.addActions(actions);
            executor.execute(player, this, event);
            return event != null && event.isCancelled();
        }
        return false;
    }

    public Layout getLayout(String layout) {
        return layouts.stream().filter(l -> l.getName().equals(layout)).findFirst().orElse(null);
    }
}