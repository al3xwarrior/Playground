package com.al3x.housing2;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.HousingCommandFramework;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {
    private static Main INSTANCE;
    private SlimeLoader loader;
    private HousesManager housesManager;
    private HousingCommandFramework commandFramework;

    @Override
    public void onEnable() {
        INSTANCE = this;
        // The location of the worlds folder is relative to the server's root directory
        loader = new FileLoader(new File("./slime_worlds"));

        this.housesManager = new HousesManager(this);

        commandFramework = new HousingCommandFramework(this);

        getCommand("housing").setExecutor(new Housing(housesManager, this));
        getCommand("home").setExecutor(new Home(this));
        getCommand("cancelinput").setExecutor(new CancelInput(this));
        getCommand("testplaceholder").setExecutor(new TestPlaceholder(this));
        getCommand("placeholders").setExecutor(new Placeholders(this));

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new HousingMenuClickEvent(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveHouse(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HousingItems(housesManager), this);

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

        Runnables.startRunnables(this);

        HandlePlaceholders.registerPlaceholders();

        getServer().getLogger().info("[Housing2] Enabled");
    }

    public HousesManager getHousesManager() {
        return housesManager;
    }

    public SlimeLoader getLoader() {
        return loader;
    }

    public HousingCommandFramework getCommandFramework() {
        return commandFramework;
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
