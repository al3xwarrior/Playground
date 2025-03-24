package com.al3x.housing2.Data;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class GroupData {
    private String name;
    private String prefix;
    private String color;
    private String displayName;
    private String suffix;
    private int priority;
    private HashMap<String, Object> permissions;

    public GroupData() {

    }

    public GroupData(String name, String prefix, String color, String displayName, String suffix, int priority, HashMap<String, Object> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.displayName = displayName;
        this.suffix = suffix;
        this.priority = priority;
        this.permissions = permissions;
    }

    public static List<GroupData> fromList(List<Group> list) {
        List<GroupData> groupData = new ArrayList<>();
        for (Group group : list) {
            groupData.add(group.toData());
        }
        return groupData;
    }

    public static List<Group> toList(List<GroupData> data) {
        List<Group> groups = new ArrayList<>();
        for (GroupData groupData : data) {
            groups.add(new Group(groupData));
        }
        return groups;
    }
}