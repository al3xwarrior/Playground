package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Action.Actions.AttackEntityAction;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Getter
public enum ActionEnum {
    CONDITIONAL(ConditionalAction.class),
    CANCEL(CancelAction.class),
    CHANGE_PLAYER_GROUP(ChangePlayerGroupAction.class),
    CHANGE_PLAYER_TEAM(ChangePlayerTeamAction.class),
    KILL_PLAYER(KillPlayerAction.class),
    KILL_NPC(KillNPCAction.class),
    FULL_HEAL(FullHealAction.class),
    SET_VELOCITY(SetVelocityAction.class),
    SEND_TITLE(SendTitleAction.class),
    ACTIONBAR(ActionbarAction.class),
    RESET_INVENTORY(ResetInventoryAction.class),
    CHANGE_MAX_HEALTH(ChangeMaxHealthAction.class),
    GIVE_ITEM(GiveItemAction.class),
    REMOVE_ITEM(RemoveItemAction.class),
    CHAT(ChatAction.class),
    APPLY_POTION(ApplyPotionEffectAction.class),
    CLEAR_POTION(ClearPotionEffectAction.class),
    PLAYER_STAT(PlayerStatAction.class),
    NPC_STAT(NPCStatAction.class),
    GLOBAL_STAT(GlobalStatAction.class),
    TELEPORT(TeleportAction.class),
    CLEAR_ENDERCHEST(ClearEnderChestAction.class),
    PLAY_SOUND(PlaySoundAction.class),
    STOP_SOUND(StopSoundAction.class),
    SET_GAMEMODE(SetGamemodeAction.class),
    CHANGE_HEALTH(ChangeHealthAction.class),
    CHANGE_HUNGER(ChangeHungerAction.class),
    RANDOM_ACTION(RandomAction.class),
    FUNCTION(FunctionAction.class),
    APPLY_INVENTORY_LAYOUT(ApplyInventoryLayoutAction.class),
    PAUSE_EXECUTION(PauseAction.class),
    SHOW_BOSSBAR(ShowBossbarAction.class),
    SET_HITDELAY(SetHitDelayAction.class),
    LAUNCH_PROJECTILE(LaunchProjectileAction.class),
    PARTICLE(ParticleAction.class),
    CHANGE_NPC_NAVIGATION(NpcPathAction.class),
    CHANGE_PLAYER_DISPLAYNAME(ChangePlayerDisplayNameAction.class),
    CHANGE_PLAYER_NAMETAG(NameTagAction.class),
    REPEAT(RepeatAction.class),
    CLEAR_BOSSBAR(ClearBossbarAction.class),
    CLEAR_PLAYERSTATS(ClearPlayerStatsAction.class),
    CLEAR_GLOBALSTATS(ClearGlobalStatsAction.class),
    EXIT(ExitAction.class),
    BREAK(BreakAction.class),
    CONTINUE(ContinueAction.class),
    DISPLAY_MENU(DisplayMenuAction.class),
    CLOSE_MENU(CloseMenuAction.class),
    EXPLOSION(ExplosionAction.class),
    START_HOUSE_MUSIC(StartHouseMusicAction.class),
    STOP_HOUSE_MUSIC(StopHouseMusicAction.class),
    CHANGE_PLAYER_ATTRIBUTE(ChangePlayerAttributeAction.class),
    CHANGE_NPC_ATTRIBUTE(ChangeNPCAttributeAction.class),
    CHANGE_TIME(ChangeTimeAction.class),
    ATTACK_ENTITY(AttackEntityAction.class),
    DROP_ITEM(DropItemAction.class),
    RUN_AS_NPC(RunAsNPCAction.class),
    HIDE_NPC(HideNPCAction.class),
    SHOW_NPC(ShowNPCAction.class),
    SWIM_CRAWL(SwimCrawlAction.class),
    TOGGLE_SIT(ToggleSitAction.class),
    SEND_TO_HOUSE(SendToHouseAction.class),
    SET_VOICE_GROUP(SetVoiceGroupAction.class),
    EDIT_VOICE_GROUP(EditVoiceGroupAction.class),
    EDIT_VISIBILITY(EditVisibilityAction.class),
    EDIT_AUDIBILITY(EditAudibilityAction.class),
    PROTOOLS_ACTION(ProtoolsAction.class),
    SPAWN_GHOST_BLOCK(SpawnGhostBlock.class),
    SET_PLAYER_SLOT(SetPlayerSlotAction.class);
    // Add new actions here
    ;
    private final Class<? extends Action> action;

    ActionEnum(Class<? extends Action> action) {
        this.action = action;
    }

    public Action getActionInstance(HashMap<String, Object> data, String comment, HousingWorld house) {
        try {
            Action action = this.action.getDeclaredConstructor().newInstance();
            action.fromData(data, house);
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
            if (action.name().equals(id)) {
                return action;
            }
        }
        return null;
    }
}
