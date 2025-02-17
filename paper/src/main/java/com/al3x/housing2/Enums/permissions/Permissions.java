package com.al3x.housing2.Enums.permissions;

import com.al3x.housing2.Utils.StringUtilsKt;

public enum Permissions implements PermissionInterface{
    FLY(true),
    WOOD_DOOR(true),
    IRON_DOOR(true),
    WOOD_TRAPDOOR(true),
    IRON_TRAPDOOR(true),
    FENCE_GATE(true),
    BUTTON(true),
    LEVER(true),
    LAUNCH_PAD,
    COMMAND_TP,
    COMMAND_TP_OTHER,
    JUKEBOX, //PSHHHHH this will probably never happen lmfao // sike -al3x
    KICK,
    BAN,
    MUTE,
    CHAT(false, ChatSettings.class), //Chat will have a subenum for off, on, 1, 2, 3, 5, etc
    PET_SPAWNING, //Another things that will probably never happen
    BUILD(true),
    OFFLINE_BUILD(true), //Another thing where it can depend on something here
    FLUID, //requires build
    PRO_TOOLS(true), //requires build
    ENTITY_BUCKET(true), //requires build
    USE_CHESTS(true),
    USE_ENDER_CHESTS(true),
    USE_SHULKERS(true),
    ITEM_EDITOR(true),
    GAMEMODE(true, Gamemodes.class), //Gamemode enum
    COMMAND_GAMEMODE(true),
    COMMAND_EDITSTATS(false),
    CHANGE_PLAYER_GROUP,
    CHANGE_PLAYER_TEAM(true),
    CHANGE_GAMERULES,
    HOUSING_MENU(true),
    HOUSE_SETTINGS(true),
    EDIT_PERMISSIONS_AND_GROUP(true),
    TEAM_CHAT_SPY,
    EDIT_ACTIONS(true),
    EDIT_REGIONS(true),
    EDIT_SCOREBOARD(true),
    EDIT_EVENTS(true),
    EDIT_COMMANDS(true),
    EDIT_FUNCTIONS(true),
    EDIT_INVENTORY_LAYOUTS(true),
    EDIT_TEAMS(true),
    EDIT_CUSTOM_MENUS(true),
    ITEMS,
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
    ITEM_BIOME_STICK,
    RESET_PLAYER_DATA(true),
    ;

    Class<? extends PermissionInterface> subEnum;
    boolean implemented = false;

    Permissions() {
    }

    Permissions(boolean implemented) {
        this.implemented = implemented;
    }

    Permissions(boolean implemented, Class<? extends PermissionInterface> subEnum) {
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
