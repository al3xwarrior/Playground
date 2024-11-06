package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Instances.HousingData.ActionData;
import com.al3x.housing2.Instances.HousingData.ConditionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;

public class ConditionalAction extends Action {
    private static final Gson gson = new Gson();
    private List<Condition> conditions;
    private boolean matchAnyCondition;
    private List<Action> ifActions;
    private List<Action> elseActions;

    public ConditionalAction(ArrayList<Condition> conditions, boolean matchAnyCondition, ArrayList<Action> ifActions, ArrayList<Action> elseActions) {
        super("Conditional Action");
        this.conditions = conditions;
        this.matchAnyCondition = matchAnyCondition;
        this.ifActions = ifActions;
        this.elseActions = elseActions;
    }

    public ConditionalAction() {
        super("Conditional Action");
        this.conditions = new ArrayList<>();
        this.matchAnyCondition = false;
        this.ifActions = new ArrayList<>();
        this.elseActions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ConditionalAction(conditions=" + this.conditions + ", matchAnyCondition=" + this.matchAnyCondition + ", ifActions=" + this.ifActions + ", elseActions=" + this.elseActions + ")";
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
        builder.description("Executes actions based on certain conditions.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("conditions",
                        ItemBuilder.create(Material.COMPARATOR)
                                .name("&aActions")
                                .info("&7Current Value", "")
                                .info(null, (conditions.isEmpty() ? "&cNo Actions" : "&a" + conditions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.CONDITION
                ),
                new ActionEditor.ActionItem("matchAnyCondition", ItemBuilder.create((matchAnyCondition ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aMatch Any Condition")
                        .description("When enabled ony one condition needs to match, otherwise all conditions must match.")
                        .info("&7Current Value", "")
                        .info(null, matchAnyCondition ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("ifActions",
                        ItemBuilder.create(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                                .name("&aIf Actions")
                                .description("Actions to execute if the conditions &nare&r&7 met.")
                                .info("&7Current Value", "")
                                .info(null, (ifActions.isEmpty() ? "&cNo Actions" : "&a" + ifActions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ACTION
                ),
                new ActionEditor.ActionItem("elseActions",
                        ItemBuilder.create(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
                                .name("&aElse Actions")
                                .description("Actions to execute if the conditions are &nnot&r&7 met.")
                                .info("&7Current Value", "")
                                .info(null, (elseActions.isEmpty() ? "&cNo Actions" : "&a" + elseActions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ACTION
                )

        );
        return new ActionEditor(4, "&eConditional Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false; //does nothing
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        boolean result = false;
        if (conditions.isEmpty()) {
            result = true;
        } else {
            for (Condition condition : conditions) {
                if (condition.execute(player, house)) {
                    result = true;
                    if (matchAnyCondition) break;
                } else {
                    if (!matchAnyCondition) {
                        result = false;
                        break;
                    }
                }
            }
        }

        ActionExecutor executor = new ActionExecutor();
        executor.addActions((result ? ifActions : elseActions));
        return executor.execute(player, house, event);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Action> getIfActions() {
        return ifActions;
    }

    public void setIfActions(List<Action> ifActions) {
        this.ifActions = ifActions;
    }

    public List<Action> getElseActions() {
        return elseActions;
    }

    public void setElseActions(List<Action> elseActions) {
        this.elseActions = elseActions;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("conditions", ConditionData.Companion.fromList(conditions));
        data.put("matchAnyCondition", matchAnyCondition);
        data.put("ifActions", Companion.fromList(ifActions));
        data.put("elseActions", Companion.fromList(elseActions));
        return data;
    }

    @Override
    public HashMap<String, List<Action>> getActions() {
        HashMap<String, List<Action>> actions = new HashMap<>();
        actions.put("ifActions", ifActions);
        actions.put("elseActions", elseActions);
        return actions;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (data.containsKey("conditions")) {
            //Imma just assume this will work
            conditions = conditionDataToList(gson.toJsonTree(data.get("conditions")).getAsJsonArray());
        }
        if (data.containsKey("matchAnyCondition")) {
            matchAnyCondition = (boolean) data.get("matchAnyCondition");
        }
        if (data.containsKey("ifActions")) {
            ifActions = dataToList(gson.toJsonTree(data.get("ifActions")).getAsJsonArray());
        }
        if (data.containsKey("elseActions")) {
            elseActions = dataToList(gson.toJsonTree(data.get("elseActions")).getAsJsonArray());
        }
    }

    private List<Condition> conditionDataToList(JsonArray jsonArray) {
        ArrayList<ConditionData> conditions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            conditions.add(gson.fromJson(jsonObject, ConditionData.class));
        }
        return ConditionData.Companion.toList(conditions);
    }

    private List<Action> dataToList(JsonArray jsonArray) {
        ArrayList<ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            actions.add(gson.fromJson(jsonObject, ActionData.class));
        }
        return Companion.toList(actions);
    }
}
