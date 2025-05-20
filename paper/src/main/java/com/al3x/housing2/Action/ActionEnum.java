package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.*;
import com.al3x.housing2.Action.Actions.AttackEntityAction;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Getter
public enum ActionEnum {
    CONDITIONAL("Conditional Action", ConditionalAction.class),
    CANCEL("Cancel Action", CancelAction.class),
    CHANGE_PLAYER_GROUP("Change Player Group", ChangePlayerGroupAction.class),
    CHANGE_PLAYER_TEAM("Change Player Team", ChangePlayerTeamAction.class),
    KILL_PLAYER("Kill Player Action", KillPlayerAction.class),
    KILL_NPC("Kill NPC Action", KillNPCAction.class),
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
    NPC_STAT("NPC Stat Action", NPCStatAction.class),
    GLOBAL_STAT("Global Stat Action", GlobalStatAction.class),
    TELEPORT("Teleport Action", TeleportAction.class),
    CLEAR_ENDERCHEST("Clear Enderchest Action", ClearEnderChestAction.class),
    PLAY_SOUND("Play Sound Action", PlaySoundAction.class),
    STOP_SOUND("Stop Sound Action", StopSoundAction.class),
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
    CHANGE_PLAYER_NAMETAG("NameTag Action", NameTagAction.class),
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
    CHANGE_NPC_ATTRIBUTE("Change NPC Attribute Action", ChangeNPCAttributeAction.class),
    CHANGE_TIME("Change Time Action", ChangeTimeAction.class),
    ATTACK_ENTITY("Attack Entity Action", AttackEntityAction.class),
    DROP_ITEM("Drop Item Action", DropItemAction.class),
    RUN_AS_NPC("Run As NPC Action", RunAsNPCAction.class),
    HIDE_NPC("Hide NPC Action", HideNPCAction.class),
    SHOW_NPC("Show NPC Action", ShowNPCAction.class),
    SWIM_CRAWL("Swim/Crawl Action", SwimCrawlAction.class),
    TOGGLE_SIT("Toggle Sit Action", ToggleSitAction.class),
    SEND_TO_HOUSE("Send to House Action", SendToHouseAction.class),
    SET_VOICE_GROUP("Set Voice Group Action", SetVoiceGroupAction.class),
    EDIT_VOICE_GROUP("Edit Voice Group Action", EditVoiceGroupAction.class),
    EDIT_VISIBILITY("Edit Visibility Action", EditVisibilityAction.class),
    EDIT_AUDIBILITY("Edit Audibility Action", EditAudibilityAction.class),
    PROTOOLS_ACTION("Protools Action", ProtoolsAction.class),
    SPAWN_GHOST_BLOCK("Spawn Ghost Block", SpawnGhostBlock.class),
    SET_PLAYER_SLOT("Set Player Slot Action", SetPlayerSlotAction.class),
    SET_GUI_SLOT("Set GUI Slot Action", SetGuiSlotAction.class),
//    SPAWN_ENTITY("Spawn Entity Action", SpawnEntityAction.class),
//    ENTITY_META("Entity Meta Action", EntityMetaAction.class),
    ;
    // Add new actions here
    ;
    private final String id;
    private final Class<? extends Action> action;

    ActionEnum(String id, Class<? extends Action> action) {
        this.id = id;
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
            if (action.getId().equals(id)) {
                return action;
            }
        }
        return null;
    }
}
