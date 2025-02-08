package com.al3x.housing2;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Commands.Protools.*;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.HouseEvents.Permissions.OpenSomething;
import com.al3x.housing2.Listeners.ProtocolLib.EntityInteraction;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Placeholders.papi.CookiesPlaceholder;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.HousingCommandFramework;
import com.al3x.housing2.Utils.SkinCache;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import com.maximde.hologramlib.HologramLib;
import com.maximde.hologramlib.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

public final class Main extends JavaPlugin {
    private static Main INSTANCE; // whats the point of this? can we just pass "this"? - al3x
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

        this.housesManager = new HousesManager(this);
        this.protoolsManager = new ProtoolsManager(this, housesManager);
        this.commandFramework = new HousingCommandFramework(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.cookieManager = new CookieManager(this, getDataFolder());
        this.clipboardManager = new ClipboardManager(this, getDataFolder());
        this.lobbyDisplays = new LobbyDisplays(housesManager);
        this.playerSpeedManager = new PlayerSpeedManager();

        loadItemsToCache();

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

        // Protools
        this.getCommand("wand").setExecutor(new Wand(this));
        this.getCommand("set").setExecutor(new Set(protoolsManager));
        this.getCommand("set").setTabCompleter(new BlockList.TabCompleter());
        this.getCommand("replace").setExecutor(new Replace(protoolsManager));
        this.getCommand("replace").setTabCompleter(new BlockList.DuoTabCompleter());
        this.getCommand("sphere").setExecutor(new Sphere(protoolsManager));
        this.getCommand("sphere").setTabCompleter(new BlockList.TabCompleter());
        this.getCommand("undo").setExecutor(new Undo(protoolsManager));

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
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityLimitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(housesManager), this);

//        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "housing:export");

        // House Events
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

        getServer().getLogger().info("[Housing2] Enabled");
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

    public LinkedHashMap<String, Integer> items = new LinkedHashMap<>();
    public void loadItemsToCache() {
        //read the resource "items.yml" and load it into the cache
        items.clear();
        File itemsFile = new File(getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            saveResource("items.yml", true);
        }
        YamlConfiguration customConfig = new YamlConfiguration();
        try {
            customConfig.load(itemsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for (String key : customConfig.getKeys(false)) {
            items.put(key, customConfig.getInt(key));
        }
    }
}
