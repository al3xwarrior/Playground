package com.al3x.housing2.Instances;

import com.al3x.housing2.Commands.newcommands.Home;
import com.al3x.housing2.Commands.newcommands.Visit;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;

public class CommandManager {
    public void registerCommands(ReloadableRegistrarEvent<Commands> commands) {
        Commands registrar = commands.registrar();

        // Register commands here
        new Visit(registrar);
        new Home(registrar);
    }
}
