package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Instances.Team;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerData {
    private String group;
    private String team;
    private String inventory;
    private String armor;
    private String enderchest;
    private boolean muted;
    private Instant muteExpiration;
    private boolean banned;
    private Instant banExpiration;
    private List<StatData> stats;
    private String name;
    private transient List<Stat> cacheStats = new ArrayList<>();

    public PlayerData() {

    }

    public PlayerData(String group, String team, String inventory, String armor, String enderchest, boolean muted, Instant muteExpiration, boolean banned, Instant banExpiration, List<StatData> stats, String name) {
        this.group = group;
        this.team = team;
        this.inventory = inventory;
        this.armor = armor;
        this.enderchest = enderchest;
        this.muted = muted;
        this.muteExpiration = muteExpiration;
        this.banned = banned;
        this.banExpiration = banExpiration;
        this.stats = stats;
        this.name = name;
    }

    // Getters and setters for all fields

    public Group getGroupInstance(HousingWorld house) {
        return house.getGroups().stream()
                .filter(g -> g != null && g.getName() != null && g.getName().equals(group))
                .findFirst()
                .orElse(new Group("No Group"));
    }

    public Team getTeamInstance(HousingWorld house) {
        return house.getTeams().stream()
                .filter(t -> t.getName().equals(team))
                .findFirst()
                .orElse(new Team("No Team"));
    }
}