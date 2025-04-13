package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ArgumentsProperty;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.CustomSlotProperty;
import com.al3x.housing2.Action.Properties.GenericPagination.FunctionProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

@ToString
public class FunctionAction extends HTSLImpl {

    public FunctionAction() {
        super(
                "function_action",
                "Function Action",
                "Executes a function.",
                Material.ACTIVATOR_RAIL,
                List.of("function")
        );

        getProperties().addAll(List.of(
                new FunctionProperty(
                        "function",
                        "Function",
                        "The function to execute."
                ),
                new BooleanProperty(
                        "run_for_all_players",
                        "Run for all players",
                        "Run the function for all players in the sandbox."
                ).setValue(false),
                new BooleanProperty(
                        "await",
                        "Await",
                        "Wait for the function to finish before continuing."
                ).setValue(false),
                new CustomSlotProperty<ActionProperty.Constant>(
                        "add_argument",
                        "Add Argument",
                        "Add a argument to be passed into the function.",
                        Material.PAPER,
                        50
                ) {

                    @Override
                    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
                        List<ArgumentsProperty.Argument> arguments = getProperty("arguments", ArgumentsProperty.class).getValue();
                        if (arguments.size() >= 6) {
                            player.sendMessage(colorize("&cYou can only have a maximum of 6 arguments."));
                            return;
                        }
                        player.sendMessage(colorize("&eEnter the argument value."));
                        menu.openChat(main, (message) -> {
                            if (message.length() > 16) {
                                player.sendMessage(colorize("&cThe argument name cannot be longer than 16 characters."));
                                return;
                            }
                            if (message.isEmpty()) {
                                player.sendMessage(colorize("&cThe argument name cannot be empty."));
                                return;
                            }
                            if (arguments.stream().anyMatch(arg -> arg.getName().equalsIgnoreCase(message))) {
                                player.sendMessage(colorize("&cThe argument name already exists."));
                                return;
                            }
                            arguments.add(new ArgumentsProperty.Argument(message, "%stat.player/" + message + "%"));
                            menu.open();
                        });
                    }
                },
                new ArgumentsProperty().setValue(new ArrayList<>())
        ));
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Function function = getValue("function", Function.class);
        if (function == null) {
            return OutputType.ERROR;
        }
        boolean await = getValue("await", Boolean.class);
        List<ArgumentsProperty.Argument> arguments = getValue("arguments", ArgumentsProperty.class).getValue();
        if (getValue("run_for_all_players", Boolean.class)) {
            return function.execute(Main.getInstance(), null, null, house, false, await, executor, arguments);
        } else {
            return function.execute(Main.getInstance(), player, player, house, false, await, executor, arguments);
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
