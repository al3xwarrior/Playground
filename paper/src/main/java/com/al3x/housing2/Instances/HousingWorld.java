package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.Actions.TeleportAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Enums.WeatherTypes;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.*;
import com.al3x.housing2.Listeners.TrashCanListener;
import com.al3x.housing2.Main;
import com.al3x.housing2.Mongo.Collection.HousesCollection;
import com.al3x.housing2.Utils.*;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.UnknownWorldException;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

import static com.al3x.housing2.Enums.permissions.Permissions.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class HousingWorld {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new InstantTypeAdapter()).create();

    private transient Main main;
    private transient SlimeLoader loader;
    private transient AdvancedSlimePaperAPI asp;
    private transient World houseWorld;
    private transient SlimeWorld slimeWorld;

    private UUID ownerUUID;
    private UUID houseUUID;
    private String ownerName;
    private String name;
    private String description;
    private int size;
    private int guests;
    private int cookies;
    private List<UUID> cookieGivers;
    private int cookieWeek;
    private long timeCreated;
    private Location spawn;
    private long ingameTime;
    private boolean dayLightCycle;
    private WeatherTypes weather;
    private boolean weatherCycle;
    private List<String> scoreboard;
    private String scoreboardTitle;
    private HousePrivacy privacy;
    private ItemStack iconItem;
    private ConcurrentHashMultiset<HousingNPC> housingNPCS;
    private List<Hologram> holograms;
    private HashMap<EventType, List<Action>> eventActions;
    private List<Function> functions;
    private StatManager statManager;
    private List<Command> commands;
    private List<Region> regions;
    private List<Layout> layouts;
    private List<CustomMenu> customMenus;
    private List<Group> groups;
    private List<Team> teams;
    private HashMap<String, PlayerData> playersData;
    private String defaultGroup = "default";
    private List<Location> trashCans;
    private List<LaunchPad> launchPads;
    private HashMap<LocationData, List<ActionData>> actionButtons;
    private String seed;
    private Random random;
    private Integer version;
    private transient ArrayList<Player> adminModeUsers = new ArrayList<>();
    private boolean joinLeaveMessages;
    private boolean deathMessages;
    private boolean keepInventory;
    private String lockedReason = "";
    private ResourcePackData resourcePack;
    private boolean randomTicks;

    public transient HouseData houseData;

    // Jukebox
    private transient Playlist playlist;
    private transient RadioSongPlayer radioSongPlayer;

    private transient boolean loaded = false;
    private transient boolean unloaded = false;
    private transient List<Consumer<HousingWorld>> onLoad = new ArrayList<>();
    public transient HashMap<UUID, List<BossBar>> bossBars = new HashMap<>();
    private transient HousingScoreboard scoreboardInstance;

    private Player invitedPlayer;

    //A problem I just thought off was that we will need to remove the owner from the house if we want to ever use this on more than one server.
    public HousingWorld(Main main, String houseID) {
        // long start = System.currentTimeMillis();

        main.getServer().getAsyncScheduler().runNow(main, (t) -> {
            long asyncStart = System.currentTimeMillis();
            initialize(main, null);
            loadHouseData(houseID);
            setupHouseData();
            loadCommands();

            main.getLogger().info("Loaded Async Part of house " + name + " in " + (System.currentTimeMillis() - asyncStart) + "ms!");

            main.getServer().getScheduler().runTask(main, () -> {
                try {
                    long syncStart = System.currentTimeMillis();
                    loadWorld();
                    setupDataAfterLoad();
                    loadNPCs();
                    setupWorldBorder();
                    if (groups.isEmpty()) {
                        addDefaultGroups(null);
                    }
                    loaded = true;
                    main.getLogger().info("Loaded Sync Part of house " + name + " in " + (System.currentTimeMillis() - syncStart) + "ms!");
                    onLoad.forEach(consumer -> consumer.accept(this));
                    main.getServer().getAsyncScheduler().runNow(main, (t2) -> {
                        save();
                    });
                } catch (Exception e) {
                    main.getLogger().log(Level.SEVERE, e.getMessage(), e);
                }
            });
        });
    }

    public HousingWorld(Main main, Player owner, HouseSize size) {
        initialize(main, owner.getName() + "'s House");
        setupNewHouse(owner, size);
        createTemplatePlatform();
        setupWorldBorder();
        setupDefaultScoreboard();
        addDefaultGroups(owner);
        save();
        loaded = true;
        onLoad.forEach(consumer -> consumer.accept(this));
    }

    private void initialize(Main main, String name) {
        this.main = main;
        this.name = name;
        this.housingNPCS = ConcurrentHashMultiset.create();
        this.eventActions = new HashMap<>();
        for (EventType type : EventType.values()) {
            eventActions.put(type, new ArrayList<>());
        }
        this.functions = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.regions = new ArrayList<>();
        this.cookieGivers = new ArrayList<>();
        this.layouts = new ArrayList<>();
        this.holograms = new ArrayList<>();
        this.trashCans = new ArrayList<>();
        this.launchPads = new ArrayList<>();
        this.actionButtons = new HashMap<>();
        this.customMenus = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.playersData = new HashMap<>();
        this.statManager = new StatManager(this);
        this.scoreboardTitle = "<gradient:gold:green><b>ᴘʟᴀʏɢʀᴏᴜɴᴅ";

        try {
            this.loader = main.getLoader();
            this.asp = AdvancedSlimePaperAPI.instance();
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }

        this.playlist = new Playlist(JukeBoxManager.getRandomSong()); // needs at least 1 song to work
        this.radioSongPlayer = new RadioSongPlayer(playlist);
        radioSongPlayer.setRepeatMode(RepeatMode.ALL);
        radioSongPlayer.setRandom(true);

        this.version = Updater.LATEST_VERSION;
    }

    private void loadHouseData(String name) {
        File file = new File(main.getDataFolder(), "houses/" + name + ".json");
        if (!file.exists()) {
            return;
        }
        try {
            JsonObject json = Updater.update(JsonParser.parseString(Files.readString(file.toPath(), StandardCharsets.UTF_8)).getAsJsonObject());
            houseData = GSON.fromJson(json, HouseData.class);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void setupHouseData() {
        this.houseUUID = UUID.fromString(houseData.getHouseID());
        this.ownerUUID = UUID.fromString(houseData.getOwnerID());
        this.name = houseData.getHouseName();
        this.cookies = (int) houseData.getCookies();
        this.cookieGivers = new ArrayList<>();
        this.description = houseData.getDescription();
        this.timeCreated = houseData.getTimeCreated();
        this.privacy = houseData.getPrivacy() != null ? HousePrivacy.valueOf(houseData.getPrivacy()) : HousePrivacy.PRIVATE;
        this.statManager.setGlobalStats(StatData.toList(houseData.getGlobalStats()));
        this.commands = houseData.getCommands() != null ? CommandData.toList(houseData.getCommands()) : new ArrayList<>();
        this.layouts = houseData.getLayouts() != null ? LayoutData.toList(houseData.getLayouts()) : new ArrayList<>();
        this.customMenus = houseData.getCustomMenus() != null ? CustomMenuData.toList(houseData.getCustomMenus()) : new ArrayList<>();
        this.groups = houseData.getGroups() != null ? GroupData.toList(houseData.getGroups()) : new ArrayList<>();
        this.teams = houseData.getTeams() != null ? TeamData.toList(houseData.getTeams()) : new ArrayList<>();
        this.playersData = houseData.getPlayerData() != null ? houseData.getPlayerData() : new HashMap<>();

        // Remove null entries because I'm not going to find the cause of it rn
        List<String> keysToRemove = new ArrayList<>();
        for (String id : playersData.keySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(id));
            if (!player.hasPlayedBefore()) {
                keysToRemove.add(id);
            }
        }
        for (String id : keysToRemove) {
            playersData.remove(id);
        }

        for (PlayerData playerData : playersData.values()) {
            if (playerData.getStats() != null) playerData.setCacheStats(StatData.toList(playerData.getStats()));
        }
        this.defaultGroup = houseData.getDefaultGroup() != null ? houseData.getDefaultGroup() : "default";
        this.scoreboard = houseData.getScoreboard();
        this.scoreboardTitle = houseData.getScoreboardTitle() != null ? houseData.getScoreboardTitle() : "<gradient:gold:green><b>ᴘʟᴀʏɢʀᴏᴜɴᴅ";
        loadEventActions();
        this.functions = houseData.getFunctions() != null ? FunctionData.toList(houseData.getFunctions()) : new ArrayList<>();
        this.seed = houseData.getSeed();
        this.random = new Random(seed.hashCode());
        this.size = houseData.getSize();
        if (this.size == 75) this.size = 150;
        if (this.size == 150) this.size = 160;
        this.version = houseData.getVersion();
        this.ingameTime = houseData.getIngameTime() != null ? houseData.getIngameTime() : 6000L;
        this.dayLightCycle = houseData.getDayLightCycle() != null ? houseData.getDayLightCycle() : false;
        this.weather = houseData.getWeather() != null ? houseData.getWeather() : WeatherTypes.SUNNY;
        this.weatherCycle = houseData.getWeatherCycle() != null ? houseData.getWeatherCycle() : false;
        this.joinLeaveMessages = houseData.getJoinLeaveMessages() != null ? houseData.getJoinLeaveMessages() : true;
        this.deathMessages = houseData.getDeathMessages() != null ? houseData.getDeathMessages() : true;
        this.keepInventory = houseData.getKeepInventory() != null ? houseData.getKeepInventory() : false;
        this.lockedReason = houseData.getLockedMessage() != null ? houseData.getLockedMessage() : "";

        // House loaded after a new week was issued
        if (cookieWeek < main.getCookieManager().getWeek()) {
            cookieWeek = main.getCookieManager().getWeek();
            cookies = 0;
        }

        this.radioSongPlayer.setPlaying(houseData.getJukeboxPlaying() != null ? houseData.getJukeboxPlaying() : true);
        this.resourcePack = houseData.getResourcePack();
        this.randomTicks = Boolean.TRUE.equals(houseData.getRandomTicks());
    }

    private void setupDataAfterLoad() {
        this.regions = houseData.getRegions() != null ? RegionData.toList(houseData.getRegions()) : new ArrayList<>();
        this.holograms = houseData.getHolograms() != null ? HologramData.toList(houseData.getHolograms(), this) : new ArrayList<>();
        this.spawn = houseData.getSpawnLocation() != null ? houseData.getSpawnLocation().toLocation() : new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);
        this.trashCans = houseData.getTrashCans() != null ? new ArrayList<>(houseData.getTrashCans().stream().map(LocationData::toLocation).toList()) : new ArrayList<>();
        this.launchPads = houseData.getLaunchPads() != null ? houseData.getLaunchPads() : new ArrayList<>(); //Used a new method for this :)
        for (String key : houseData.getActionButtons() != null ? houseData.getActionButtons().keySet() : new ArrayList<String>()) {
            actionButtons.put(LocationData.fromString(key), houseData.getActionButtons().get(key));
        }
        scoreboardInstance = new HousingScoreboard(this);
        String icon = houseData.getIcon() != null ? houseData.getIcon() : "OAK_DOOR";
        if (Material.getMaterial(icon) != null) {
            this.iconItem = new ItemStack(Material.getMaterial(icon));
        } else {
            try {
                this.iconItem = Serialization.itemStackFromBase64(icon);
            } catch (IOException e) {
                this.iconItem = new ItemStack(Material.OAK_DOOR);
            }
        }

        killAllEntities();
    }

    private void loadEventActions() {
        for (EventType type : EventType.values()) {
            eventActions.put(type, new ArrayList<>());
            List<ActionData> actions = houseData.getEventActions().get(type.name());
            if (actions != null) {
                for (ActionData action : actions) {
                    eventActions.get(type).add(ActionEnum.getActionByName(action.getAction()).getActionInstance(action.getData(), action.getComment()));
                }
            }
        }
    }

    private void loadWorld() {
        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            return;
        }
        slimeWorld = world;
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        houseWorld.setTime(this.ingameTime);
        houseWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, this.dayLightCycle);
        switch (this.weather) {
            case WeatherTypes.SUNNY -> {
                houseWorld.setStorm(false);
                houseWorld.setThundering(false);
            }
            case WeatherTypes.RAINING -> {
                houseWorld.setStorm(true);
                houseWorld.setThundering(false);
            }
            case WeatherTypes.STORMING -> {
                houseWorld.setStorm(true);
                houseWorld.setThundering(true);
            }
            case WeatherTypes.THUNDER -> {
                houseWorld.setStorm(false);
                houseWorld.setThundering(true);
            }
        }
        houseWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        houseWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false); // No natural mob spawning
        houseWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, this.weatherCycle);
        houseWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, this.getRandomTicks() ? 0 : 3);
        this.spawn = spawn == null ? new Location(Bukkit.getWorld(this.houseUUID.toString()), 0.5, 61, 0.5) : spawn;
        TrashCanListener.initTrashCans(trashCans);
    }

    private void loadNPCs() {
        for (NPCData npc : houseData.getHouseNPCs()) {
            Location location = npc.getNpcLocation().toLocation();
            loadNPC(location, npc);
        }
    }

    private void loadCommands() {
        onLoad.add((w) -> {
            Bukkit.reloadData();
        });

    }

    private void setupNewHouse(Player owner, HouseSize size) {
        this.houseUUID = UUID.randomUUID();
        this.ownerUUID = owner.getUniqueId();
        ensureUniqueHouseUUID();
        this.description = "&7This is a default description!";
        this.timeCreated = System.currentTimeMillis();
        this.privacy = HousePrivacy.PRIVATE;
        this.iconItem = new ItemStack(Material.OAK_DOOR);
        this.seed = UUID.randomUUID().toString();
        this.random = new Random(seed.hashCode());
        this.size = determineHouseSize(size);
        this.ingameTime = 6000L;
        this.dayLightCycle = false;
        this.weather = WeatherTypes.SUNNY;
        this.weatherCycle = false;
        this.joinLeaveMessages = true;
        this.deathMessages = true;
        this.keepInventory = false;
        SlimeWorld world = createOrReadWorld();
        if (world == null) {
            owner.sendMessage(colorize("&cFailed to create your house!"));
            return;
        }
        slimeWorld = world;
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        houseWorld.setTime(ingameTime);
        houseWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, dayLightCycle);
        houseWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, weatherCycle);
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0.5, 61, 0.5);

        // Default Commands
        createCommand("stuck").setActions(List.of(new TeleportAction(true)));
        createCommand("spawn").setActions(List.of(new TeleportAction(true)));
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
            case XLARGE -> 150;
            case MASSIVE -> 255;
            default -> 30;
        };
    }

    private void addDefaultGroups(OfflinePlayer owner) {
        Group defaultGroup = new Group("default");
        defaultGroup.setPrefix("&7");
        defaultGroup.setDisplayName("&7Default");
        defaultGroup.setColor("§7");
        groups.add(defaultGroup);

        Group ownerGroup = new Group("owner");
        ownerGroup.setPrefix("&e[Owner] ");
        ownerGroup.setDisplayName("&eOwner");
        ownerGroup.setColor("§e");
        ownerGroup.setPriority(2147483647);
        groups.add(ownerGroup);

        if (owner instanceof Player) {
            PlayerData data = loadOrCreatePlayerData((Player) owner);
            data.setGroup(ownerGroup.getName());
        }
    }

    private void setupWorldBorder() {
        this.houseWorld.getWorldBorder().setCenter(new Location(houseWorld, 0, 61, 0));
        this.houseWorld.getWorldBorder().setSize(this.size % 2 == 0 ? this.size : this.size + 1);
        this.houseWorld.getWorldBorder().setWarningDistance(0);
    }

    private void setupDefaultScoreboard() {
        this.scoreboard = new ArrayList<>();
        this.scoreboard.add("%house.name%:");
        this.scoreboard.add("&7- &eCookies: &6%house.cookies%");
        this.scoreboard.add("&7- &aPlayers: &2%house.guests%");
        this.scoreboard.add("&7");
        this.scoreboard.add("&7Edit the scoreboard in the");
        this.scoreboard.add("&7systems menu!");


        scoreboardInstance = new HousingScoreboard(this);
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
        properties.setValue(SlimeProperties.DRAGON_BATTLE, false);
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
        for (Player player : houseWorld.getPlayers()) {
            //They have a window open so we shouldnt save their inventory
            if (MenuManager.getWindowOpen(player) != null && MenuManager.getWindowOpen(player) == MenuManager.getPlayerMenu(player))
                continue;
            PlayerData data = loadOrCreatePlayerData(player);
            data.setInventory(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getInventory().getContents()).toList())));
            data.setEnderchest(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getEnderChest().getContents()).toList())));
        }

        for (PlayerData playerData : playersData.values()) {
            if (playerData.getStats() != null) playerData.setStats(StatData.fromList(playerData.getCacheStats()));
        }

        try {
            houseData = HouseData.fromHousingWorld(this);
            File file = new File(main.getDataFolder(), "houses/" + houseUUID + ".json");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            String json = GSON.toJson(houseData);
            Files.writeString(file.toPath(), json, StandardCharsets.UTF_8);
            main.getHousesManager().updateCache(houseData);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }

        try {
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
        housingNPCS.forEach(npc -> {
            npc.getHologram().remove();
            npc.getCitizensNPC().despawn();
            npc.getCitizensNPC().destroy();
            CitizensAPI.getNPCRegistry().deregister(npc.getCitizensNPC());
        });

        holograms.forEach(Hologram::remove);
        killAllEntities();
        trashCans.forEach(location -> { //I dont think this is needed cause of the killAllEntities() method
            for (ArmorStand armorStand : getWorld().getEntitiesByClass(ArmorStand.class)) {
                if (armorStand.getLocation().add(0, 1, 0).getBlock().equals(location.getBlock())) {
                    armorStand.remove();
                }
            }
        });
        Bukkit.unloadWorld(houseWorld, false);
    }

    public void delete() {
        runOnLoadOrNow((h) -> {
            if (radioSongPlayer != null) radioSongPlayer.setPlaying(false);
            if (houseWorld != null) {
                houseWorld.getPlayers().forEach(player -> {
                    kickPlayerFromHouse(player);
                    player.sendMessage(colorize("&e&lThis house has been deleted!"));
                });
                Bukkit.unloadWorld(houseWorld, false);
            }
            deleteWorld();
        });
    }

    private void killAllEntities() {
        houseWorld.getEntities().forEach(entity -> {
            if (!(entity instanceof Player)) entity.remove();
        });
    }

    private boolean deleteWorld() {
        housingNPCS.forEach(npc -> removeNPC(npc.getNpcID(), true));
        commands.clear();
        File file = new File(main.getDataFolder(), "houses/" + houseUUID + ".json");
        if (file.exists()) file.delete();
        try {
            loader.deleteWorld(houseUUID.toString());
        } catch (UnknownWorldException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    public void broadcast(String message) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().getName().equals(houseUUID.toString()))
                .forEach(player -> player.sendMessage(colorize(message)));
    }

    public void addSong(Song song) {
        if (!radioSongPlayer.isPlaying()) {
            clearAllSongsBut(song);
            radioSongPlayer.setPlaying(true);
            return;
        }
        if (!playlist.contains(song)) playlist.add(song);
    }

    public void removeSong(Song song) {
        if (!playlist.contains(song)) return;
        if (playlist.getCount() == 1) {
            stopMusic();
            return;
        }
        if (radioSongPlayer.getSong() == song) radioSongPlayer.playNextSong();
        playlist.remove(song);
    }

    public void clearAllSongsBut(Song song) {
        playlist.add(song);
        playlist.getSongList().forEach(bald -> {
            if (bald != song) playlist.remove(bald);
        });
    }

    public void startMusic() {
        radioSongPlayer.setPlaying(true);
    }

    public void stopMusic() {
        radioSongPlayer.setPlaying(false);
    }

    public void skipSong() {
        radioSongPlayer.playNextSong();
    }

    public Song getCurrentSong() {
        return radioSongPlayer.getSong();
    }

    public boolean songInPlaylist(Song song) {
        return playlist.contains(song);
    }

    public Set<UUID> getRadioPlayers() {
        return radioSongPlayer.getPlayerUUIDs();
    }

    public int getSongCount() {
        return playlist.getCount();
    }

    public List<Song> getSongs() {
        return playlist.getSongList();
    }

    // Runs after the player joined
    public void playerJoins(Player player) {
        radioSongPlayer.addPlayer(player);

        if (privacy == HousePrivacy.LOCKED) {
            player.sendMessage(colorize("&6&m                                 "));
            player.sendMessage(colorize("&cThis house has been &4&lLOCKED&c!"));
            player.sendMessage(colorize("&r"));
            player.sendMessage(colorize("&cReason: &e" + lockedReason));
            player.sendMessage(colorize("&r"));
            player.sendMessage(colorize("&7&oYou may make the house public again after changes have been made."));
            player.sendMessage(colorize("&6&m                                 "));
        }
    }

    public void playerLeaves(Player player) {
        radioSongPlayer.removePlayer(player);
        for (BossBar bossBar : this.bossBars.getOrDefault(player.getUniqueId(), new ArrayList<>())) {
            bossBar.removeViewer(player);
        }
    }

    public boolean isJukeboxPlaying() {
        return radioSongPlayer.isPlaying();
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

    public void runOnLoadOrNow(Consumer<HousingWorld> consumer) {
        if (loaded) {
            consumer.accept(this);
        } else {
            onLoad.add(consumer);
        }
    }

    public void sendPlayerToHouse(Player player) {
        try {
            PlayerData playerData = houseData.getPlayerData().get(player.getUniqueId().toString());
            if (playerData.isBanned() && playerData.getBanExpiration().isAfter(Instant.now())) {
                player.sendMessage(colorize("&cYou are banned from " + name + "!"));
                return;
            } else if (playerData.isBanned()) {
                playerData.setBanned(false);
            }
        } catch (NullPointerException ignored) {
        }

        if (privacy == HousePrivacy.LOCKED && !ownerUUID.equals(player.getUniqueId())) {
            player.sendMessage(colorize("&cThis house has been locked by a Staff Member! Let the owner know they need to make some changes!"));
            return;
        }

        if (loaded) {
            player.teleport(spawn);
            player.sendMessage(StringUtilsKt.housingStringFormatter("&aSending you to " + name + "&a..."));
            main.getResourcePackManager().addResourcePack(player, this);
        } else {
            onLoad.add(house -> {
                player.teleport(house.getSpawn());
                player.sendMessage(StringUtilsKt.housingStringFormatter("&aSending you to " + name + "&a..."));
                main.getResourcePackManager().addResourcePack(player, this);
            });
        }
    }

    public void sendPlayerToHouse(Player player, boolean force) {
        adminModeUsers.add(player);
        if (loaded) {
            player.teleport(spawn);
            player.sendMessage(StringUtilsKt.housingStringFormatter("&aSending you to " + name + "&a..."));
            main.getResourcePackManager().addResourcePack(player, this);
        } else {
            onLoad.add(house -> {
                player.teleport(house.getSpawn());
                player.sendMessage(StringUtilsKt.housingStringFormatter("&aSending you to " + name + "&a..."));
                main.getResourcePackManager().addResourcePack(player, this);
            });
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public StatManager getStatManager() {
        return statManager;
    }

    public void kickPlayerFromHouse(Player player) {
        player.sendMessage(colorize("&cYou have been kicked from this house!"));
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(ownerUUID).getName();
    }

    public void setGuests() {
        guests = Bukkit.getWorld(houseUUID.toString()).getPlayers().size();
    }

    public void clearCookies() {
        cookies = 0;
        cookieGivers.clear();
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

    public Integer getVersion() {
        return version;
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

    public boolean getDaylightCycle() {
        return dayLightCycle;
    }

    public void setDayLightCycle(boolean value) {
        this.dayLightCycle = value;
    }

    public long getIngameTime() {
        return ingameTime;
    }

    public void setIngameTime(long value) {
        this.ingameTime = value;
    }

    public WeatherTypes getWeather() {
        return weather;
    }

    public void setWeather(WeatherTypes value) {
        this.weather = value;
    }

    public boolean getWeatherCycle() {
        return weatherCycle;
    }

    public void setWeatherCycle(boolean value) {
        this.weatherCycle = value;
    }

    public boolean getJoinLeaveMessages() {
        return joinLeaveMessages;
    }

    public void setJoinLeaveMessages(boolean joinLeaveMessages) {
        this.joinLeaveMessages = joinLeaveMessages;
    }

    public void giveCookies(Player player, int amount) {
        cookieGivers.add(player.getUniqueId());
        cookies += amount;

        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner != null)
            owner.sendMessage(colorize("&eYou were given &a" + amount + " &ecookie" + ((amount > 1) ? "s!" : "!") + " by " + player.getName()));
        player.sendMessage(colorize("&eYou gave " + Bukkit.getOfflinePlayer(ownerUUID).getName() + " &ea pack of &a" + amount + " &ecookie" + ((cookies > 1) ? "s!" : "!")));
    }

    /**
     * @param player The player to check
     * @return True if the player has already given cookies to this house this week
     */
    public boolean playerGivenCookies(Player player) {
        return cookieGivers.contains(player.getUniqueId());
    }

    public List<UUID> getCookieGivers() {
        return cookieGivers;
    }

    public void setCookies(int cookies) {
        this.cookies = cookies;
    }

    public int getCookieWeek() {
        return cookieWeek;
    }

    public void setCookieWeek(int cookieWeek) {
        this.cookieWeek = cookieWeek;
    }

    public void addLaunchPad(Location location) {
        launchPads.add(new LaunchPad(location));
    }

    public void setLaunchPads(List<LaunchPad> launchPads) {
        this.launchPads = launchPads;
    }

    public void removeLaunchPad(Location location) {
        launchPads.removeIf(launchPad -> launchPad.getLocation().equals(location));
    }

    public boolean launchPadAtLocation(Location location) {
        return launchPads.stream().anyMatch(launchPad -> launchPad.getLocation().equals(location));
    }

    public LaunchPad getLaunchPadAtLocation(Location location) {
        return launchPads.stream().filter(launchPad -> launchPad.getLocation().equals(location)).findFirst().orElse(null);
    }

    public List<LaunchPad> getLaunchPads() {
        return launchPads;
    }

    public void addTrashCan(Location location) {
        getWorld().setBlockData(location, Bukkit.createBlockData(Material.BARRIER));
        trashCans.add(location);
    }

    public void setTrashCans(List<Location> trashCans) {
        this.trashCans = trashCans;
    }

    public void removeTrashCan(Location location) {
        trashCans.remove(location);
    }

    public boolean trashCanAtLocation(Location location) {
        return trashCans.stream().anyMatch(trashCan -> trashCan.equals(location));
    }

    public List<Location> getTrashCans() {
        return trashCans;
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

    public ItemStack getIcon() {
        return iconItem;
    }

    public void setIcon(Material icon) {
        this.iconItem = new ItemStack(icon);
    }

    public void setIcon(ItemStack icon) {
        this.iconItem = icon;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(ownerUUID);
    }

    public List<Region> getRegions() {
        return regions;
    }

    public List<Layout> getLayouts() {
        return layouts;
    }

    public List<CustomMenu> getCustomMenus() {
        return customMenus;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public HousingScoreboard getScoreboardInstance() {
        return scoreboardInstance;
    }

    public void createNPC(Player player, Location location) {
        if (housingNPCS.size() > 50) {
            player.sendMessage(colorize("&cYou have reached the maximum amount of NPCs!"));
            return;
        }
        HousingNPC npc = new HousingNPC(main, player, location, this);
        housingNPCS.add(npc);
    }

    public void loadNPC(Location location, NPCData data) {
        HousingNPC npc = new HousingNPC(main, location, this, data);
        housingNPCS.add(npc);
    }

    public HousingNPC getNPC(int id) {
        return housingNPCS.stream().filter(npc -> npc.getInternalID() == id).findFirst().orElse(null);
    }

    public HousingNPC getNPCByCitizensID(int id) {
        return housingNPCS.stream().filter(npc -> npc.getNpcID() == id).findFirst().orElse(null);
    }

    public void removeNPC(int id, boolean delete) {
        housingNPCS.stream().filter(npc -> npc.getNpcID() == id).findFirst().ifPresent(npc -> {
            NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(id);
            if (citizensNPC != null) {
                citizensNPC.destroy();
                CitizensAPI.getNPCRegistry().deregister(citizensNPC);
                if (delete) {
                    npc.deleted = true;
                    housingNPCS.remove(npc);
                }
            }
            npc.getHologram().remove();
        });
    }

    public List<HousingNPC> getNPCs() {
        return new ArrayList<>(housingNPCS);
    }

    public Hologram createHologram(Player player, Location location) {
        if (holograms.size() > 50) {
            player.sendMessage(colorize("&cYou have reached the maximum amount of holograms!"));
            return null;
        }
        Hologram hologram = new Hologram(main, player, this, location);
        holograms.add(hologram);
        return hologram;
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }

    public HashMap<String, List<ActionData>> getActionButtons() {
        HashMap<String, List<ActionData>> data = new HashMap<>();
        actionButtons.forEach((key, value) -> data.put(key.getWorld() + ", " + key.getX() + ", " + key.getY() + ", " + key.getZ(), value));
        return data;
    }

    public void addActionButton(Location location) {
        actionButtons.put(LocationData.fromLocation(location), new ArrayList<>());
    }

    public List<Action> getActionButton(Location location) {
        for (LocationData loc : actionButtons.keySet()) {
            if (loc.toLocation().distance(location) < 1) {
                return ActionData.toList(actionButtons.get(loc));
            }
        }
        return new ArrayList<>();
    }

    public void setActionButton(Location location, List<Action> actions) {
        for (LocationData loc : actionButtons.keySet()) {
            if (loc.toLocation().distance(location) < 1) {
                actionButtons.put(loc, ActionData.fromList(actions));
                return;
            }
        }
    }

    public boolean removeActionButton(Location location) {
        for (LocationData loc : actionButtons.keySet()) {
            if (loc.toLocation().distance(location) < 1) {
                actionButtons.remove(loc);
                return true;
            }
        }
        return false;
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
        if (name == null || !name.matches("^[a-zA-Z0-9]*$") || commands.stream().anyMatch(command -> command.getName().equals(name)))
            return null;
        Command command = new Command(name);
        commands.add(command);
        Bukkit.getScheduler().runTaskAsynchronously(main, Bukkit::reloadData);
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

    public Group createGroup(String name) {
        if (name == null || groups.stream().anyMatch(group -> group.getName().equalsIgnoreCase(name))) return null;
        Group group = new Group(name);
        groups.add(group);
        return group;
    }

    public Team createTeam(String name) {
        if (name == null || groups.stream().anyMatch(team -> team.getName().equalsIgnoreCase(name))) return null;
        Team team = new Team(name);
        teams.add(team);
        return team;
    }

    public PlayerData loadOrCreatePlayerData(Player player) {
        PlayerData data = playersData.get(player.getUniqueId().toString());
        if (data == null) {
            data = new PlayerData(null, null, null, null, null, false, Instant.now(), false, Instant.now(), new ArrayList<>(), player.getName());
            data.setGroup(defaultGroup);
            playersData.put(player.getUniqueId().toString(), data);
        }
        return data;
    }

    public HashMap<String, PlayerData> getPlayersData() {
        return playersData;
    }

    public void setDefaultGroup(String defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public boolean hasPermission(Player player, Permissions permission) {
        if (player.getUniqueId().equals(ownerUUID) || isAdminMode(player)) return true;
        PlayerData data = playersData.get(player.getUniqueId().toString());
        if (data == null) return false;
        Object permissionValue = data.getGroupInstance(this).getPermissions().get(permission);
        if (permissionValue instanceof Boolean) return permissionValue.equals(true);
        return false; //Other permission types will be checked by itself
    }

    public Object getPermission(Player player, Permissions permission) {
        if (player.getUniqueId().equals(ownerUUID) || isAdminMode(player)) return true;
        PlayerData data = playersData.get(player.getUniqueId().toString());
        if (data == null) return false;
        return data.getGroupInstance(this).getPermissions().get(permission);
    }

    private boolean hasAnyPermission(Player player, Permissions[] permission) {
        if (player.getUniqueId().equals(ownerUUID) || isAdminMode(player)) return true;
        PlayerData data = playersData.get(player.getUniqueId().toString());
        if (data == null) return false;
        for (Permissions permissions : permission) {
            Object permissionValue = data.getGroupInstance(this).getPermissions().get(permissions);
            if (permissionValue instanceof Boolean && permissionValue.equals(true)) return true;
        }
        return false; //Other permission types will be checked by itself
    }

    public boolean hasSystem(Player player) {
        Permissions[] permission = {
                EDIT_ACTIONS,
                EDIT_REGIONS,
                EDIT_SCOREBOARD,
                EDIT_EVENTS,
                EDIT_COMMANDS,
                EDIT_FUNCTIONS,
                EDIT_INVENTORY_LAYOUTS,
                EDIT_TEAMS,
                EDIT_CUSTOM_MENUS
        };
        return hasAnyPermission(player, permission);
    }

    public boolean hasPlayerListing(Player player) {
        Permissions[] permission = {
                KICK,
                BAN,
                MUTE,
                GAMEMODE,
                CHANGE_PLAYER_GROUP,
                EDIT_PERMISSIONS_AND_GROUP,
                CHANGE_PLAYER_TEAM,
                EDIT_TEAMS,
        };
        return hasAnyPermission(player, permission);
    }

    public boolean executeEventActions(EventType eventType, Player player, CancellableEvent event) {
        if (isAdminMode(player)) return false;

        if (eventType == EventType.PLAYER_JOIN) playerJoins(player);
        if (eventType == EventType.PLAYER_QUIT) playerLeaves(player);

        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            ActionExecutor executor = new ActionExecutor("event");
            executor.addActions(actions);
            executor.execute(player, this, event);
            return event != null && event.isCancelled();
        }
        return false;
    }

    public Layout getLayout(String layout) {
        return layouts.stream().filter(l -> l.getName().equals(layout)).findFirst().orElse(null);
    }

    public void addCustomMenu(CustomMenu customMenu) {
        customMenus.add(customMenu);
    }

    public void setSpawn(@NotNull Location location) {
        this.spawn = location;
    }

    public Group getGroup(String group) {
        return groups.stream().filter(g -> g.getName().equals(group)).findFirst().orElse(null);
    }

    public Team getTeam(String team) {
        return teams.stream().filter(t -> t.getName().equals(team)).findFirst().orElse(null);
    }

    public boolean isAdminMode(Player player) {
        return adminModeUsers.contains(player);
    }

    public void removeFromAdminMode(Player player) {
        adminModeUsers.remove(player);
    }

    public boolean getDeathMessages() {
        return deathMessages;
    }

    public void setDeathMessages(boolean deathMessages) {
        this.deathMessages = deathMessages;
    }

    public boolean getKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public void setScoreboardTitle(String title) {
        this.scoreboardTitle = title;
    }

    public String getLockedReason() {
        return lockedReason;
    }

    public void setLockedReason(String lockedReason) {
        this.lockedReason = lockedReason;
    }

    public ResourcePackData getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(ResourcePackData resourcePack) {
        this.resourcePack = resourcePack;
    }

    public boolean getRandomTicks() {
        return randomTicks;
    }

    public void setRandomTicks(boolean randomTicks) {
        houseWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, randomTicks ? 0 : 3);
        this.randomTicks = randomTicks;
    }

    public Main getPlugin() {
        return this.main;
    }
    public void setSize(HouseSize size) {
        this.size = determineHouseSize(size);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Player getInvitedPlayer() {
        return invitedPlayer;
    }
    public void setInvitedPlayer(Player player) {
        this.invitedPlayer = player;
    }
}
