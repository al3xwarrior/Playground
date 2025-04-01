package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.function.BiFunction;

import static com.al3x.housing2.Action.ActionEditor.ActionItem.ActionType.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;
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
                        "fun_for_all_players",
                        "Run for all players",
                        "Run the function for all players in the sandbox.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "await",
                        "Await",
                        "Wait for the function to finish before continuing.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));

        for (String argKey : arguments.keySet()) {
            String argument = arguments.get(argKey);
            getProperties().add(new ActionProperty(
                    "arg_" + argKey,
                    "Argument: " + argKey,
                    "The argument to pass to the function.",
                    ActionProperty.PropertyType.STRING,
                    this::argConsumer
            ));
        }
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> argConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, o) -> {
            if (event.isLeftClick()) {
                backMenu.getOwner().sendMessage(colorize("&eEnter the new argument value."));
                backMenu.openChat(Main.getInstance(), (message) -> {
                    arguments.put((String) o, message);
                    backMenu.open();
                });
                return true;
            } else if (event.isRightClick()) {
                arguments.remove(o); // FIXME
                backMenu.open();
                return true;
            }
            return false;
        };
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        ArrayList<String> argKeys = new ArrayList<>(arguments.keySet());
        for (String argKey : argKeys) {
            String argument = arguments.get(argKey);
            items.add(new ActionEditor.ActionItem("null",
                    ItemBuilder.create(Material.PAPER)
                            .name("&aArgument: &6" + argKey)
                            .description("Edit the argument")
                            .info("&7Current Value", "")
                            .info(null, "&6" + argument)
                            .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                            .rClick(ItemBuilder.ActionType.REMOVE_YELLOW),
                    (event, obj) -> {
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
                    }
            ));
        }

        items.add(new ActionEditor.ActionItem(
                ItemBuilder.create(Material.PAPER)
                        .name("&aAdd Argument")
                        .description("Add a argument to be passed into the function."),
                CUSTOM, 50, (event, obj) -> {
            if (arguments.size() >= 6) {
                backMenu.getOwner().sendMessage(colorize("&cYou can only have a maximum of 6 arguments."));
                return false;
            }
            arguments.put("name", "%player.name%");
            backMenu.open();
            return true;
        }
        ));

        return new ActionEditor(6, "&eFunction Action Settings", items);
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("function", function == null ? null : function);
        data.put("runForAllPlayers", runForAllPlayers);
        data.put("await", await);
        data.put("arguments", arguments);
        return data;
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

    @Override
    public String keyword() {
        return "function";
    }
}
