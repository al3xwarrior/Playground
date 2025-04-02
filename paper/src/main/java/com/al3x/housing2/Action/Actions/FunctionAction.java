package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.al3x.housing2.Utils.Color.colorize;

@ToString
public class FunctionAction extends HTSLImpl {
    @Setter
    String function;
    boolean await;
    boolean runForAllPlayers;

    LinkedHashMap<String, String> arguments = new LinkedHashMap<>();

    public FunctionAction() {
        super(
                "function_action",
                "Function Action",
                "Executes a function.",
                Material.ACTIVATOR_RAIL,
                List.of("function")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "function",
                        "Function",
                        "The function to execute.",
                        ActionProperty.PropertyType.FUNCTION
                ),
                new ActionProperty(
                        "run_for_all_players",
                        "Run for all players",
                        "Run the function for all players in the sandbox.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "await",
                        "Await",
                        "Wait for the function to finish before continuing.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "add_argument",
                        "Add Argument",
                        "Add a argument to be passed into the function.",
                        ActionProperty.PropertyType.CUSTOM, 52,
                        (house, backMenu, player) -> (event, o) -> {
                            if (arguments.size() >= 6) {
                                backMenu.getOwner().sendMessage(colorize("&cYou can only have a maximum of 6 arguments."));
                                return false;
                            }
                            arguments.put("name", "%player.name%");
                            backMenu.open();
                            return true;
                        }
                )
        ));

        arguments.forEach((key, value) -> {
            getProperties().add(new ActionProperty(
                    "arg_" + key,
                    "Argument: " + key,
                    "The argument to pass to the function.",
                    ActionProperty.PropertyType.STRING,
                    (house, backMenu, player) -> {
                        return argConsumer(house, backMenu, player, key, value);
                    }
            ));
        });
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> argConsumer(HousingWorld house, Menu backMenu, Player player, String argKey, String argument) {
        return (event, o) -> {
            if (event.isLeftClick()) {
                backMenu.getOwner().sendMessage(colorize("&eEnter the new argument value."));
                backMenu.openChat(Main.getInstance(), (message) -> {
                    arguments.put(argKey, message);
                    backMenu.open();
                });
                return true;
            } else if (event.isRightClick()) {
                arguments.remove(argKey);
                backMenu.open();
                return true;
            }
            return false;
        };
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
        if (function == null) {
            return OutputType.ERROR;
        }
        Function functionData = house.getFunction(function);
        if (functionData == null) {
            return OutputType.ERROR;
        }
        if (runForAllPlayers) {
            return functionData.execute(Main.getInstance(), null, null, house, false, await, executor, arguments);
        } else {
            return functionData.execute(Main.getInstance(), player, player, house, false, await, executor, arguments);
        }
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        function = (String) data.get("function");
        runForAllPlayers = (boolean) data.get("runForAllPlayers");
        if (data.get("await") == null) {
            await = false;
        } else {
            await = (boolean) data.get("await");
        }
        arguments = new LinkedHashMap<>();
        if (data.get("arguments") == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : ((LinkedTreeMap<String, Object>) data.get("arguments")).entrySet()) {
            arguments.put(entry.getKey(), (String) entry.getValue());
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
