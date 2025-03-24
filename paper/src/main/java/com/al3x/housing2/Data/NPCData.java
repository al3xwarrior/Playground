package com.al3x.housing2.Data;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class NPCData {
    private int npcID;
    private String npcUUID;
    private String npcName;
    private String npcType;
    private LocationData npcLocation;
    private String npcSkin;
    private List<ActionData> actions;
    private boolean lookAtPlayer;
    private List<String> equipment;
    private String navigationType;
    private List<LocationData> waypoints;
    private HologramData hologramData;
    private Float speed;
    private boolean canBeDamaged;
    private int respawnTime;
    private double maxHealth;
    private boolean minecraftAI;
    private boolean isBaby;
    private Map<String, List<ActionData>> eventActions;
    private List<StatData> stats;

    public NPCData() {
    }

    public NPCData(int npcID, String npcUUID, String npcName, String npcType, LocationData npcLocation, String npcSkin, List<ActionData> actions, boolean lookAtPlayer, List<String> equipment, String navigationType, List<LocationData> waypoints, HologramData hologramData, Float speed, boolean canBeDamaged, int respawnTime, double maxHealth, boolean minecraftAI, boolean isBaby, Map<String, List<ActionData>> eventActions, List<StatData> stats) {
        this.npcID = npcID;
        this.npcUUID = npcUUID;
        this.npcName = npcName;
        this.npcType = npcType;
        this.npcLocation = npcLocation;
        this.npcSkin = npcSkin;
        this.actions = actions;
        this.lookAtPlayer = lookAtPlayer;
        this.equipment = equipment;
        this.navigationType = navigationType;
        this.waypoints = waypoints;
        this.hologramData = hologramData;
        this.speed = speed;
        this.canBeDamaged = canBeDamaged;
        this.respawnTime = respawnTime;
        this.maxHealth = maxHealth;
        this.minecraftAI = minecraftAI;
        this.isBaby = isBaby;
        this.eventActions = eventActions;
        this.stats = stats;
    }

    // Getters and setters for all fields

    public static List<NPCData> fromList(List<HousingNPC> npcList, HousingWorld housingWorld) {
        return npcList.stream().map(npc -> new NPCData(
                npc.getInternalID(),
                npc.getNpcUUID().toString(),
                npc.getName(),
                npc.getEntityType().name(),
                LocationData.fromLocation(npc.getLocation()),
                npc.getSkinUUID(),
                ActionData.fromList(npc.getActions()),
                npc.isLookAtPlayer(),
                npc.getEquipment(),
                npc.getNavigationType().name(),
                LocationData.fromLocationList(npc.getWaypoints()),
                npc.getHologram() != null ? HologramData.fromData(npc.getHologram()) : null,
                NumberUtilsKt.toFloat(npc.getSpeed()),
                npc.isCanBeDamaged(),
                npc.getRespawnTime(),
                npc.getMaxHealth(),
                npc.getMinecraftAI(),
                npc.isBaby(),
                ActionData.fromHashMap(npc.getEventActions()),
                StatData.fromList(npc.getStats())
        )).collect(Collectors.toList());
    }
}