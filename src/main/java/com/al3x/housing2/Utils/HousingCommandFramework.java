package com.al3x.housing2.Utils;

import com.al3x.housing2.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class HousingCommandFramework {
    private SimpleCommandMap map;
    private Plugin plugin;
    public HousingCommandFramework(Main main) {
        this.plugin = main;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (SimpleCommandMap) field.get(manager);
            } catch (IllegalArgumentException | NoSuchFieldException | IllegalAccessException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerCommand(String fallback, Command command) {
        map.register(fallback, command);
    }

    public void unregisterCommand(Command command) {
        command.unregister(map);
    }
}
