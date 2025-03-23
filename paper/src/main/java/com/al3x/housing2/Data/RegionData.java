package com.al3x.housing2.Data;

import com.al3x.housing2.Enums.PvpSettings;
import com.al3x.housing2.Instances.Region;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class RegionData {
    private boolean loaded;
    private LocationData posA;
    private LocationData posB;
    private String name;
    private List<ActionData> exitActions;
    private List<ActionData> enterActions;
    private Map<String, Boolean> pvpSettings;

    public RegionData() {
    }

    public RegionData(boolean loaded, LocationData posA, LocationData posB, String name, List<ActionData> exitActions, List<ActionData> enterActions, Map<String, Boolean> pvpSettings) {
        this.loaded = loaded;
        this.posA = posA;
        this.posB = posB;
        this.name = name;
        this.exitActions = exitActions;
        this.enterActions = enterActions;
        this.pvpSettings = pvpSettings;
    }

    // Getters and setters for all fields

    public static RegionData fromRegion(Region region) {
        return new RegionData(
            region.isLoaded(),
            LocationData.fromLocation(region.getFirst()),
            LocationData.fromLocation(region.getSecond()),
            region.getName(),
            ActionData.fromList(region.getExitActions()),
            ActionData.fromList(region.getEnterActions()),
            region.getPvpSettings()
        );
    }

    public static List<RegionData> fromList(List<Region> regions) {
        return regions.stream()
            .map(RegionData::fromRegion)
            .collect(Collectors.toList());
    }

    public static Region toRegion(RegionData regionData) {
        return new Region(
            regionData.isLoaded(),
            regionData.getPosA().toLocation(),
            regionData.getPosB().toLocation(),
            regionData.getName(),
            ActionData.toList(regionData.getExitActions()),
            ActionData.toList(regionData.getEnterActions()),
            new HashMap<>(regionData.getPvpSettings())
        );
    }

    public static List<Region> toList(List<RegionData> regionDataList) {
        return regionDataList.stream()
            .map(RegionData::toRegion)
            .collect(Collectors.toList());
    }
}