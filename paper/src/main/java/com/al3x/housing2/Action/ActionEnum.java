package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Action.Actions.AttackEntityAction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ActionEnum {
    CONDITIONAL("conditional", ConditionalAction.class),
    CANCEL("cancel", CancelAction.class),
    CHANGE_PLAYER_GROUP("group", ChangePlayerGroupAction.class),
    CHANGE_PLAYER_TEAM("team", ChangePlayerTeamAction.class),
    KILL_PLAYER("killPlayer", KillPlayerAction.class),
    KILL_NPC("killNpc", KillNPCAction.class),
    FULL_HEAL("fullHeal", FullHealAction.class),
    SET_VELOCITY("velocity", SetVelocityAction.class),
    SEND_TITLE("title", SendTitleAction.class),
    ACTIONBAR("actionbar", ActionbarAction.class),
    RESET_INVENTORY("resetInventory", ResetInventoryAction.class),
    CHANGE_MAX_HEALTH("maxHealth", ChangeMaxHealthAction.class),
    GIVE_ITEM("giveItem", GiveItemAction.class),
    REMOVE_ITEM("removeItem", RemoveItemAction.class),
    CHAT("chat", ChatAction.class),
    APPLY_POTION("effect", ApplyPotionEffectAction.class),
    CLEAR_POTION("removeEffect", ClearPotionEffectAction.class),
    PLAYER_STAT("playerstat", PlayerStatAction.class),
    NPC_STAT("npcstat", NPCStatAction.class),
    GLOBAL_STAT("globalstat", GlobalStatAction.class),
    TELEPORT("teleport", TeleportAction.class),
    CLEAR_ENDERCHEST("clearEnderchest", ClearEnderChestAction.class),
    PLAY_SOUND("playSound", PlaySoundAction.class),
    STOP_SOUND("stopSound", StopSoundAction.class),
    SET_GAMEMODE("setGamemode", SetGamemodeAction.class),
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
    BREAK("Break Action", BreakBlockAction.class),
    CONTINUE("Continue Action", ContinueAction.class),
    DISPLAY_MENU("Display Menu Action", DisplayMenuAction.class),
    CLOSE_MENU("Close Menu Action", CloseMenuAction.class),
    EXPLOSION("Explosion Action", ExplosionAction.class),
    START_HOUSE_MUSIC("Start House Music Action", StartHouseMusicAction.class),
    STOP_HOUSE_MUSIC("Stop House Music Action", StopHouseMusicAction.class),
    CHANGE_PLAYER_ATTRIBUTE("Change Player Attribute Action", ChangePlayerAttributeAction.class),
    CHANGE_NPC_ATTRIBUTE("Change NPC Attribute Action", ChangeNPCAttributeAction.class),
    CHANGE_TIME("Change Time Action", ChangeTimeAction.class),
    ATTACK_ENTITY("Attack Entity Action", AttackEntityAction.class),
    DROP_ITEM("Drop Item Action", DropItemAction.class),
    RUN_AS_NPC("Run As NPC Action", RunAsNPCAction.class),
    HIDE_NPC("Hide NPC Action", HideNPCAction.class),
    SHOW_NPC("Show NPC Action", ShowNPCAction.class),
    SWIM_CRAWL("Swim/Crawl Action", SwimCrawlAction.class),
    SET_VOICE_GROUP("Set Voice Group Action", SetVoiceGroupAction.class),
    EDIT_VOICE_GROUP("Edit Voice Group Action", EditVoiceGroupAction.class),
    EDIT_VISIBILITY("Edit Visibility Action", EditVisibilityAction.class),
    EDIT_AUDIBILITY("Edit Audibility Action", EditAudibilityAction.class),
    // Add new actions here
    // Name of the action and the class that has the name of the action need to be the exact same
    ;
    private final String id;
    private final Class<? extends Action> action;

    ActionEnum(String id, Class<? extends Action> action) {
        this.id = id;
        this.action = action;
    }

    public Class<? extends Action> getAction() {
        return action;
    }

    public String getId() {
        return id;
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

    public static ActionEnum getActionById(String id) {
        for (ActionEnum action : ActionEnum.values()) {
            if (action.id.equals(id)) {
                return action;
            }
        }
        return null;
    }
}
