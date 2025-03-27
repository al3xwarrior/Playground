package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Action.ActionEditor.ActionItem.ActionType.CUSTOM;
import static com.al3x.housing2.Utils.Color.colorize;

public class FunctionAction extends HTSLImpl {
    String function;
    boolean await;
    boolean runForAllPlayers;

    LinkedHashMap<String, String> arguments = new LinkedHashMap<>();

    public FunctionAction() {
        super("Function Action");
    }

    public FunctionAction(String function, boolean runForAllPlayers) {
        super("Function Action");
        this.function = function;
        this.runForAllPlayers = runForAllPlayers;
    }

    @Override
    public String toString() {
        return "FunctionAction (function: " + (function == null ? "&cNone" : "&6" + function) + ", runForAllPlayers: " + runForAllPlayers + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ACTIVATOR_RAIL);
        builder.name("&eTrigger Function");
        builder.info("&eSettings", "");
        builder.info("Function", (function == null ? "&cNone" : "&6" + function));
        builder.info("Run for all players", runForAllPlayers ? "&aYes" : "&cNo");
        builder.info("Await", await ? "&aYes" : "&cNo");
        builder.info("Arguments", "&e" + arguments.size());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ACTIVATOR_RAIL);
        builder.name("&aTrigger Function");
        builder.description("Executes a given function");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items = new ArrayList<>(List.of(
                new ActionEditor.ActionItem("function", ItemBuilder.create(Material.ACTIVATOR_RAIL)
                        .name("&aFunction")
                        .description("Select a function to run")
                        .info("&7Current Value", "")
                        .info(null, (function == null ? "&cNone" : "&6" + function)),
                        ActionEditor.ActionItem.ActionType.FUNCTION
                ),
                new ActionEditor.ActionItem("runForAllPlayers", ItemBuilder.create((runForAllPlayers ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aRun for all players")
                        .description("Run the function for all players in the world")
                        .info("&7Current Value", "")
                        .info(null, runForAllPlayers ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("await", ItemBuilder.create((await ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aAwait")
                        .description("Wait for the function to finish before continuing")
                        .info("&7Current Value", "")
                        .info(null, await ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        ));

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

    public void setFunction(String function) {
        this.function = function;
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
