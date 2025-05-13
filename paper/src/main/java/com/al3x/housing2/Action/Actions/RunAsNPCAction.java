package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ActionsProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
@Getter
@Setter
public class RunAsNPCAction extends HTSLImpl {
    public RunAsNPCAction(HousingNPC npc) {
        this();
        getProperty("npcId", NumberProperty.class).setValue(String.valueOf(npc.getInternalID()));
    }

    public RunAsNPCAction() {
        super(
                ActionEnum.RUN_AS_NPC,
                "Run As NPC Action",
                "Executes the action as an NPC.",
                Material.PLAYER_HEAD,
                List.of("runAsNPC")
        );

        getProperties().addAll(List.of(
                new NumberProperty(
                        "npcId",
                        "NPC ID",
                        "The ID of the NPC to run the action as."
                ),
                new ActionsProperty(
                        "subActions",
                        "Actions",
                        "The actions to execute."
                )
        ));
    }

    @Override
    public int nestLimit() {
        return 3;
    }

    @Override
    public int limit() {
        return 1;
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
        int npcId = getProperty("npcId", NumberProperty.class).parsedValue(house, player).intValue();

        HousingNPC npc = house.getNPC(npcId);

        ActionExecutor executor1 = new ActionExecutor("runAsNPC", subActions);
        executor1.setLimits(executor.getLimits());
        return executor1.execute(npc.getCitizensNPC(), player, house, event);
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
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " \"" + npcId + "\"" + " {\n" + builder + " ".repeat(indent) + "}";
//    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <npcID> {\\n<actions>\\n}";
    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        if (action.contains(" ")) {
//            Duple<String[], String> npcIdArg = handleArg(action.split(" "), 0);
//            this.npcId = npcIdArg.getSecond();
//            action = String.join(" ", npcIdArg.getFirst());
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
