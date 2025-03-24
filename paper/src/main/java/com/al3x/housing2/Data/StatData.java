package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Stat;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
public class StatData {
    private String name;
    private String value; // Can be a string, double, or int

    public StatData() {}

    public StatData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String ofString() {
        return value;
    }

    public double ofDouble() {
        return Double.parseDouble(value);
    }

    public int ofInt() {
        return Integer.parseInt(value);
    }

    public static Map<String, List<StatData>> fromHashMap(Map<UUID, List<Stat>> statMap) {
        return statMap.entrySet().stream().collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> entry.getValue().stream()
                .filter(stat -> stat.getValue() != null)
                .map(stat -> new StatData(stat.getStatName(), stat.getValue()))
                .collect(Collectors.toList())
        ));
    }

    public static Map<UUID, List<Stat>> toHashMap(Map<String, List<StatData>> statMap) {
        return statMap.entrySet().stream().collect(Collectors.toMap(
            entry -> UUID.fromString(entry.getKey()),
            entry -> entry.getValue().stream()
                .map(statData -> new Stat(statData.getName(), statData.getValue()))
                .collect(Collectors.toList())
        ));
    }

    public static List<StatData> fromList(List<Stat> statList) {
        return statList.stream()
            .filter(stat -> stat.getValue() != null)
            .map(stat -> new StatData(stat.getStatName(), stat.getValue()))
            .collect(Collectors.toList());
    }

    public static List<Stat> toList(List<StatData> statDataList) {
        return statDataList.stream()
            .map(statData -> new Stat(statData.getName(), statData.getValue()))
            .collect(Collectors.toList());
    }
}