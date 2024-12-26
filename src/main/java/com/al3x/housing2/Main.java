package com.al3x.housing2;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Commands.Protools.*;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.ProtocolLib.EntityInteraction;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.HousingCommandFramework;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {
    private static Main INSTANCE; // whats the point of this? can we just pass "this"? - al3x
    private SlimeLoader loader;
    private HousesManager housesManager;
    private HousingCommandFramework commandFramework;
    private ProtoolsManager protoolsManager;
    private ProtocolManager protocolManager;

    //    private MineSkinClientImpl mineSkinClient;
    private String mineSkinKey;

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

        getCommand("housing").setExecutor(new Housing(housesManager, this));
        getCommand("housing").setTabCompleter(new Housing.TabCompleter());
        getCommand("home").setExecutor(new Home(this));
        getCommand("cancelinput").setExecutor(new CancelInput(this));
        getCommand("testplaceholder").setExecutor(new TestPlaceholder(this));
        getCommand("testplaceholder").setTabCompleter(new TestPlaceholder.TabCompleter());
        getCommand("placeholders").setExecutor(new Placeholders(this));
        getCommand("edit").setExecutor(new Edit(this));

        // Protools
        this.getCommand("wand").setExecutor(new Wand(this));
        this.getCommand("set").setExecutor(new Set(protoolsManager));
        this.getCommand("set").setTabCompleter(new BlockList.TabCompleter());
        this.getCommand("sphere").setExecutor(new Sphere(protoolsManager));
        this.getCommand("sphere").setTabCompleter(new BlockList.TabCompleter());

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new HousingMenuClickEvent(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveHouse(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HousingItems(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtoolsListener(this.protoolsManager), this);
        Bukkit.getPluginManager().registerEvents(new HologramInteractListener(this, housesManager), this);

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

        EntityInteraction.registerInteraction(housesManager);

        Runnables.startRunnables(this);

        HandlePlaceholders.registerPlaceholders();

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

    public String getMineSkinKey() {
        return mineSkinKey;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("[Housing2] Saving and unloading houses...");
        Runnables.stopRunnables();
        for (HousingWorld house : housesManager.getConcurrentLoadedHouses().values()) {
            housesManager.saveHouseAndUnload(house);
        }
        getServer().getLogger().info("[Housing2] Disabled");
    }

    public static Main getInstance() {
        return INSTANCE;
    }


}
