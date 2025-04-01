package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.ConditionalData;
import com.al3x.housing2.Enums.AttackEntityEnum;
import com.al3x.housing2.Enums.EditVisibilityEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.al3x.housing2.Utils.VoiceChat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
public class EditAudibilityAction extends HTSLImpl {
    private static final Gson gson = new Gson();
    @Setter
    @Getter
    private EditVisibilityEnum mode = EditVisibilityEnum.NEAREST;
    private String range = "10";
    private List<Condition> conditions = new ArrayList<>();
    private String limit = "1";
    private boolean value = false;


    public EditAudibilityAction() {
        super(
                "edit_audibility_action",
                "Edit Audibility",
                "Edit the audibility of players.",
                Material.SCULK_SENSOR,
                List.of("audibility")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "mode",
                        "Mode",
                        "The mode to use.",
                        ActionProperty.PropertyType.ENUM,
                        EditVisibilityEnum.class
                ),
                new ActionProperty(
                        "range",
                        "Range",
                        "The range to use.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "value",
                        "Value",
                        "The value to use.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "limit",
                        "Limit",
                        "The limit to use.",
                        ActionProperty.PropertyType.STRING
                )
        ));

        if (mode == EditVisibilityEnum.CONDITION) {
            getProperties().add(new ActionProperty(
                    "conditions",
                    "Conditions",
                    "The conditions to use.",
                    ActionProperty.PropertyType.CONDITION
            ));
        }
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String limit = HandlePlaceholders.parsePlaceholders(player, house, this.limit);
        if (!NumberUtilsKt.isDouble(limit) || !NumberUtilsKt.isDouble(range)) {
            return OutputType.ERROR;
        }
        double rangeValue = Double.parseDouble(range);
        double limitValue = Double.parseDouble(limit);
        List<Player> players = new ArrayList<>(player.getWorld().getPlayers());
        Main main = house.getPlugin();
        switch (mode) {
            case NEAREST:
                players.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(player.getLocation())));
                break;
            case ALL:
                break;
            case CONDITION:
                int count = 0;
                for (Player onlinePlayer : players) {
                    if (conditions.stream().allMatch(condition -> condition.execute(onlinePlayer, house, null, executor))) {
                        VoiceChat.editAudibility(player, onlinePlayer, value);
                        count++;
                        if (count > limitValue) return OutputType.SUCCESS;
                    }
                }
                return OutputType.SUCCESS;
        }

        int count = 0;
        for (Player onlinePlayer : players) {
            if (onlinePlayer.getLocation().distance(player.getLocation()) > rangeValue) continue;

            VoiceChat.editAudibility(player, onlinePlayer, value);

            count++;
            if (count > limitValue) return OutputType.SUCCESS;
        }

        return OutputType.SUCCESS;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = EditVisibilityEnum.valueOf((String) data.get("mode"));
        range = (String) data.get("range");
        limit = (String) data.get("limit");
        value = (boolean) data.get("value");
        if (!data.containsKey("conditions")) return;
        Object subActions = data.get("conditions");
        JsonArray jsonArray = gson.toJsonTree(subActions).getAsJsonArray();
        ArrayList<ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            ActionData action = gson.fromJson(jsonObject, ActionData.class);
            actions.add(action);
        }
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
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + mode + " " + range + " " + value + " " + limit + "(" + conditionString + ")";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        LinkedHashMap<String, Object> actionData = data();

        if (AttackEntityEnum.fromString(parts[0]) != null) {
            actionData.put("mode", AttackEntityEnum.fromString(parts[0]));
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts.length > 0) {
            actionData.put("range", parts[0]);
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts.length > 0) {
            actionData.put("value", parts[0]);
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts.length > 0) {
            actionData.put("limit", parts[0]);
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }

        List<Condition> conditions = new ArrayList<>();
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

        actionData.put("conditions", ConditionalData.fromList(conditions));

        this.conditions = conditions;
        return nextLines;
    }
}