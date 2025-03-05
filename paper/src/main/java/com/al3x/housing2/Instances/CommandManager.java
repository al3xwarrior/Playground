package com.al3x.housing2.Instances;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Commands.Group;
import com.al3x.housing2.Commands.Protools.ProtoolsRegister;
import com.al3x.housing2.Main;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;

public class CommandManager {
    public void registerCommands(ReloadableRegistrarEvent<Commands> commands) {
        Commands registrar = commands.registrar();
        HousesManager housesManager = Main.getInstance().getHousesManager();

        // Register commands here
        new ProtoolsRegister(registrar, housesManager);
        new Broadcast(registrar);
        new CancelInput(registrar);
        new Edit(registrar, housesManager);
        new Find(registrar, housesManager);
        new Fly(registrar, housesManager);
        new Gamemode(registrar, housesManager);
        new GlobalChat(registrar);
        new Group(registrar, housesManager);
        new Home(registrar);
        new Housing(registrar, housesManager);
        new Hub(registrar);
        new LockHouse(registrar, housesManager);
        new Message(registrar);
        new Placeholders(registrar, housesManager);
        new Reply(registrar);
        new SetSpawn(registrar, housesManager);
        new StaffAlerts(registrar);
        new TestPlaceholders(registrar, housesManager);
        new Visit(registrar);
        new WTFMap(registrar, housesManager);
    }
}
