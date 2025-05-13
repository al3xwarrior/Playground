package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ActionsProperty;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.ConditionsProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.ConditionalData;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
public class ConditionalAction extends HTSLImpl implements NPCAction {

    public ConditionalAction() {
        super(
                ActionEnum.CONDITIONAL,
                "Conditional",
                "Executes actions based on certain conditions.",
                Material.REDSTONE,
                List.of("if")
        );

        getProperties().addAll(List.of(
                new ConditionsProperty(
                        "conditions",
                        "Conditions",
                        "The conditions to check."
                ).setValue(new ArrayList<>()),
                new BooleanProperty(
                        "matchAnyCondition",
                        "Match Any Condition",
                        "When enabled only one condition needs to match, otherwise all conditions must match."
                ).setValue(false),
                new BooleanProperty(
                        "not",
                        "Not",
                        "When enabled the conditions will be inverted."
                ).setValue(false),
                new ActionsProperty(
                        "ifActions",
                        "If Actions",
                        "The actions to execute if the conditions are met."
                ).setValue(new ArrayList<>()),
                new ActionsProperty(
                        "elseActions",
                        "Else Actions",
                        "The actions to execute if the conditions are not met."
                ).setValue(new ArrayList<>())
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //does nothing
    }

    public OutputType execute(Entity entity, Player player, HousingWorld house, CancellableEvent event, ActionExecutor oldExecutor) {
        List<Condition> conditions = getProperty("conditions", ConditionsProperty.class).getValue();
        List<Action> ifActions = getProperty("ifActions", ActionsProperty.class).getValue();
        List<Action> elseActions = getProperty("elseActions", ActionsProperty.class).getValue();
        boolean matchAnyCondition = getProperty("matchAnyCondition", BooleanProperty.class).getValue();
        boolean not = getProperty("not", BooleanProperty.class).getValue();

        boolean result = conditions.isEmpty();

        for (Condition condition : conditions) {
            if (condition.requiresPlayer() && !(entity instanceof Player)) {
                result = false;
                continue;
            }

            boolean inverted = condition.getValue("inverted", Boolean.class);

            boolean conditionResult = (entity == player)
                ? (condition.execute(player, house, event, oldExecutor) == OutputType.TRUE) != not
                : (condition instanceof NPCCondition npcCondition) &&
                  npcCondition.npcExecute(player, CitizensAPI.getNPCRegistry().getNPC(entity), house, event, oldExecutor) != not;

            conditionResult = (inverted != conditionResult);
            if (conditionResult) {
                result = true;
                if (matchAnyCondition) break;
            } else {
                if (!matchAnyCondition) {
                    result = false;
                    break;
                }
            }
        }

        ActionExecutor executor = new ActionExecutor("conditional");
        executor.addActions(result ? ifActions : elseActions);
        executor.setLimits(oldExecutor.getLimits());
        executor.onBreak(oldExecutor.getOnBreak());
        executor.onComplete((ActionExecutor e) -> oldExecutor.execute(player, house, event));
        executor.execute(player, house, event);
        return OutputType.RUNNING;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor oldExecutor) {
        return execute(player, player, house, event, oldExecutor);
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        execute(npc.getEntity(), player, house, event, executor);
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
    public String syntax() {
        return getScriptingKeywords().getFirst() + " (<conditions>) {\\n<ifActions>\\n} else {\\n<elseActions>\\n}";
    }
//
//    @Override
//    public String export(int indent) {
//        StringBuilder builder = new StringBuilder();
//        for (Condition condition : conditions) {
//            if (condition instanceof CHTSLImpl c) {
//                builder.append(c.export()).append(", ");
//            }
//        }
//        String conditionString = builder.toString();
//        if (!conditionString.isEmpty()) {
//            conditionString = conditionString.substring(0, conditionString.length() - 2);
//        }
//
//        String ifString = "";
//        for (Action action : ifActions) {
//            if (action instanceof HTSLImpl impl) {
//                ifString += impl.export(indent + 4) + "\n";
//            }
//        }
//
//        StringBuilder elseString = new StringBuilder();
//        for (Action action : elseActions) {
//            if (action instanceof HTSLImpl impl) {
//                elseString.append(impl.export(indent + 4)).append("\n");
//            }
//        }
//
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " (" + conditionString + ") {\n" + ifString + " ".repeat(indent) + "} else {\n" + elseString + " ".repeat(indent) + "}";
//    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        if (indent.length() > 4*5) {
//            throw new IllegalArgumentException("Nesting limit reached"); //TODO: change this to a proper exception
//        }
//
//        String[] parts = action.split(" ");
//        LinkedHashMap<String, Object> actionData = data();
//
//        if ((parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("false")) || parts[0].equalsIgnoreCase("and") || parts[0].equalsIgnoreCase("or")) {
//            actionData.put("matchAnyCondition", parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("or"));
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts[0].equalsIgnoreCase("true") || parts[0].equalsIgnoreCase("false")) {
//            actionData.put("not", parts[0].equalsIgnoreCase("true"));
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//
//        List<Condition> conditions = new ArrayList<>();
//        List<Action> ifActions = new ArrayList<>();
//        List<Action> elseActions = new ArrayList<>();
//
//        String conditionString = String.join(" ", parts);
//
//        if (!(conditionString.trim().startsWith("(") && conditionString.trim().contains(")") && conditionString.trim().endsWith("{"))) {
//            throw new IllegalArgumentException("Invalid conditional action"); //TODO: change this to a proper exception
//        }
//        conditionString = conditionString.trim().substring(1, conditionString.trim().length() - 1).replace(")", "").trim();
//        String[] conditionParts = conditionString.split(",");
//
//        List<CHTSLImpl> defaultConditions = List.of(Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(a -> a instanceof CHTSLImpl).map(a -> (CHTSLImpl) a).toArray(CHTSLImpl[]::new));
//
//        for (String conditionPart : conditionParts) {
//            for (CHTSLImpl condition : defaultConditions) {
//                if (conditionPart.startsWith(condition.keyword())) {
//                    CHTSLImpl c = (CHTSLImpl) condition.clone();
//                    c.importCondition(StringUtilsKt.substringAfter(conditionPart, c.keyword() + " "), nextLines);
//                    conditions.add(c);
//                    break;
//                }
//            }
//        }
//
//        actionData.put("conditions", ConditionalData.fromList(conditions));
//
//        ArrayList<String> ifLines = new ArrayList<>();
//        ArrayList<String> elseLines = new ArrayList<>();
//        boolean isElse = false;
//        int ifStart = -1;
//        for (int i = 0; i < nextLines.size(); i++) {
//            String line = nextLines.get(i);
//            if (line.startsWith(indent + "} else {")) {
//                ifLines = new ArrayList<>(nextLines.subList(0, i));
//                isElse = true;
//                ifStart = i + 1;
//                continue;
//            }
//
//            if (line.startsWith(indent + "}")) {
//                if (isElse) {
//                    elseLines = new ArrayList<>(nextLines.subList(ifStart, i));
//                } else {
//                    ifLines = new ArrayList<>(nextLines.subList(0, i));
//                }
//
//                nextLines = new ArrayList<>(nextLines.subList(i + 1, nextLines.size()));
//                break;
//            }
//        }
//
//        ifActions = HTSLHandler.importActions(String.join("\n", ifLines), indent + " ".repeat(4));
//        if (!elseLines.isEmpty()) {
//            elseActions = HTSLHandler.importActions(String.join("\n", elseLines), indent + " ".repeat(4));
//        }
//
//        actionData.put("ifActions", ActionData.fromList(ifActions));
//        actionData.put("elseActions", ActionData.fromList(elseActions));
//
//        fromData(actionData, ConditionalAction.class);
//
//        return nextLines;
//    }
}
