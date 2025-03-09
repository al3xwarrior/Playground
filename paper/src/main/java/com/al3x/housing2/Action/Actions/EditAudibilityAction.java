package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.AttackEntityEnum;
import com.al3x.housing2.Enums.EditVisibilityEnum;
import com.al3x.housing2.Instances.HousingData.ConditionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.*;

public class EditAudibilityAction extends HTSLImpl {
    private EditVisibilityEnum mode;
    private String range;
    private List<Condition> conditions;
    private String limit;
    private boolean value;
    private static Gson gson = new Gson();


    public EditAudibilityAction() {
        super("Edit Audibility Action");
        mode = EditVisibilityEnum.NEAREST;
        range = "10";
        conditions = new ArrayList<>();
        limit = "1";
        value = false;
    }

    @Override
    public String toString() {
        return "EditAudibilityAction (mode: " + mode + ", range: " + range + ", value: " + value + ", limit: " + limit + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.SCULK_SENSOR);
        builder.name("&eEdit Audibility");
        builder.info("&eSettings", "");
        builder.info("Mode", mode.name());
        builder.info("Range", range);
        builder.info("Limit", limit);
        builder.info("Value", String.valueOf(value));
        builder.info("Conditions", conditions.size());

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.SCULK_SENSOR);
        builder.name("&eEdit Audibility");
        builder.description("Change the Audibility of players.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eEdit Audibility Action Settings", new ArrayList<>());
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("mode",
                ItemBuilder.create(Material.IRON_SWORD)
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + mode.name())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, EditVisibilityEnum.values(), null));
        items.add(new ActionEditor.ActionItem("range",
                ItemBuilder.create(Material.SNOWBALL)
                        .name("&eRange")
                        .info("&7Current Value", "")
                        .info(null, "&a" + range)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING));
        items.add(new ActionEditor.ActionItem("value",
                ItemBuilder.create((value ? Material.LIME_DYE : Material.GRAY_DYE))
                        .name("&eMake player audible")
                        .info("&7Current Value", "")
                        .info(null, "&a" + value)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.BOOLEAN));
        items.add(new ActionEditor.ActionItem("limit",
                ItemBuilder.create(Material.COMMAND_BLOCK)
                        .name("&eLimit")
                        .info("&7Current Value", "")
                        .info(null, "&a" + limit)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING));
        if (mode == EditVisibilityEnum.CONDITION) {
            items.add(new ActionEditor.ActionItem("conditions",
                    ItemBuilder.create(Material.REDSTONE)
                            .name("&eConditions")
                            .info("&7Current Value", "")
                            .info(null, (conditions.isEmpty() ? "&cNo Conditions" : "&a" + conditions.size() + " Conditions"))
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.CONDITION));
        }
        return new ActionEditor(6, "&eEdit Audibility Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false; // Not used
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event, ActionExecutor executor) {
        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String limit = HandlePlaceholders.parsePlaceholders(player, house, this.limit);
        if (!NumberUtilsKt.isDouble(limit) || !NumberUtilsKt.isDouble(range)) {
            return true;
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
                        if (count > limitValue) return true;
                    }
                }
                return true;
        }

        int count = 0;
        for (Player onlinePlayer : players) {
            if (onlinePlayer.getLocation().distance(player.getLocation()) > rangeValue) continue;

            VoiceChat.editAudibility(player, onlinePlayer, value);

            count++;
            if (count > limitValue) return true;
        }

        return true;
    }

    public EditVisibilityEnum getMode() {
        return mode;
    }

    public void setMode(EditVisibilityEnum mode) {
        this.mode = mode;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("mode", mode.name());
        data.put("range", range);
        data.put("value", value);
        data.put("limit", limit);
        data.put("conditions", ConditionData.Companion.fromList(conditions));
        return data;
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
        ArrayList<com.al3x.housing2.Instances.HousingData.ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            com.al3x.housing2.Instances.HousingData.ActionData action = gson.fromJson(jsonObject, com.al3x.housing2.Instances.HousingData.ActionData.class);
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
        return " ".repeat(indent) + keyword() + " " + mode + " " + range + " " + value + " " + limit + "(" + conditionString + ")";
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

        actionData.put("conditions", ConditionData.Companion.fromList(conditions));

        this.conditions = conditions;
        return nextLines;
    }

    @Override
    public String keyword() {
        return "editAudibility";
    }
}