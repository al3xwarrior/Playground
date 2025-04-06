package com.al3x.housing2;

import com.al3x.housing2.Axiom.PlaygroundIntegration;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.HouseEvents.Permissions.BlockInteractions;
import com.al3x.housing2.Listeners.HouseEvents.Permissions.OpenSomething;
import com.al3x.housing2.Listeners.ProtocolLib.EntityInteraction;
import com.al3x.housing2.Mongo.DatabaseManager;
import com.al3x.housing2.Network.NetworkManager;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Placeholders.papi.CookiesPlaceholder;
import com.al3x.housing2.Utils.HousingCommandFramework;
import com.al3x.housing2.Utils.SkinCache;
import com.al3x.housing2.Utils.VoiceChat;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.loaders.file.FileLoader;
import com.maximde.hologramlib.HologramLib;
import com.maximde.hologramlib.hologram.HologramManager;
import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import io.jooby.ExecutionMode;
import io.jooby.Jooby;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {
    private static Main INSTANCE;

    @Getter
    private static DatabaseManager databaseManager;

    @Getter
    private SlimeLoader loader;
//    private MongoLoader mongoLoader;
    @Getter
    private HousesManager housesManager;
    private CommandManager commandManager;
    @Getter
    private HousingCommandFramework commandFramework;
    @Getter
    private ProtoolsManager protoolsManager;
    @Getter
    private ProtocolManager protocolManager;
    @Getter
    private CookieManager cookieManager;
    @Getter
    private ClipboardManager clipboardManager;
    @Getter
    private LobbyDisplays lobbyDisplays;
    @Getter
    private HologramManager hologramManager;
    @Getter
    private PlayerSpeedManager playerSpeedManager;
    @Getter
    private NetworkManager networkManager;
    @Getter
    private HeadDatabaseAPI headDatabaseAPI;
    private VoiceChat voiceChat;
    @Getter
    private PlaygroundWeb playgroundWeb;
    @Getter
    private ResourcePackManager resourcePackManager;
    @Getter
    private PlaygroundBot playgroundBot;
    @Getter
    private SeatManager seatManager;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onLoad() {
        HologramLib.onLoad(this);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        // The location of the worlds folder is relative to the server's root directory

        saveDefaultConfig();

        String mongoURI = getConfig().getString("mongo_uri", "mongodb://localhost:27017/");
        String mongoDatabase = getConfig().getString("mongo_database", "playground");
        databaseManager = new DatabaseManager(mongoURI, mongoDatabase);

//        mongoLoader = new MongoLoader(databaseManager.getMongoClient(), mongoDatabase, "slime_worlds");
        loader = new FileLoader(new File("./slime_worlds"));

//        if (getConfig().getString("loader", "file").equalsIgnoreCase("mongo")) {
//            loader = mongoLoader;
//        }

        if (getConfig().contains("bot_key") && !Objects.equals(getConfig().getString("bot_key"), "") && getConfig().contains("guild_id") && !Objects.equals(getConfig().getString("guild_id"), "")) {
            try {
                playgroundBot = new PlaygroundBot(getConfig().getString("bot_key"), getConfig().getString("guild_id"));
                getLogger().info("Discord bot properly loaded!");
            } catch (InterruptedException e) {
                getLogger().warning("Discord Bot didn't initialize properly.");
            }
        } else {
            getLogger().warning("Discord bot key and/or guild id not found in config.yml. Discord bot will not be loaded.");
        }

        this.housesManager = new HousesManager(this);
        this.protoolsManager = new ProtoolsManager(this, housesManager);
        this.commandFramework = new HousingCommandFramework(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.cookieManager = new CookieManager(this, getDataFolder());
        this.clipboardManager = new ClipboardManager(this, getDataFolder());
        this.lobbyDisplays = new LobbyDisplays(housesManager);
        this.playerSpeedManager = new PlayerSpeedManager();
        this.networkManager = new NetworkManager(this);
        this.resourcePackManager = new ResourcePackManager(this);
        this.commandManager = new CommandManager();
        this.seatManager = new SeatManager(this);

        this.playgroundWeb = (PlaygroundWeb) Jooby.createApp(new String[0], ExecutionMode.EVENT_LOOP, () -> new PlaygroundWeb(this));
        this.playgroundWeb.start();

        HologramLib.getManager().ifPresentOrElse(
                manager -> hologramManager = manager,
                () -> getLogger().severe("Failed to initialize HologramLib manager.")
        );

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commandManager.registerCommands(commands);
        });

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new HousingMenuClickEvent(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveHouse(housesManager, resourcePackManager), this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HousingItems(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new NpcItems(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtoolsListener(this.protoolsManager), this);
        Bukkit.getPluginManager().registerEvents(new HologramInteractListener(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new SignClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SeatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TrashCanListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LaunchPadListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ActionButtonListener(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityLimitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new EatingListener(this), this);

//        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "housing:export");

        // House Events
        Bukkit.getPluginManager().registerEvents(new NPCEvents(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new LeaveHouse(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinHouse(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new DamageEvent(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKill(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPickupItem(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ToggleSneak(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new FishCaught(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new BreakBlock(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ChangeHeldItem(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ToggleFly(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new AttackEvent(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlaceBlock(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEnterPortal(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JumpEvent(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new OpenSomething(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new BlockInteractions(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new FishBucket(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new PotionSplash(housesManager), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        EntityInteraction.registerInteraction(housesManager);

        if (Bukkit.getPluginManager().isPluginEnabled("AxiomPaper")) {
            PlaygroundIntegration.init();
        }

        Runnables.startRunnables(this);

        Placeholder.registerPlaceholders();

        Bukkit.getPluginManager().registerEvents(new SkinCache(), this);
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, SkinCache::save, 0, 360000); // save skin cache every 5 minutes
        // ^ this never triggers for some reason but its no biggie. only really needed in case the server crashes

        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            voiceChat = new VoiceChat();
            service.registerPlugin(voiceChat);
            voiceChat.startup(this);
        }

        // PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CookiesPlaceholder(this).register();
        }

        if (Main.getInstance().getPlaygroundBot() != null) playgroundBot.updateHousings();
        getServer().getLogger().info("[Housing2] Enabled just fine!");
    }

    @EventHandler
    public void onHeadDatabaseLoad(DatabaseLoadEvent e) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    //    public MongoLoader getMongoLoader() {
//        return mongoLoader;
//    }

    @Override
    public void onDisable() {
        INSTANCE.getLogger().info("[Housing2] Saving and unloading houses...");
        Runnables.stopRunnables();
        for (HousingWorld house : housesManager.getConcurrentLoadedHouses().values()) {
            housesManager.saveHouseAndUnload(house);
        }
        SkinCache.save();
        INSTANCE.getLogger().info("[Housing2] Disabled");
        // lobbyDisplays.removeDisplays();
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public Gson getGson() {
        return GSON;
    }

}
