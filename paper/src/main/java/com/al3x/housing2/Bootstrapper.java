package com.al3x.housing2;

import com.al3x.housing2.Instances.CommandManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class Bootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {

            CommandManager commandManager = new CommandManager();
            commandManager.registerCommands(commands);

        });
    }
}
