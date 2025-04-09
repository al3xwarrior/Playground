package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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
    private static final Gson gson = new Gson();
    private String npcId;
    private List<Action> subActions = new ArrayList<>();

    public RunAsNPCAction(HousingNPC npc) {
        this();
        this.npcId = String.valueOf(npc.getInternalID());
    }

    public RunAsNPCAction() {
        super(
                "run_as_npc_action",
                "Run As NPC Action",
                "Executes the action as an NPC.",
                Material.PLAYER_HEAD,
                List.of("runAsNPC")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "npcId",
                        "NPC ID",
                        "The ID of the NPC to run the action as.",
                        ActionProperty.PropertyType.NUMBER
                ),
                new ActionProperty(
                        "subActions",
                        "Actions",
                        "The actions to execute.",
                        ActionProperty.PropertyType.ACTION
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
        if (subActions.isEmpty()) {
            return OutputType.SUCCESS;
        }
        String parsed = Placeholder.handlePlaceholders(npcId, house, player);
        String npcId;
        if (NumberUtilsKt.isInt(parsed)) {
            npcId = parsed;
        } else {
            return OutputType.ERROR;
        }

        HousingNPC npc = house.getNPC(Integer.parseInt(npcId));

        ActionExecutor executor1 = new ActionExecutor("runAsNPC", subActions);
        executor1.setLimits(executor.getLimits());
        return executor1.execute(npc.getCitizensNPC(), player, house, event);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("subActions", ActionData.fromList(subActions));
        data.put("npcId", npcId);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (!data.containsKey("subActions")) return;
        // I don't know how this works lol
        Object subActions = data.get("subActions");
        JsonArray jsonArray = gson.toJsonTree(subActions).getAsJsonArray();
        ArrayList<ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            ActionData action = gson.fromJson(jsonObject, ActionData.class);
            actions.add(action);
        }

        this.subActions = ActionData.toList(actions);
        this.npcId = String.valueOf(data.get("npcId"));
    }

    @Override
    public String export(int indent) {
        StringBuilder builder = new StringBuilder();
        for (Action action : subActions) {
            if (!(action instanceof HTSLImpl impl)) continue;
            builder.append(impl.export(indent + 4)).append("\n");
        }
        if (builder.isEmpty()) return " ".repeat(indent) + getScriptingKeywords().getFirst();
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " \"" + npcId + "\"" + " {\n" + builder + " ".repeat(indent) + "}";
    }

    @Override
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <npcID> {\\n<actions>\\n}";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        if (action.contains(" ")) {
            Duple<String[], String> npcIdArg = handleArg(action.split(" "), 0);
            this.npcId = npcIdArg.getSecond();
            action = String.join(" ", npcIdArg.getFirst());
        }
        ArrayList<String> subactions = new ArrayList<>();
        if (action.startsWith("{")) {
            for (int i = 0; i < nextLines.size(); i++) {
                String line = nextLines.get(i);
                if (line.startsWith(indent + "}")) {
                    nextLines = new ArrayList<>(nextLines.subList(i, nextLines.size()));
                    break;
                }
                subactions.add(line);
            }
        }

        ArrayList<Action> actions = new ArrayList<>(HTSLHandler.importActions(String.join("\n", subactions), indent + "    "));

        this.subActions = actions;
        return nextLines;
    }
}
