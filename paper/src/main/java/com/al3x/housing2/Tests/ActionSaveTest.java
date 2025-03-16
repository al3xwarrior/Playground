package com.al3x.housing2.Tests;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.Actions.ConditionalAction;
import com.al3x.housing2.Action.Actions.FunctionAction;
import com.al3x.housing2.Action.Actions.RandomAction;
import com.al3x.housing2.Action.Actions.RepeatAction;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActionSaveTest extends Test {
    @Override
    public void execute(Player player, int index, int size, List<Boolean> results) {
        player.sendMessage("§aRunning test \"ActionSaveTest\" (" + index + "/" + size + ")");
        try {
            HousingWorld world = new HousingWorld(Main.getInstance(), player, HouseSize.XLARGE);
            Function func = world.createFunction("test");
            world.createNPC(player, world.getSpawn());


            ActionEnum[] actions = ActionEnum.values();
            ConditionEnum[] conditions = ConditionEnum.values();

            // Create a list of all actions and conditions aswell as have each action that can have subactions have all actions as subactions
            ArrayList<Action> actionList = Arrays.stream(actions).map(ActionEnum::getActionInstance).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Condition> conditionList = Arrays.stream(conditions).map(ConditionEnum::getConditionInstance).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Action> usedActions = new ArrayList<>(actionList);
            for (Action action : usedActions) {
                if (action instanceof ConditionalAction conditionalAction) {
                    conditionalAction.setIfActions(new ArrayList<>(actionList));
                    conditionalAction.setElseActions(new ArrayList<>(actionList));
//                    conditionalAction.setConditions(new ArrayList<>(conditionList));
                }
                if (action instanceof RepeatAction repeatAction) {
                    repeatAction.setSubActions(new ArrayList<>(actionList));
                }
                if (action instanceof FunctionAction functionAction) {
                    functionAction.setFunction("test");
                }
                if (action instanceof RandomAction randomAction) {
                    randomAction.setSubActions(new ArrayList<>(actionList));
                }
            }

            func.setActions(new ArrayList<>(usedActions));
            for (EventType eventType : EventType.values()) {
                if (!eventType.name().startsWith("NPC")) world.setEventActions(eventType, new ArrayList<>(usedActions));
            }

            world.createNPC(player, player.getLocation());
            HousingNPC npc = world.getNPCs().getFirst();
            npc.getActions().addAll(usedActions);
            for (EventType eventType : EventType.values()) {
                if (eventType.name().startsWith("NPC")) npc.getActions(eventType).addAll(usedActions);
            }

            world.save();

            world.delete();

            results.add(true);
            player.sendMessage("§aTest passed successfully");
        } catch (Exception e) {
            results.add(false);
            player.sendMessage("§cTest failed: " + e.getMessage());
        }
    }
}
