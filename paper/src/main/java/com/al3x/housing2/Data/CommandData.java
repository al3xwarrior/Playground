package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.Command.CommandArg;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommandData {
    private boolean loaded;
    private String command;
    private List<CommandArg> args;
    private int priorityRequired;
    private List<ActionData> actions;

    public CommandData() {
    }

    public CommandData(boolean loaded, String command, List<CommandArg> args, int priorityRequired, List<ActionData> actions) {
        this.loaded = loaded;
        this.command = command;
        this.args = args;
        this.priorityRequired = priorityRequired;
        this.actions = actions;
    }

    public static List<CommandData> fromList(List<Command> commands) {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commands) {
            commandData.add(new CommandData(
                    command.isLoaded(),
                    command.getName(),
                    command.getArgs(),
                    command.getPriorityRequired(),
                    ActionData.fromList(command.getActions())
            ));
        }
        return commandData;
    }

    public static List<Command> toList(List<CommandData> commandDataList, HousingWorld house) {
        List<Command> commands = new ArrayList<>();
        if (commandDataList != null) {
            for (CommandData commandData : commandDataList) {
                commands.add(new Command(
                        commandData.isLoaded(),
                        commandData.getCommand(),
                        commandData.getArgs(),
                        commandData.getPriorityRequired(),
                        ActionData.toList(commandData.getActions(), house)
                ));
            }
        }
        return commands;
    }
}