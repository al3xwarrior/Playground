package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.permissions.ChatSettings;
import com.al3x.housing2.Enums.permissions.Gamemodes;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Data.GroupData;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Group {
    private String name;
    private String prefix;
    private String color;
    private String displayName;
    private String suffix;
    private int priority;
    @Setter
    private HashMap<Permissions, Object> permissions; //90% of the time this will be a boolean

    public Group(String name) {
        this.name = name;
        this.prefix = "§3[" + name.toUpperCase() + "] ";
        this.color = "§3";
        this.displayName = "§3" + name;
        this.suffix = "";
        this.priority = 1;
        this.permissions = getDefaultPermissions();
    }

    public Group(GroupData data) {
        this.name = data.getName();
        this.prefix = data.getPrefix();
        this.color = data.getColor();
        this.displayName = data.getDisplayName();
        this.suffix = data.getSuffix();
        this.priority = data.getPriority();
        this.permissions = new HashMap<>();

        for (Permissions permission : Permissions.values()) {
            permissions.put(permission, data.getPermissions().getOrDefault(permission.name(), false));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (prefix.startsWith(this.color)) {
            prefix = prefix.replace(this.color, color);
        }
        if (displayName.startsWith(this.color)) {
            displayName = displayName.replace(this.color, color);
        }
        if (suffix.startsWith(this.color)) {
            suffix = suffix.replace(this.color, color);
        }
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public HashMap<Permissions, Object> getPermissions() {
        return permissions;
    }

    private HashMap<Permissions, Object> getDefaultPermissions() {
        HashMap<Permissions, Object> defaultPermissions = new HashMap<>();
        for (Permissions permission : Permissions.values()) {
            defaultPermissions.put(permission, false);
        }
        defaultPermissions.put(Permissions.FLY, true);
        defaultPermissions.put(Permissions.WOOD_DOOR, true);
        defaultPermissions.put(Permissions.IRON_DOOR, true);
        defaultPermissions.put(Permissions.WOOD_TRAPDOOR, true);
        defaultPermissions.put(Permissions.IRON_TRAPDOOR, true);
        defaultPermissions.put(Permissions.FENCE_GATE, true);
        defaultPermissions.put(Permissions.BUTTON, true);
        defaultPermissions.put(Permissions.LEVER, true);
        defaultPermissions.put(Permissions.LAUNCH_PAD, true);
        defaultPermissions.put(Permissions.CHAT, ChatSettings.THREE);
        defaultPermissions.put(Permissions.USE_CHESTS, true);
        defaultPermissions.put(Permissions.USE_ENDER_CHESTS, true);
        defaultPermissions.put(Permissions.GAMEMODE, Gamemodes.ADVENTURE);
        defaultPermissions.put(Permissions.HOUSING_MENU, true);
        return defaultPermissions;
    }

    public GroupData toData() {
        HashMap<String, Object> permissions = new HashMap<>();
        for (Map.Entry<Permissions, Object> entry : this.permissions.entrySet()) {
            permissions.put(entry.getKey().name(), entry.getValue());
        }
        return new GroupData(name, prefix, color, displayName, suffix, priority, permissions);
    }
}
