package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingData.ActionData;
import com.al3x.housing2.Instances.HousingData.ConditionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;

public class ConditionalAction extends HTSLImpl implements NPCAction {
    private static final Gson gson = new Gson();
    private List<Condition> conditions;
    private boolean matchAnyCondition;
    private boolean not;
    private List<Action> ifActions;
    private List<Action> elseActions;

    public ConditionalAction(ArrayList<Condition> conditions, boolean matchAnyCondition, ArrayList<Action> ifActions, ArrayList<Action> elseActions) {
        super("Conditional Action");
        this.not = false;
        this.conditions = conditions;
        this.matchAnyCondition = matchAnyCondition;
        this.ifActions = ifActions;
        this.elseActions = elseActions;
    }

    public ConditionalAction() {
        super("Conditional Action");
        this.conditions = new ArrayList<>();
        this.not = false;
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

    public List<ActionEditor.ActionItem> getItems(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("conditions",
                        ItemBuilder.create(Material.COMPARATOR)
                                .name("&aConditions")
                                .info("&7Current Value", "")
                                .info(null, (conditions.isEmpty() ? "&cNo Actions" : "&a" + conditions.size() + " Action"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.CONDITION
                ),
                new ActionEditor.ActionItem("not", ItemBuilder.create((not ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aNot")
                        .description("When enabled the conditions will be inverted.")
                        .info("&7Current Value", "")
                        .info(null, not ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
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

        return items;
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        return new ActionEditor(4, "&eConditional Settings", getItems(house));
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false; //does nothing
    }

    public boolean execute(Entity entity, Player player, HousingWorld house, Cancellable event, ActionExecutor oldExecutor) {
        boolean result = false;
        if (conditions.isEmpty()) {
            result = true;
        } else {
            for (Condition condition : conditions) {
                if (condition.requiresPlayer() && !(entity instanceof Player)) {
                    result = false;
                    continue;
                }
                if (entity == player) {
                    if (condition.execute(player, house, event, oldExecutor) != not) {
                        result = true;
                        if (matchAnyCondition) break;
                    } else {
                        if (!matchAnyCondition) {
                            result = false;
                            break;
                        }
                    }
                } else if (condition instanceof NPCCondition npcCondition) {
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                    if (npcCondition.npcExecute(player, npc, house, event, oldExecutor) != not) {
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
        }

        ActionExecutor executor = new ActionExecutor("conditional");
        executor.addActions(result ? ifActions : elseActions);
        executor.setParent(oldExecutor);
        return executor.execute(entity, player, house, event) == OutputType.SUCCESS;
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event, ActionExecutor oldExecutor) {
        return execute(player, player, house, event, oldExecutor);
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        execute(npc.getEntity(), player, house, event, executor);
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("conditions", ConditionData.Companion.fromList(conditions));
        data.put("matchAnyCondition", matchAnyCondition);
        data.put("not", not);
        data.put("ifActions", Companion.fromList(ifActions));
        data.put("elseActions", Companion.fromList(elseActions));
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int nestLimit() {
        return 3;
    }

    @Override
    public int limit() {
        return 40;
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
        if (data.containsKey("not")) {
            not = (boolean) data.get("not");
        } else {
            not = false;
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

    @Override
    public String keyword() {
        return "if";
    }

    @Override
    public String syntax() {
        return "if (<conditions>) {\\n<ifActions>\\n} else {\\n<elseActions>\\n}";
    }

    @Override
    public String export(int indent) {
        StringBuilder builder = new StringBuilder();
        for (Condition condition : conditions) {
            if (condition instanceof CHTSLImpl c) {
                builder.append(c.export()).append(", ");
            }
        }
        String conditionString = builder.toString();
        if (!conditionString.isEmpty()) {
            conditionString = conditionString.substring(0, conditionString.length() - 2);
        }

        String ifString = "";
        for (Action action : ifActions) {
            if (action instanceof HTSLImpl impl) {
                ifString += impl.export(indent + 4) + "\n";
            }
        }

        String elseString = "";
        for (Action action : elseActions) {
            if (action instanceof HTSLImpl impl) {
                elseString += impl.export(indent + 4) + "\n";
            }
        }

        return " ".repeat(indent) + keyword() + " (" + conditionString + ") {\n" + ifString + " ".repeat(indent) + "} else {\n" + elseString + " ".repeat(indent) + "}";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        if (indent.length() > 4*5) {
            throw new IllegalArgumentException("Nesting limit reached"); //TODO: change this to a proper exception
        }

        String[] parts = action.split(" ");
        LinkedHashMap<String, Object> actionData = data();

        if ((parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("false")) || parts[0].equalsIgnoreCase("and") || parts[0].equalsIgnoreCase("or")) {
            actionData.put("matchAnyCondition", parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("or"));
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("false")) {
            actionData.put("not", parts[0].equalsIgnoreCase("true"));
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }

        List<Condition> conditions = new ArrayList<>();
        List<Action> ifActions = new ArrayList<>();
        List<Action> elseActions = new ArrayList<>();

        String conditionString = String.join(" ", parts);

        if (!(conditionString.trim().startsWith("(") && conditionString.trim().contains(")") && conditionString.trim().endsWith("{"))) {
            throw new IllegalArgumentException("Invalid conditional action"); //TODO: change this to a proper exception
        }
        conditionString = conditionString.trim().substring(1, conditionString.trim().length() - 1).replace(")", "").trim();
        String[] conditionParts = conditionString.split(",");

        List<CHTSLImpl> defaultConditions = List.of(Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(a -> a instanceof CHTSLImpl).map(a -> (CHTSLImpl) a).toArray(CHTSLImpl[]::new));

        for (String conditionPart : conditionParts) {
            for (CHTSLImpl condition : defaultConditions) {
                if (conditionPart.startsWith(condition.keyword())) {
                    CHTSLImpl c = (CHTSLImpl) condition.clone();
                    c.importCondition(StringUtilsKt.substringAfter(conditionPart, c.keyword() + " "), nextLines);
                    conditions.add(c);
                    break;
                }
            }
        }

        actionData.put("conditions", ConditionData.Companion.fromList(conditions));

        ArrayList<String> ifLines = new ArrayList<>();
        ArrayList<String> elseLines = new ArrayList<>();
        boolean isElse = false;
        int ifStart = -1;
        for (int i = 0; i < nextLines.size(); i++) {
            String line = nextLines.get(i);
            if (line.startsWith(indent + "} else {")) {
                ifLines = new ArrayList<>(nextLines.subList(0, i));
                isElse = true;
                ifStart = i + 1;
                continue;
            }

            if (line.startsWith(indent + "}")) {
                if (isElse) {
                    elseLines = new ArrayList<>(nextLines.subList(ifStart, i));
                } else {
                    ifLines = new ArrayList<>(nextLines.subList(0, i));
                }

                nextLines = new ArrayList<>(nextLines.subList(i + 1, nextLines.size()));
                break;
            }
        }

        ifActions = HTSLHandler.importActions(String.join("\n", ifLines), indent + " ".repeat(4));
        if (!elseLines.isEmpty()) {
            elseActions = HTSLHandler.importActions(String.join("\n", elseLines), indent + " ".repeat(4));
        }

        actionData.put("ifActions", Companion.fromList(ifActions));
        actionData.put("elseActions", Companion.fromList(elseActions));

        fromData(actionData, ConditionalAction.class);

        return nextLines;
    }
}
