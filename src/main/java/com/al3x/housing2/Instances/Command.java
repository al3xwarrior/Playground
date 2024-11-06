package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private BukkitCommand command;

    private boolean loaded;
    private String name;
    private List<CommandArg> args;
    private int priorityRequired;
    private List<Action> actions;

    public Command(String name) {
        this.loaded = true;
        this.name = name;
        this.args = new ArrayList<>();
        this.priorityRequired = 0;
        this.actions = new ArrayList<>();
    }

    public Command(boolean loaded, String name, List<CommandArg> args, int priorityRequired, List<Action> actions) {
        this.loaded = loaded;
        this.name = name;
        this.args = args;
        this.priorityRequired = priorityRequired;
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setLoaded(boolean b, HousingWorld world) {
        this.loaded = b;

        if (!b) {
            Main.getInstance().getCommandFramework().unregisterCommand(getCommand(), world);
        }
    }

    public String getUsage() {
        StringBuilder usage = new StringBuilder(name);
        for (CommandArg arg : args) {
            usage.append(" ").append(arg.isRequired() ? "<" : "[").append(arg.getName()).append(arg.isGreedy() ? "..." : "").append(arg.isRequired() ? ">" : "]");
        }
        return usage.toString();
    }

    public BukkitCommand getCommand() {
        if (command == null) {
            command = new CommandRegistry(name, this);
        }
        return command;
    }

    public void execute(Player player, HousingWorld world) {
        ActionExecutor executor = new ActionExecutor();
        executor.addActions(actions);
        executor.execute(player, world, null);
    }


    public static class CommandArg {
        private ArgType type;
        private String name;
        private boolean required;
        private boolean greedy;

        public CommandArg(ArgType type, String name, boolean required, boolean greedy) {
            this.type = type;
            this.name = name;
            this.required = required;
            this.greedy = greedy;
        }

        public CommandArg() {
            this.type = ArgType.STRING;
            this.name = "arg";
            this.required = false;
            this.greedy = false;
        }

        public ArgType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public boolean isRequired() {
            return required;
        }

        public boolean isGreedy() {
            return greedy;
        }

        public void setName(String message) {
            this.name = message;
        }

        public void setType(ArgType type) {
            this.type = type;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public void setGreedy(boolean greedy) {
            this.greedy = greedy;
        }
    }

    public enum ArgType implements EnumMaterial {
        PLAYER(Material.PLAYER_HEAD),
        STRING(Material.NAME_TAG),
        INT(Material.REDSTONE),
        DOUBLE(Material.GOLD_INGOT),
        BOOLEAN(Material.LIME_DYE),;

        private Material mat;
        ArgType(Material mat) {
            this.mat = mat;
        }

        @Override
        public Material getMaterial() {
            return mat;
        }
    }

    public List<CommandArg> getArgs() {
        return args;
    }

    public void setArgs(List<CommandArg> args) {
        this.args = args;
    }

    public int getPriorityRequired() {
        return priorityRequired;
    }

    public void setPriorityRequired(int priorityRequired) {
        this.priorityRequired = priorityRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoaded() {
        return loaded;
    }
}


