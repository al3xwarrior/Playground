package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.permissions.ChatSettings;
import com.al3x.housing2.Enums.permissions.Gamemodes;
import com.al3x.housing2.Instances.HousingData.GroupData;
import com.al3x.housing2.Instances.HousingData.TeamData;

import java.util.HashMap;

public class Team {
    private String name;
    private String prefix;
    private String color;
    private String displayName;
    private String suffix;
    private boolean friendlyFire;

    public Team(String name) {
        this.name = name;
        this.prefix = "§3[" + name.toUpperCase() + "] ";
        this.color = "§3";
        this.displayName = "§3" + name;
        this.suffix = "";
        this.friendlyFire = false;
    }

    public Team(TeamData data) {
        this.name = data.getName();
        this.prefix = data.getPrefix();
        this.color = data.getColor();
        this.displayName = data.getDisplayName();
        this.suffix = data.getSuffix();
        this.friendlyFire = data.getFriendlyFire();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public TeamData toData() {
        return new TeamData(name, prefix, color, displayName, suffix, friendlyFire);
    }
}
