package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ActionEnum {
    CONDITIONAL("Conditional Action", ConditionalAction.class),
    CANCEL("Cancel Action", CancelAction.class),
    CHANGE_PLAYER_GROUP("Change Player Group", ChangePlayerGroupAction.class),
    CHANGE_PLAYER_TEAM("Change Player Team", ChangePlayerTeamAction.class),
    KILL_PLAYER("Kill Player Action", KillPlayerAction.class),
    FULL_HEAL("Full Heal Action", FullHealAction.class),
    SET_VELOCITY("Set Velocity Action", SetVelocityAction.class),
    SEND_TITLE("Send Title Action", SendTitleAction.class),
    ACTIONBAR("Actionbar Action", ActionbarAction.class),
    RESET_INVENTORY("Reset Inventory Action", ResetInventoryAction.class),
    CHANGE_MAX_HEALTH("Change Max Health Action", ChangeMaxHealthAction.class),
    GIVE_ITEM("Give Item Action", GiveItemAction.class),
    REMOVE_ITEM("Remove Item Action", RemoveItemAction.class),
    CHAT("Chat Action", ChatAction.class),
    APPLY_POTION("Apply Potion Effect Action", ApplyPotionEffectAction.class),
    CLEAR_POTION("Clear Potion Effect Action", ClearPotionEffectAction.class),
    PLAYER_STAT("Player Stat Action", PlayerStatAction.class),
    GLOBAL_STAT("Global Stat Action", GlobalStatAction.class),
    TELEPORT("Teleport Action", TeleportAction.class),
    CLEAR_ENDERCHEST("Clear Enderchest Action", ClearEnderChestAction.class),
    PLAY_SOUND("Play Sound Action", PlaySoundAction.class),
    SET_GAMEMODE("Set Gamemode Action", SetGamemodeAction.class),
    CHANGE_HEALTH("Change Health Action", ChangeHealthAction.class),
    CHANGE_HUNGER("Change Hunger Action", ChangeHungerAction.class),
    RANDOM_ACTION("Random Action", RandomAction.class),
    FUNCTION("Function Action", FunctionAction.class),
    APPLY_INVENTORY_LAYOUT("Apply Inventory Layout Action", ApplyInventoryLayoutAction.class),
    PAUSE_EXECUTION("Pause Action", PauseAction.class),
    SHOW_BOSSBAR("Show Bossbar Action", ShowBossbarAction.class),
    SET_HITDELAY("Set Hit Delay Action", SetHitDelayAction.class),
    LAUNCH_PROJECTILE("Launch Projectile Action", LaunchProjectileAction.class),
    PARTICLE("Particle Action", ParticleAction.class),
    CHANGE_NPC_NAVIGATION("Change Npc Navigation Action", NpcPathAction.class),
    CHANGE_PLAYER_DISPLAYNAME("Change Player Display Name Action", ChangePlayerDisplayNameAction.class),
    REPEAT("Repeat Action", RepeatAction.class),
    CLEAR_BOSSBAR("Clear Bossbars Action", ClearBossbarAction.class),
    CLEAR_PLAYERSTATS("Clear Player Stats Action", ClearPlayerStatsAction.class),
    CLEAR_GLOBALSTATS("Clear Global Stats Action", ClearGlobalStatsAction.class),
    EXIT("Exit Action", ExitAction.class),
    BREAK("Break Action", BreakAction.class),
    CONTINUE("Continue Action", ContinueAction.class),
    DISPLAY_MENU("Display Menu Action", DisplayMenuAction.class),
    CLOSE_MENU("Close Menu Action", CloseMenuAction.class),
    EXPLOSION("Explosion Action", ExplosionAction.class),
    START_HOUSE_MUSIC("Start House Music Action", StartHouseMusicAction.class),
    STOP_HOUSE_MUSIC("Stop House Music Action", StopHouseMusicAction.class),
    CHANGE_PLAYER_ATTRIBUTE("Change Player Attribute Action", ChangePlayerAttributeAction.class),
    CHANGE_TIME("Change Time Action", ChangeTimeAction.class),
    ATTACK_ENTITY("Attack Entity Action", AttackEntityAction.class),
    DROP_ITEM("Drop Item Action", DropItemAction.class),
    // Add new actions here
    // Name of the action and the class that has the name of the action need to be the exact same
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

    public Action getActionInstance(HashMap<String, Object> data, String comment) {
        try {
            Action action = this.action.getDeclaredConstructor().newInstance();
            action.fromData(data, this.action);
            action.setComment(comment);
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
