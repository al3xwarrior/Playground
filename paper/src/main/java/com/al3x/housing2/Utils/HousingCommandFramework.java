package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HousingCommandFramework {
    private SimpleCommandMap map;
    public static List<String> customCommands = new ArrayList<>();
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

    public boolean hasCommand(String command) {
        return map.getCommand(command) != null;
    }

    public void registerCommand(String fallback, Command command) {
        map.register(fallback, command);
        customCommands.add(command.getName());
        customCommands.addAll(command.getAliases());
    }

    public void unregisterCommand(Command command, HousingWorld world) {
        command.unregister(map);
        customCommands.remove(command.getName());
        customCommands.removeAll(command.getAliases());

        //I am not 100% sure if this is the best way, but it works :)
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) field.get(map);
            knownCommands.remove(world.getName() + ":" + command.getName(), command);
            knownCommands.remove(command.getName(), command);
            field.set(map, knownCommands);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
