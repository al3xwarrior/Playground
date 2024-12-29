package com.al3x.housing2.Enums.permissions;

import com.al3x.housing2.Utils.StringUtilsKt;

public enum Permissions implements PermissionInterface{
    FLY,
    WOOD_DOOR,
    IRON_DOOR,
    WOOD_TRAPDOOR,
    IRON_TRAPDOOR,
    FENCE_GATE,
    BUTTON,
    LEVER,
    LAUNCH_PAD,
    COMMAND_TP,
    COMMAND_TP_OTHER,
    JUKEBOX, //PSHHHHH this will probably never happen lmfao
    KICK,
    BAN,
    MUTE,
    CHAT(ChatSettings.class), //Chat will have a subenum for off, on, 1, 2, 3, 5, etc
    PET_SPAWNING, //Another things that will probably never happen
    BUILD,
    OFFLINE_BUILD(), //Another thing where it can depend on something here
    FLUID, //requires build
    PRO_TOOLS, //requires build
    USE_CHESTS,
    USE_ENDER_CHESTS,
    ITEM_EDITOR,
    GAMEMODE(Gamemodes.class), //Gamemode enum
    COMMAND_GAMEMODE,
    COMMAND_EDITSTATS,
    CHANGE_PLAYER_GROUP,
    CHANGE_GAMERULES,
    HOUSING_MENU,
    TEAM_CHAT_SPY,
    EDIT_ACTIONS,
    EDIT_REGIONS,
    EDIT_SCOREBOARD,
    EDIT_EVENTS,
    EDIT_COMMANDS,
    EDIT_FUNCTIONS,
    EDIT_INVENTORY_LAYOUTS,
    EDIT_TEAMS,
    EDIT_CUSTOM_MENUS,
    ITEM_MAILBOX,
    ITEM_EGG_HUNT,
    ITEM_TELEPORT_PAD,
    ITEM_LAUNCH_PAD,
    ITEM_ACTION_PAD,
    ITEM_HOLOGRAM,
    ITEM_NPCS,
    ITEM_ACTION_BUTTON,
    ITEM_LEADERBOARD,
    ITEM_TRASHCAN,
    ITEM_BIOME_STICK

    ;

    Class<? extends PermissionInterface> subEnum;

    Permissions() {
    }

    Permissions(Class<? extends PermissionInterface> subEnum) {
        this.subEnum = subEnum;
    }

    public String getDisplayName() {
        return StringUtilsKt.formatCapitalize(name());
    }

    public String getDescription() {
        return "";
    }

    public Object cycle(Object value) {
        if (subEnum != null) {
            try {
                return subEnum.getMethod("cycle", Object.class).invoke(value, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (value instanceof Boolean) {
            return !(Boolean) value;
        }
        return value;
    }
}
