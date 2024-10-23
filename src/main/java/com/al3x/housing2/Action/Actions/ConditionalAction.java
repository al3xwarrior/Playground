package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;

public class ConditionalAction extends Action {
    private static final Gson gson = new Gson();
    private List<Condition> conditions;
    private List<Action> ifActions;
    private List<Action> elseActions;

    public ConditionalAction(ArrayList<Action> ifActions, ArrayList<Action> elseActions) {
        super("Conditional Action");
        this.ifActions = ifActions;
        this.elseActions = elseActions;
    }

    public ConditionalAction() {
        super("Conditional Action");
        this.ifActions = new ArrayList<>();
        this.elseActions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "f";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.REDSTONE);
        builder.name("&eConditional");
        builder.description("Change the settings for this action");
        builder.info("&eSettings", "");
        builder.info("Conditions", conditions.size());
        builder.info("If Actions", ifActions.size());
        builder.info("Else Actions", elseActions.size());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.REDSTONE);
        builder.name("&aConditional");
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
                                .info(null, (conditions.isEmpty() ? "&cNo Actions" : "&a" + conditions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ACTION
                )
        );
        return new ActionEditor(4, "&eChat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        /*
        if (subActions.isEmpty()) {
            return true;
        }

         */

        //Action action = conditions.get((int) (house.getRandom().nextDouble() * conditions.size()));
        //return action.execute(player, house);
        return false;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setSubActions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        //data.put("conditions", Companion.fromList(conditions));
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

        //this.conditions = Companion.toList(actions);
    }
}
