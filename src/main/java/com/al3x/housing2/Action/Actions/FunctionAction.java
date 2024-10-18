package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class FunctionAction extends Action {
    Function function;
    boolean runForAllPlayers;
    public FunctionAction() {
        super("Function Action");
    }

    public FunctionAction(Function function, boolean runForAllPlayers) {
        super("Function Action");
        this.function = function;
        this.runForAllPlayers = runForAllPlayers;
    }

    @Override
    public String toString() {
        return "FunctionAction (" + function.getName() + ", " + runForAllPlayers + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.MAP);
        builder.name("&eTrigger Function");
        builder.description("Runs a function");
        builder.info("&eSettings", "");
        builder.info("Function", function.getName());
        builder.info("Run for all players", runForAllPlayers ? "Yes" : "No");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.MAP);
        builder.name("&aTrigger Function");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("function", ItemBuilder.create(Material.WRITTEN_BOOK)
                        .name("&aFunction")
                        .description("Select a function to run")
                        .info("&7Current Value", "")
                        .info(null, function.getName()),
                        ActionEditor.ActionItem.ActionType.FUNCTION
                ),
                new ActionEditor.ActionItem("runForAllPlayers", ItemBuilder.create(Material.WRITTEN_BOOK)
                        .name("&aRun for all players")
                        .description("Run the function for all players in the world")
                        .info("&7Current Value", "")
                        .info(null, runForAllPlayers ? "Yes" : "No"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&eFunction Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (runForAllPlayers) {
            for (Player p : house.getWorld().getPlayers()) {
                function.execute(Main.getInstance(), p, house);
            }
        } else {
            function.execute(Main.getInstance(), player, house);
        }
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        return null;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
