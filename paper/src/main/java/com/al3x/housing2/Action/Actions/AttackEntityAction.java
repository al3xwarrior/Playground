package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.AttackEntityEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.ConditionalData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class AttackEntityAction extends HTSLImpl implements NPCAction {
    private AttackEntityEnum mode;
    private String range;
    private List<Condition> conditions;
    private String value;
    private static Gson gson = new Gson();


    public AttackEntityAction() {
        super("Attack Entity Action");
        mode = AttackEntityEnum.NEAREST;
        range = "10";
        conditions = new ArrayList<>();
        value = "2";
    }

    @Override
    public String toString() {
        return "AttackEntityAction (mode: " + mode + ", range: " + range + ", value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_SWORD);
        builder.name("&eAttack Entity");
        builder.info("&eSettings", "");
        builder.info("Mode", mode.name());
        builder.info("Range", range);
        builder.info("Value", value);
        builder.info("Conditions", conditions.size());

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_SWORD);
        builder.name("&eAttack Entity");
        builder.description("Attack the nearest entity within the specified range.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eAttack Entity Action Settings", new ArrayList<>());
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("mode",
                ItemBuilder.create(Material.IRON_SWORD)
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + mode.name())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, AttackEntityEnum.values(), null));
        items.add(new ActionEditor.ActionItem("range",
                ItemBuilder.create(Material.SNOWBALL)
                        .name("&eRange")
                        .info("&7Current Value", "")
                        .info(null, "&a" + range)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING));
        items.add(new ActionEditor.ActionItem("value",
                ItemBuilder.create(Material.NETHERITE_SWORD)
                        .name("&eDamage")
                        .info("&7Current Value", "")
                        .info(null, "&a" + value)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING));
        if (mode == AttackEntityEnum.CONDITION) {
            items.add(new ActionEditor.ActionItem("conditions",
                    ItemBuilder.create(Material.REDSTONE)
                            .name("&eConditions")
                            .info("&7Current Value", "")
                            .info(null, (conditions.isEmpty() ? "&cNo Conditions" : "&a" + conditions.size() + " Conditions"))
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.CONDITION));
        }
        return new ActionEditor(6, "&eAttack Entity Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.ERROR; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value) || !NumberUtilsKt.isDouble(range)) {
            return OutputType.ERROR;
        }
        double rangeValue = Double.parseDouble(range);
        double damageValue = Double.parseDouble(value);
        List<Entity> entities = new ArrayList<>(player.getWorld().getEntities());
        switch (mode) {
            case NEAREST:
                entities.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(player.getLocation())));
                break;
            case PLAYER:
                entities.removeIf(entity -> !(entity instanceof Player) || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case NPC:
                entities.removeIf(entity -> !CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case MOB:
                entities.removeIf(entity -> entity instanceof Player || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case ALL:
                break;
            case CONDITION:
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (conditions.stream().allMatch(condition -> condition.execute(target, house, null, executor))) {
                            target.damage(damageValue);
                        }
                    } else if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        NPC target = CitizensAPI.getNPCRegistry().getNPC(entity);
                        if (conditions.stream().filter(condition -> condition instanceof NPCCondition).allMatch(condition -> ((NPCCondition) condition).npcExecute(player, target, house, null, executor))) {
                            if (entity instanceof LivingEntity le) {
                                le.damage(damageValue);
                            }
                        }
                    }
                }
                return OutputType.SUCCESS;
        }

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.damage(damageValue);
            }
        }

        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {

        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value) || !NumberUtilsKt.isDouble(range)) {
            return;
        }
        double rangeValue = Double.parseDouble(range);
        double damageValue = Double.parseDouble(value);
        List<Entity> entities = new ArrayList<>(npc.getEntity().getNearbyEntities(rangeValue, rangeValue, rangeValue));
        switch (mode) {
            case NEAREST:
                entities.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(npc.getEntity().getLocation())));

                if (entities.isEmpty()) {
                    return;
                }

                Entity e = entities.getFirst();
                if (e instanceof LivingEntity le) {
                    le.damage(damageValue);
                }

                return;
            case PLAYER:
                entities.removeIf(entity -> !(entity instanceof Player) || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case NPC:
                entities.removeIf(entity -> !CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case MOB:
                entities.removeIf(entity -> entity instanceof Player || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case ALL:
                break;
            case CONDITION:
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (conditions.stream().allMatch(condition -> condition.execute(target, house, null, executor))) {
                            target.damage(damageValue);
                        }
                    } else if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        NPC target = CitizensAPI.getNPCRegistry().getNPC(entity);
                        if (conditions.stream().filter(condition -> condition instanceof NPCCondition).allMatch(condition -> ((NPCCondition) condition).npcExecute(player, target, house, null, executor))) {
                            if (entity instanceof LivingEntity le) {
                                le.damage(damageValue);
                            }
                        }
                    }
                }
                return;
        }

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.damage(damageValue);
            }
        }
    }

    public AttackEntityEnum getMode() {
        return mode;
    }

    public void setMode(AttackEntityEnum mode) {
        this.mode = mode;
    }

    public String getValue() {
        return value;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("mode", mode.name());
        data.put("range", range);
        data.put("value", value);
        data.put("conditions", ConditionalData.fromList(conditions));
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = AttackEntityEnum.valueOf((String) data.get("mode"));
        range = (String) data.get("range");
        value = (String) data.get("value");
        if (!data.containsKey("conditions")) return;
        // I don't know how this works lol
        Object subActions = data.get("conditions");
        JsonArray jsonArray = gson.toJsonTree(subActions).getAsJsonArray();
        ArrayList<ConditionalData> conditionalData = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            ConditionalData condition = gson.fromJson(jsonObject, ConditionalData.class);
            conditionalData.add(condition);
        }
        conditions = ConditionalData.toList(conditionalData);
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
        return " ".repeat(indent) + keyword() + " " + mode + " " + range + " " + value + "(" + conditionString + ")";
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

    @Override
    public String keyword() {
        return "attackEntity";
    }
}
