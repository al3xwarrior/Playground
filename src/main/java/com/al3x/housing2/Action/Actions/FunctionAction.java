package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.FunctionData;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class FunctionAction extends HTSLImpl {
    String function;
    boolean runForAllPlayers;
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
        builder.description("Runs a function");
        builder.info("&eSettings", "");
        builder.info("Function", (function == null ? "&cNone" : "&6" + function));
        builder.info("Run for all players", runForAllPlayers ? "&aYes" : "&cNo");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ACTIVATOR_RAIL);
        builder.name("&aTrigger Function");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items =  List.of(
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
                )
        );
        return new ActionEditor(4, "&eFunction Action Settings", items);
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (function == null) {
            return false;
        }
        Function functionData = house.getFunction(function);
        if (functionData == null) {
            return false;
        }
        if (runForAllPlayers) {
            for (Player p : house.getWorld().getPlayers()) {
                functionData.execute(Main.getInstance(), p, house);
            }
        } else {
            functionData.execute(Main.getInstance(), player, house);
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("function", function == null ? null : function);
        data.put("runForAllPlayers", runForAllPlayers);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        function = (String) data.get("function");
        runForAllPlayers = (boolean) data.get("runForAllPlayers");
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
