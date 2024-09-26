package com.al3x.housing2;

import com.al3x.housing2.Commands.Housing;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Listeners.HouseEvents.JoinHouse;
import com.al3x.housing2.Listeners.HouseEvents.LeaveHouse;
import com.al3x.housing2.Listeners.HousingMenuClickEvent;
import com.al3x.housing2.Listeners.JoinLeaveHouse;
import com.al3x.housing2.Listeners.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private HousesManager housesManager;

    @Override
    public void onEnable() {

        this.housesManager = new HousesManager();

        getCommand("housing").setExecutor(new Housing(housesManager));

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new HousingMenuClickEvent(this, housesManager), this);
        Bukkit.getPluginManager().registerEvents(new LeaveHouse(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinHouse(housesManager), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveHouse(this, housesManager), this);

        getServer().getLogger().info("[Housing2] Enabled");
    }

    @Override
    public void onDisable() {}
}
