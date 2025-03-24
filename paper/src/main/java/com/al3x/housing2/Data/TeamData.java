package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamData {
    private String name;
    private String prefix;
    private String color;
    private String displayName;
    private String suffix;
    private boolean friendlyFire;

    public TeamData() {
    }

    public TeamData(String name, String prefix, String color, String displayName, String suffix, boolean friendlyFire) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.displayName = displayName;
        this.suffix = suffix;
        this.friendlyFire = friendlyFire;
    }


    public static List<TeamData> fromList(List<Team> list) {
        List<TeamData> teamData = new ArrayList<>();
        for (Team team : list) {
            teamData.add(team.toData());
        }
        return teamData;
    }

    public static List<Team> toList(List<TeamData> data) {
        List<Team> teams = new ArrayList<>();
        for (TeamData teamData : data) {
            teams.add(new Team(teamData));
        }
        return teams;
    }
}