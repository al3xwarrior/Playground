package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ActionsProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

@ToString
@Getter
@Setter
public class RandomAction extends HTSLImpl implements NPCAction {
    public RandomAction() {
        super(ActionEnum.RANDOM_ACTION,
                "Random Action",
                "Executes a random action from the list of actions.",
                Material.ENDER_CHEST,
                List.of("random")
        );

        getProperties().add(new ActionsProperty(
                "subActions",
                "Actions",
                "The actions to execute."
        ));
    }

    @Override
    public int nestLimit() {
        return 3;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        List<Action> subActions = getProperty("subActions", ActionsProperty.class).getValue();
        if (subActions.isEmpty()) {
            return OutputType.SUCCESS;
        }

        ActionExecutor executor1 = new ActionExecutor("random", new ArrayList<>(Collections.singletonList(subActions.get((int) (house.getRandom().nextDouble() * subActions.size())))));
        executor1.setLimits(executor.getLimits());
        executor1.onComplete((ActionExecutor e) -> {
            if (executor != null) {
                executor.execute(player, house, event);
            }
        });
        executor1.execute(player, house, event);
        return OutputType.RUNNING;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        execute(player, house, event, executor);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
//
//    @Override
//    public String export(int indent) {
//        StringBuilder builder = new StringBuilder();
//        for (Action action : subActions) {
//            if (!(action instanceof HTSLImpl impl)) continue;
//            builder.append(impl.export(indent + 4)).append("\n");
//        }
//        if (builder.isEmpty()) return " ".repeat(indent) + getScriptingKeywords().getFirst();
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " {\n" + builder + " ".repeat(indent) + "}";
//    }
//
    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " {\\n<actions>\\n}";
    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        ArrayList<String> subactions = new ArrayList<>();
//        if (action.startsWith(indent + "{")) {
//            for (int i = 0; i < nextLines.size(); i++) {
//                String line = nextLines.get(i);
//                if (line.startsWith(indent + "}")) {
//                    nextLines = new ArrayList<>(nextLines.subList(i + 1, nextLines.size()));
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
