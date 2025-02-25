package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
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
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;

public class RunAsNPCAction extends HTSLImpl {
    private static final Gson gson = new Gson();
    private String npcId;
    private List<Action> subActions;

    public RunAsNPCAction(NPC npc) {
        super("Run As NPC Action");
        this.npcId = String.valueOf(npc.getId());
        this.subActions = new ArrayList<>();
    }

    public RunAsNPCAction(ArrayList<Action> subactions) {
        super("Run As NPC Action");
        this.subActions = subactions;
    }

    public RunAsNPCAction() {
        super("Run As NPC Action");
        this.subActions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RunAsNPCAction{" +
                "npcId=" + npcId +
                ", subActions=" + subActions +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eRun As NPC Action");
        builder.description("Change the settings for this action");
        builder.info("&eSettings", "");
        builder.info("NPC ID", npcId);
        builder.info("Actions", subActions.size());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&aRun As NPC Action");
        builder.description("Executes the action as an NPC");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("npcId",
                        ItemBuilder.create(Material.PLAYER_HEAD)
                                .name("&aNPC ID")
                                .info("&7Current Value", "&e" + npcId)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.STRING
                ),
                new ActionEditor.ActionItem("subActions",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aActions")
                                .info("&7Current Value", "")
                                .info(null, (subActions.isEmpty() ? "&cNo Actions" : "&a" + subActions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ACTION
                )
        );
        return new ActionEditor(4, "&eChat Action Settings", items);
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
    public boolean execute(Player player, HousingWorld house) {
        return false; // Not used
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (subActions.isEmpty()) {
            return true;
        }
        String parsed = Placeholder.handlePlaceholders(npcId, house, player);
        if (NumberUtilsKt.isInt(parsed)) {
            npcId = parsed;
        } else {
            return true;
        }

        HousingNPC npc = house.getNPC(Integer.parseInt(npcId));

        ActionExecutor executor1 = new ActionExecutor("runAsNPC", subActions);
        executor1.execute(npc.getCitizensNPC(), player, house, event);
        return true;
    }

    public List<Action> getSubActions() {
        return subActions;
    }

    public void setSubActions(ArrayList<Action> subActions) {
        this.subActions = subActions;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("subActions", Companion.fromList(subActions));
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
        ArrayList<com.al3x.housing2.Instances.HousingData.ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            com.al3x.housing2.Instances.HousingData.ActionData action = gson.fromJson(jsonObject, com.al3x.housing2.Instances.HousingData.ActionData.class);
            actions.add(action);
        }

        this.subActions = Companion.toList(actions);
        this.npcId = String.valueOf(data.get("npcId"));
    }

    @Override
    public String export(int indent) {
        StringBuilder builder = new StringBuilder();
        for (Action action : subActions) {
            if (!(action instanceof HTSLImpl impl)) continue;
            builder.append(impl.export(indent + 4)).append("\n");
        }
        if (builder.isEmpty()) return " ".repeat(indent) + keyword();
        return " ".repeat(indent) + keyword() + "\"" + npcId + "\"" + " {\n" + builder + " ".repeat(indent) + "}";
    }

    @Override
    public String syntax() {
        return "runAsNPC <npcID> {\\n<actions>\\n}";
    }

    @Override
    public String keyword() {
        return "runAsNPC";
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
