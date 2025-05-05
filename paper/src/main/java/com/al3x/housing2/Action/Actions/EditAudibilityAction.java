package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.ConditionsProperty;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EditVisibilityEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.VoiceChat;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
public class EditAudibilityAction extends HTSLImpl {
    public EditAudibilityAction() {
        super(
                ActionEnum.EDIT_AUDIBILITY,
                "Edit Audibility",
                "Edit the audibility of players.",
                Material.SCULK_SENSOR,
                List.of("audibility")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "mode",
                        "Mode",
                        "The mode to use.",
                        EditVisibilityEnum.class
                ).setValue(EditVisibilityEnum.NEAREST),
                new NumberProperty(
                        "range",
                        "Range",
                        "The range to use."
                ).setValue("10"),
                new BooleanProperty(
                        "value",
                        "Value",
                        "If the player can be heard or not."
                ).setValue(false),
                new NumberProperty(
                        "limit",
                        "Limit",
                        "The amount of players to be edited."
                ).setValue("1"),
                new ConditionsProperty(
                        "conditions",
                        "Conditions",
                        "The conditions to use."
                ).setValue(new ArrayList<>()).showIf(() -> getValue("mode", EditVisibilityEnum.class) == EditVisibilityEnum.CONDITION)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Double range = getProperty("range", NumberProperty.class).parsedValue(house, player);
        Double limit = getProperty("limit", NumberProperty.class).parsedValue(house, player);
        List<Player> players = new ArrayList<>(player.getWorld().getPlayers());
        Main main = house.getPlugin();
        List<Condition> conditions = getProperty("conditions", ConditionsProperty.class).getValue();
        boolean value = getValue("value", Boolean.class);
        switch (getValue("mode", EditVisibilityEnum.class)) {
            case NEAREST:
                players.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(player.getLocation())));
                break;
            case ALL:
                break;
            case CONDITION:
                int count = 0;
                for (Player onlinePlayer : players) {
                    if (conditions.stream().allMatch(condition -> condition.execute(onlinePlayer, house, null, executor) == OutputType.TRUE)) {
                        VoiceChat.editAudibility(player, onlinePlayer, value);
                        count++;
                        if (count > limit) return OutputType.SUCCESS;
                    }
                }
                return OutputType.SUCCESS;
        }

        int count = 0;
        for (Player onlinePlayer : players) {
            if (onlinePlayer.getLocation().distance(player.getLocation()) > range) continue;

            VoiceChat.editAudibility(player, onlinePlayer, value);

            count++;
            if (count > limit) return OutputType.SUCCESS;
        }

        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
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
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + mode + " " + range + " " + value + " " + limit + "(" + conditionString + ")";
//    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        String[] parts = action.split(" ");
//        LinkedHashMap<String, Object> actionData = data();
//
//        if (AttackEntityEnum.fromString(parts[0]) != null) {
//            actionData.put("mode", AttackEntityEnum.fromString(parts[0]));
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts.length > 0) {
//            actionData.put("range", parts[0]);
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts.length > 0) {
//            actionData.put("value", parts[0]);
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts.length > 0) {
//            actionData.put("limit", parts[0]);
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//
//        List<Condition> conditions = new ArrayList<>();
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
//        this.conditions = conditions;
//        return nextLines;
//    }
}