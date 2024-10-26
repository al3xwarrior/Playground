package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ActionEnum {
    CONDITIONAL("Conditional Action", ConditionalAction.class),
    CANCEL("Cancel Action", CancelAction.class),
    ACTIONBAR("Actionbar Action", ActionbarAction.class),
    CHAT("Chat Action", ChatAction.class),
    FULL_HEAL("Full Heal Action", FullHealAction.class),
    KILL_PLAYER("Kill Player Action", KillPlayerAction.class),
    PLAYER_STAT("Player Stat Action", PlayerStatAction.class),
    GLOBAL_STAT("Global Stat Action", GlobalStatAction.class),
    PLAY_SOUND("Play Sound Action", PlaySoundAction.class),
    PUSH_PLAYER("Push Player Action", PushPlayerAction.class),
    RANDOM_ACTION("Random Action", RandomAction.class),
    RESET_INVENTORY("Reset Inventory Action", ResetInventoryAction.class),
    SEND_TITLE("Send Title Action", SendTitleAction.class),
    SHOW_BOSSBAR("Show Bossbar Action", ShowBossbarAction.class),
    PAUSE_EXECUTION("Pause Action", PauseAction.class),
    FUNCTION("Function Action", FunctionAction.class),
    CHANGE_MAX_HEALTH("Change Max Health Action", ChangeMaxHealthAction.class),
    SET_HITDELAY("Set Hit Delay Action", SetHitDelayAction.class),
    CLEAR_PLAYERSTATS("Clear Player Stats Action", ClearPlayerStatsAction.class),
    CLEAR_GLOBALSTATS("Clear Global Stats Action", ClearGlobalStatsAction.class),
    CLEAR_ENDERCHEST("Clear Enderchest Action", ClearEnderChestAction.class),
    //Name of the action and the class that has the name of the action need to be the exact same
    APPLY_POTION("Apply Potion Effect Action", ApplyPotionEffectAction.class),
    CLEAR_POTION("Clear Potion Effect Action", ClearPotionEffectAction.class),
    LAUNCH_PROJECTILE("Launch Projectile Action", LaunchProjectileAction.class),
    EXIT("Exit Action", ExitAction.class),
    // Add new actions here
    ;
    private String name;
    private Class<? extends Action> action;

    ActionEnum(String name, Class<? extends Action> action) {
        this.action = action;
        this.name = name;
    }

    public Class<? extends Action> getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public Action getActionInstance(HashMap<String, Object> data) {
        try {
            Action action = this.action.getDeclaredConstructor().newInstance();
            action.fromData(data, this.action);
            return action;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Action getActionInstance() {
        try {
            return this.action.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActionEnum getActionByName(String name) {
        for (ActionEnum action : ActionEnum.values()) {
            if (action.name.equals(name)) {
                return action;
            }
        }
        return null;
    }
}
