package com.al3x.housing2.Instances;

import com.al3x.housing2.Commands.*;
import com.al3x.housing2.Commands.Function;
import com.al3x.housing2.Commands.Group;
import com.al3x.housing2.Commands.Protools.ProtoolsRegister;
import com.al3x.housing2.Main;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.al3x.housing2.Instances.CommandRegistry.commandArgsResults;

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
        new ViewPlayerStats(registrar, housesManager);
        new ViewGlobalStats(registrar, housesManager);
        new EditGlobalStats(registrar, housesManager);
        new EditPlayerStats(registrar, housesManager);
        new Function(registrar, housesManager);
        new SetHouseSize(registrar, housesManager);

        //Register the commands inside each house.
        HouseCommands.register(registrar);
    }
}
