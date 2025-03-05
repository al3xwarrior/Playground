package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandRegistry  {
    public static HashMap<UUID, List<String>> commandArgsResults = new HashMap<>();

    private Command command;

    protected CommandRegistry(@NotNull String name, Command comand) {
        this.command = comand;
    }
}
