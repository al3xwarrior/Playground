package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Action.Actions.AttackEntityAction;
import com.al3x.housing2.Instances.HousingWorld;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ActionEnum {
    CONDITIONAL("conditional_action", ConditionalAction.class),
    CANCEL("cancel_action", CancelAction.class),
    CHANGE_PLAYER_GROUP("change_player_group_action", ChangePlayerGroupAction.class),
    CHANGE_PLAYER_TEAM("change_player_team_action", ChangePlayerTeamAction.class),
    KILL_PLAYER("kill_player_action", KillPlayerAction.class),
    KILL_NPC("kill_npc_action", KillNPCAction.class),
    FULL_HEAL("full_heal_action", FullHealAction.class),
    SET_VELOCITY("set_velocity_action", SetVelocityAction.class),
    SEND_TITLE("send_title_action", SendTitleAction.class),
    ACTIONBAR("actionbar_action", ActionbarAction.class),
    RESET_INVENTORY("reset_inventory_action", ResetInventoryAction.class),
    CHANGE_MAX_HEALTH("change_max_health_action", ChangeMaxHealthAction.class),
    GIVE_ITEM("give_item_action", GiveItemAction.class),
    REMOVE_ITEM("remove_item_action", RemoveItemAction.class),
    CHAT("display_chat_action", ChatAction.class),
    APPLY_POTION("apply_potion_effect_action", ApplyPotionEffectAction.class),
    CLEAR_POTION("clear_potion_effect_action", ClearPotionEffectAction.class),
    PLAYER_STAT("player_stat_action", PlayerStatAction.class),
    NPC_STAT("npc_stat_action", NPCStatAction.class),
    GLOBAL_STAT("global_stat_action", GlobalStatAction.class),
    TELEPORT("teleport_action", TeleportAction.class),
    CLEAR_ENDERCHEST("clear_enderchest_action", ClearEnderChestAction.class),
    PLAY_SOUND("play_sound_action", PlaySoundAction.class),
    STOP_SOUND("stop_sound_action", StopSoundAction.class),
    SET_GAMEMODE("set_gamemode_action", SetGamemodeAction.class),
    CHANGE_HEALTH("change_health_action", ChangeHealthAction.class),
    CHANGE_HUNGER("change_hunger_action", ChangeHungerAction.class),
    RANDOM_ACTION("random_action", RandomAction.class),
    FUNCTION("function_action", FunctionAction.class),
    APPLY_INVENTORY_LAYOUT("apply_inventory_layout_action", ApplyInventoryLayoutAction.class),
    PAUSE_EXECUTION("pause_action", PauseAction.class),
    SHOW_BOSSBAR("show_bossbar_action", ShowBossbarAction.class),
    SET_HITDELAY("set_hit_delay_action", SetHitDelayAction.class),
    LAUNCH_PROJECTILE("launch_projectile_action", LaunchProjectileAction.class),
    PARTICLE("particle_action", ParticleAction.class),
    CHANGE_NPC_NAVIGATION("npc_navigation_action", NpcPathAction.class),
    CHANGE_PLAYER_DISPLAYNAME("change_player_display_name_action", ChangePlayerDisplayNameAction.class),
    REPEAT("repeat_action", RepeatAction.class),
    CLEAR_BOSSBAR("Clear Bossbars Action", ClearBossbarAction.class),
    CLEAR_PLAYERSTATS("clear_bossbar_action", ClearPlayerStatsAction.class),
    CLEAR_GLOBALSTATS("clear_global_stats_action", ClearGlobalStatsAction.class),
    EXIT("exit_action", ExitAction.class),
    BREAK("break_action", BreakAction.class),
    CONTINUE("continue_action", ContinueAction.class),
    DISPLAY_MENU("display_menu_action", DisplayMenuAction.class),
    CLOSE_MENU("close_menu_action", CloseMenuAction.class),
    EXPLOSION("explosion_action", ExplosionAction.class),
    START_HOUSE_MUSIC("start_house_music_action", StartHouseMusicAction.class),
    STOP_HOUSE_MUSIC("stop_house_music_action", StopHouseMusicAction.class),
    CHANGE_PLAYER_ATTRIBUTE("change_player_attribute_action", ChangePlayerAttributeAction.class),
    CHANGE_NPC_ATTRIBUTE("change_npc_attribute_actionAction", ChangeNPCAttributeAction.class),
    CHANGE_TIME("change_time_action", ChangeTimeAction.class),
    ATTACK_ENTITY("attack_entity_action", AttackEntityAction.class),
    DROP_ITEM("drop_item_action", DropItemAction.class),
    RUN_AS_NPC("run_as_npc_action", RunAsNPCAction.class),
    HIDE_NPC("hide_npc_action", HideNPCAction.class),
    SHOW_NPC("Show NPC Action", ShowNPCAction.class),
    SWIM_CRAWL("show_npc_action", SwimCrawlAction.class),
    SET_VOICE_GROUP("set_voice_group_action", SetVoiceGroupAction.class),
    EDIT_VOICE_GROUP("edit_voice_group_action", EditVoiceGroupAction.class),
    EDIT_VISIBILITY("edit_visibility_action", EditVisibilityAction.class),
    EDIT_AUDIBILITY("edit_audibility_action", EditAudibilityAction.class),
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

    public Action getActionInstance(HashMap<String, Object> data, String comment, HousingWorld house) {
        try {
            Action action = this.action.getDeclaredConstructor().newInstance();
            action.fromData(data, this.action, house);
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
