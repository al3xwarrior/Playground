package com.al3x.housing2;

import com.al3x.housing2.Commands.Housing;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.*;
import com.al3x.housing2.Listeners.HouseEvents.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

// spawn particle
// spawn entity
// spectator mode
// protool action (client side option)
// https://www.youtube.com/watch?v=f3UY8ySackU
// clear enderchest
// booss bar
// string stats
// drop item at coords
// launch projectile
// player whitelist
// chat event
// clear player stats action
// scoreboard layouts

public final class Main extends JavaPlugin {
    private SlimeLoader loader;
    private HousesManager housesManager;

    @Override
    public void onEnable() {
        loader = new FileLoader(new File("./slime_worlds"));
        this.housesManager = new HousesManager(this);

        getCommand("housing").setExecutor(new Housing(housesManager, this));

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

        getServer().getLogger().info("[Housing2] Enabled");
    }

    public HousesManager getHousesManager() {
        return housesManager;
    }

    public SlimeLoader getLoader() {
        return loader;
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("[Housing2] Saving houses...");
        for (HousingWorld house : housesManager.getLoadedHouses()) {
            house.getNPCs().forEach((npc) -> {
                npc.getCitizensNPC().destroy();
            });
            house.save();
        }
        getServer().getLogger().info("[Housing2] Disabled");

    }
}
