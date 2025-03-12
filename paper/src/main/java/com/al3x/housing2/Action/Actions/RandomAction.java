package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;
import static com.al3x.housing2.Utils.Color.colorize;

public class RandomAction extends HTSLImpl implements NPCAction {
    private static final Gson gson = new Gson();
    private List<Action> subActions;

    public RandomAction(ArrayList<Action> subactions) {
        super("Random Action");
        this.subActions = subactions;
    }

    public RandomAction() {
        super("Random Action");
        this.subActions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RandomAction (SubActions: " + subActions.stream().map(Action::toString).reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_CHEST);
        builder.name("&eRandom Action");
        builder.description("Change the settings for this action");
        builder.info("&eSettings", "");
        builder.info("Actions", subActions.size());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.ENDER_CHEST);
        builder.name("&aRandom Action");
        builder.description("Executes a single random action form the selected actions.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
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
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (subActions.isEmpty()) {
            return OutputType.SUCCESS;
        }

        ActionExecutor executor1 = new ActionExecutor("random", new ArrayList<>(Collections.singletonList(subActions.get((int) (house.getRandom().nextDouble() * subActions.size())))));
        executor1.setLimits(executor.getLimits());
        return executor1.execute(player, house, event);
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {

        execute(player, house, event, executor);
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
    }

    @Override
    public String export(int indent) {
        StringBuilder builder = new StringBuilder();
        for (Action action : subActions) {
            if (!(action instanceof HTSLImpl impl)) continue;
            builder.append(impl.export(indent + 4)).append("\n");
        }
        if (builder.isEmpty()) return " ".repeat(indent) + keyword();
        return " ".repeat(indent) + keyword() + " {\n" + builder + " ".repeat(indent) + "}";
    }

    @Override
    public String syntax() {
        return "random {\\n<actions>\\n}";
    }

    @Override
    public String keyword() {
        return "random";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        ArrayList<String> subactions = new ArrayList<>();
        if (action.startsWith(indent + "{")) {
            for (int i = 0; i < nextLines.size(); i++) {
                String line = nextLines.get(i);
                if (line.startsWith(indent + "}")) {
                    nextLines = new ArrayList<>(nextLines.subList(i + 1, nextLines.size()));
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
