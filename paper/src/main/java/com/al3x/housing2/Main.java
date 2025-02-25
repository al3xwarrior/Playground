package com.al3x.housing2;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Commands.Protools.*;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.HouseEvents.Permissions.OpenSomething;
import com.al3x.housing2.Listeners.ProtocolLib.EntityInteraction;
import com.al3x.housing2.Network.NetworkManager;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Placeholders.papi.CookiesPlaceholder;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.HousingCommandFramework;
import com.al3x.housing2.Utils.SkinCache;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import com.maximde.hologramlib.HologramLib;
import com.maximde.hologramlib.hologram.HologramManager;
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
    private SlimeLoader loader;
    private HousesManager housesManager;
    private HousingCommandFramework commandFramework;
    private ProtoolsManager protoolsManager;
    private ProtocolManager protocolManager;
    private CookieManager cookieManager;
    private ClipboardManager clipboardManager;
    private LobbyDisplays lobbyDisplays;
    private HologramManager hologramManager;
    private PlayerSpeedManager playerSpeedManager;
    private NetworkManager networkManager;
    private HeadDatabaseAPI headDatabaseAPI;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private String mineSkinKey;

    @Override
    public void onLoad() {
        HologramLib.onLoad(this);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        // The location of the worlds folder is relative to the server's root directory
        loader = new FileLoader(new File("./slime_worlds"));

        saveDefaultConfig();

        if (getConfig().contains("mineskin_key") && !Objects.equals(getConfig().getString("mineskin_key"), "your-mineskin-key")) {
            mineSkinKey = getConfig().getString("mineskin_key");
        } else {
            getLogger().warning("No MineSkin key found in config.yml. Skins will not be able to be loaded.");
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "playground:export");

        this.housesManager = new HousesManager(this);
        this.protoolsManager = new ProtoolsManager(this, housesManager);
        this.commandFramework = new HousingCommandFramework(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.cookieManager = new CookieManager(this, getDataFolder());
        this.clipboardManager = new ClipboardManager(this, getDataFolder());
        this.lobbyDisplays = new LobbyDisplays(housesManager);
        this.playerSpeedManager = new PlayerSpeedManager();
        this.networkManager = new NetworkManager(this);

        HologramLib.getManager().ifPresentOrElse(
                manager -> hologramManager = manager,
                () -> getLogger().severe("Failed to initialize HologramLib manager.")
        );

        getCommand("visit").setExecutor(new Visit());
        getCommand("housing").setExecutor(new Housing(housesManager, this));
        getCommand("housing").setTabCompleter(new Housing.TabCompleter());
        getCommand("home").setExecutor(new Home(this));
        getCommand("cancelinput").setExecutor(new CancelInput(this));
        getCommand("testplaceholder").setExecutor(new TestPlaceholder(this));
        getCommand("testplaceholder").setTabCompleter(new TestPlaceholder.TabCompleter());
        getCommand("placeholders").setExecutor(new Placeholders(this));
        getCommand("edit").setExecutor(new Edit(this));
        getCommand("setspawn").setExecutor(new SetSpawn(housesManager, this));
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("gamemode").setExecutor(new Gamemode(this));
        getCommand("gamemode").setTabCompleter(new Gamemode.TabCompleter());
        getCommand("hub").setExecutor(new Hub());
        getCommand("WTFMap").setExecutor(new WTFMap(housesManager));
        getCommand("broadcast").setExecutor(new Broadcast());
        getCommand("staffalerts").setExecutor(new StaffAlerts());
        getCommand("message").setExecutor(new Message());
        getCommand("reply").setExecutor(new Reply());
        getCommand("globalchat").setExecutor(new GlobalChat());
        getCommand("globalchat").setTabCompleter(new GlobalChat());
        getCommand("checkItemSize").setExecutor(new CheckItemSize());

        // Protools
        this.getCommand("wand").setExecutor(new Wand(this));
        this.getCommand("set").setExecutor(new Set(protoolsManager));
        this.getCommand("set").setTabCompleter(new BlockList.TabCompleter());
        this.getCommand("replace").setExecutor(new Replace(protoolsManager));
        this.getCommand("replace").setTabCompleter(new BlockList.DuoTabCompleter());
        this.getCommand("sphere").setExecutor(new Sphere(protoolsManager));
        this.getCommand("sphere").setTabCompleter(new BlockList.TabCompleter());
        this.getCommand("undo").setExecutor(new Undo(protoolsManager));
        this.getCommand("copy").setExecutor(new Copy(protoolsManager));
        this.getCommand("paste").setExecutor(new Paste(protoolsManager));
        this.getCommand("removeselection").setExecutor(new RemoveSelection(protoolsManager));

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new HousingMenuClickEvent(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveHouse(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HousingItems(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new NpcItems(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtoolsListener(this.protoolsManager), this);
        Bukkit.getPluginManager().registerEvents(new HologramInteractListener(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new TrashCanListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LaunchPadListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ActionButtonListener(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityLimitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(housesManager), this);

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
        Bukkit.getPluginManager().registerEvents(new FishBucket(housesManager), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        EntityInteraction.registerInteraction(housesManager);

        Runnables.startRunnables(this);

        Placeholder.registerPlaceholders();

        Bukkit.getPluginManager().registerEvents(new SkinCache(), this);
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, SkinCache::save, 0, 360000); // save skin cache every 5 minutes
        // ^ this never triggers for some reason but its no biggie. only really needed in case the server crashes

        // PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CookiesPlaceholder(this).register();
        }

        getServer().getLogger().info("[Housing2] Enabled just fine!");
    }

    @EventHandler
    public void onHeadDatabaseLoad(DatabaseLoadEvent e) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    public HousesManager getHousesManager() {
        return housesManager;
    }

    public ProtoolsManager getProtoolsManager() {
        return protoolsManager;
    }

    public SlimeLoader getLoader() {
        return loader;
    }

    public HousingCommandFramework getCommandFramework() {
        return commandFramework;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public String getMineSkinKey() {
        return mineSkinKey;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ClipboardManager getClipboardManager() {
        return clipboardManager;
    }

    public PlayerSpeedManager getPlayerSpeedManager() {
        return playerSpeedManager;
    }

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

    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    public LobbyDisplays getLobbyDisplays() {
        return this.lobbyDisplays;
    }

    public Gson getGson() {
        return GSON;
    }

    public HeadDatabaseAPI getHeadDatabaseAPI() {
        return headDatabaseAPI;
    }
}
