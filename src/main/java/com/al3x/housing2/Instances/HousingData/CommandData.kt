package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Command
import com.al3x.housing2.Instances.Command.CommandArg

data class CommandData(
    val loaded: Boolean = true,
    val command: String,
    val args: List<CommandArg>,
    val priorityRequired: Int,
    val actions: List<ActionData>
) {
    companion object {
        fun fromList(commands: List<Command>): List<CommandData> {
            val commandData = arrayListOf<CommandData>()
            for (command in commands) {
                commandData.add(
                    CommandData(
                        command.isLoaded,
                        command.name,
                        command.args,
                        command.priorityRequired,
                        ActionData.fromList(command.actions)
                    )
                )
            }
            return commandData
        }

        fun toList(Commands: MutableList<CommandData>?): MutableList<Command> {
            val commands = mutableListOf<Command>()
            if (Commands != null) {
                for (command in Commands) {
                    commands.add(
                        Command(
                            command.loaded,
                            command.command,
                            command.args,
                            command.priorityRequired,
                            ActionData.toList(command.actions)
                        )
                    )
                }
            }
            return commands
        }
    }
}
