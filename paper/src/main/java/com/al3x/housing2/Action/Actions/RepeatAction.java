package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ActionsProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


@ToString
@Getter
public class RepeatAction extends HTSLImpl implements NPCAction {
    public RepeatAction() {
        super(ActionEnum.REPEAT,
                "Repeat Action",
                "Repeats the actions in the list a specified number of times.",
                Material.REPEATER,
                List.of("repeat")
        );

        getProperties().addAll(List.of(
                new ActionsProperty(
                        "subActions",
                        "Actions",
                        "The actions to execute."
                ),
                new NumberProperty(
                        "times",
                        "Times",
                        "The number of times to repeat the actions.",
                        1.0, 20.0
                ).setValue(5.0)
        ));
    }

    @Override
    public int nestLimit() {
        return 4;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS;
    }


    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor oldExecutor) {
        List<Action> subActions = getProperty("subActions", ActionsProperty.class).getValue();
        if (subActions.isEmpty()) {
            return OutputType.SUCCESS;
        }

        int timesInt = getProperty("times", NumberProperty.class).parsedValue(house, player).intValue();

        ActionExecutor first = null;
        ActionExecutor last = null;
        for (int i = 0; i < timesInt; i++) {
            ActionExecutor executor = new ActionExecutor("repeat");
            executor.addActions(subActions);
            executor.setLimits(oldExecutor.getLimits());
            executor.onBreak((ActionExecutor e) -> oldExecutor.execute(player, house, event));
            if (last != null) {
                last.onComplete((ActionExecutor e) -> executor.execute(player, house, event));
            }
            if (first == null) {
                first = executor;
            }
            last = executor;
        }
        if (first == null) {
            return OutputType.ERROR;
        }
        last.onComplete((ActionExecutor e) -> oldExecutor.execute(player, house, event));
        first.execute(player, house, event);

        return OutputType.RUNNING;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        execute(player, house, event, executor);
    }

    @Override
    public int limit() {
        return 5;
    }


    @Override
    public boolean requiresPlayer() {
        return false;
    }

//    @Override
//    public String export(int indent) {
//        StringBuilder builder = new StringBuilder();
//        for (Action action : subActions) {
//            if (!(action instanceof HTSLImpl impl)) continue;
//            builder.append(impl.export(indent + 4)).append("\n");
//        }
//        if (builder.isEmpty()) return " ".repeat(indent) + getScriptingKeywords().getFirst();
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + times + " {\n" + builder + " ".repeat(indent) + "}";
//    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <times> {\\n<actions>\\n}";
    }

//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        if (indent.length() > 4 * 3) {
//            throw new IllegalArgumentException("Nesting limit reached"); //TODO: change this to a proper exception
//        }
//        if (action.contains(" ")) {
//            Duple<String[], String> timesArg = handleArg(action.split(" "), 0);
//            this.times = timesArg.getSecond();
//            action = String.join(" ", timesArg.getFirst());
//        }
//        ArrayList<String> subactions = new ArrayList<>();
//        if (action.startsWith("{")) {
//            for (int i = 0; i < nextLines.size(); i++) {
//                String line = nextLines.get(i);
//                if (line.startsWith(indent + "}")) {
//                    nextLines = new ArrayList<>(nextLines.subList(i, nextLines.size()));
//                    break;
//                }
//                subactions.add(line);
//            }
//        }
//
//        ArrayList<Action> actions = new ArrayList<>(HTSLHandler.importActions(String.join("\n", subactions), indent + "    "));
//
//        this.subActions = actions;
//        return nextLines;
//    }
}
