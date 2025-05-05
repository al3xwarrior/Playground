package com.al3x.housing2.Data;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.WeatherTypes;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.LaunchPad;
import com.al3x.housing2.Utils.Serialization;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class HouseData {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String objectId;
    private String ownerID;
    private String houseID;
    private String ownerName;
    private String houseName;
    private String description;
    private int size;
    private int cookies;
    private int cookieWeek;
    private String privacy;
    private String icon;
    private long timeCreated;
    private HashMap<String, List<ActionData>> eventActions;
    private LocationData spawnLocation;
    private List<String> scoreboard;
    private String scoreboardTitle;
    private List<NPCData> houseNPCs;
    private List<StatData> globalStats;
    private List<CommandData> commands;
    private List<RegionData> regions;
    private List<LayoutData> layouts;
    private List<HologramData> holograms;
    private List<CustomMenuData> customMenus;
    private List<GroupData> groups;
    private List<TeamData> teams;
    private String defaultGroup;
    private HashMap<String, PlayerData> playerData;
    private List<LocationData> trashCans;
    private List<LaunchPad> launchPads;
    private HashMap<String, List<ActionData>> actionButtons;
    private String seed;
    private List<FunctionData> functions;
    private Integer version;
    private Long ingameTime;
    private Boolean dayLightCycle;
    private WeatherTypes weather;
    private Boolean weatherCycle;
    private Boolean joinLeaveMessages;
    private Boolean deathMessages;
    private Boolean jukeboxPlaying;
    private Boolean keepInventory;
    private Boolean playerCollisions;
    private String lockedMessage;
    private ResourcePackData resourcePack;
    private Boolean randomTicks;
    private List<String> whitelistedPlayers;

    //For pojo
    public HouseData() {

    }

    public HouseData(String ownerID, String houseID, String ownerName, String houseName, String description, int size, int cookies, int cookieWeek, String privacy, String icon, long timeCreated, HashMap<String, List<ActionData>> eventActions, LocationData spawnLocation, List<String> scoreboard, String scoreboardTitle, List<NPCData> houseNPCs, List<StatData> globalStats, List<CommandData> commands, List<RegionData> regions, List<LayoutData> layouts, List<HologramData> holograms, List<CustomMenuData> customMenus, List<GroupData> groups, List<TeamData> teams, String defaultGroup, HashMap<String, PlayerData> playerData, List<LocationData> trashCans, List<LaunchPad> launchPads, HashMap<String, List<ActionData>> actionButtons, String seed, List<FunctionData> functions, Integer version, Long ingameTime, Boolean dayLightCycle, WeatherTypes weather, Boolean weatherCycle, Boolean joinLeaveMessages, Boolean deathMessages, Boolean jukeboxPlaying, Boolean keepInventory, Boolean playerCollisions, String lockedMessage, ResourcePackData resourcePack, Boolean randomTicks, List<String> whitelistedPlayers) {
        this.ownerID = ownerID;
        this.houseID = houseID;
        this.ownerName = ownerName;
        this.houseName = houseName;
        this.description = description;
        this.size = size;
        this.cookies = cookies;
        this.cookieWeek = cookieWeek;
        this.privacy = privacy != null ? privacy : HousePrivacy.PRIVATE.name();
        this.icon = icon != null ? icon : Material.OAK_DOOR.name();
        this.timeCreated = timeCreated;
        this.eventActions = eventActions;
        this.spawnLocation = spawnLocation;
        this.scoreboard = scoreboard;
        this.scoreboardTitle = scoreboardTitle;
        this.houseNPCs = houseNPCs;
        this.globalStats = globalStats;
        this.commands = commands;
        this.regions = regions;
        this.layouts = layouts;
        this.holograms = holograms;
        this.customMenus = customMenus;
        this.groups = groups;
        this.teams = teams;
        this.defaultGroup = defaultGroup != null ? defaultGroup : "default";
        this.playerData = playerData;
        this.trashCans = trashCans;
        this.launchPads = launchPads;
        this.actionButtons = actionButtons;
        this.seed = seed;
        this.functions = functions;
        this.version = version;
        this.ingameTime = ingameTime;
        this.dayLightCycle = dayLightCycle;
        this.weather = weather;
        this.weatherCycle = weatherCycle;
        this.joinLeaveMessages = joinLeaveMessages;
        this.deathMessages = deathMessages;
        this.jukeboxPlaying = jukeboxPlaying;
        this.keepInventory = keepInventory;
        this.playerCollisions = playerCollisions;
        this.lockedMessage = lockedMessage;
        this.resourcePack = resourcePack;
        this.randomTicks = randomTicks;
        this.whitelistedPlayers = whitelistedPlayers;
    }

    public static HouseData fromHousingWorld(HousingWorld world) {
        return new HouseData(
                world.getOwnerUUID().toString(),
                world.getHouseUUID().toString(),
                world.getOwner().getName(),
                world.getName(),
                world.getDescription(),
                world.getSize(),
                world.getCookies(),
                world.getCookieWeek(),
                world.getPrivacy().name(),
                Serialization.itemStackToBase64(world.getIcon()),
                world.getTimeCreated(),
                ActionData.fromHashMap(world.getEventActions(), world),
                LocationData.fromLocation(world.getSpawn()),
                world.getScoreboard(),
                world.getScoreboardTitle(),
                NPCData.fromList(world.getNPCs(), world),
                StatData.fromList(world.getStatManager().getGlobalStats()),
                CommandData.fromList(world.getCommands()),
                RegionData.fromList(world.getRegions()),
                LayoutData.fromList(world.getLayouts()),
                HologramData.fromList(world.getHolograms()),
                CustomMenuData.fromList(world.getCustomMenus()),
                GroupData.fromList(world.getGroups()),
                TeamData.fromList(world.getTeams()),
                world.getDefaultGroup(),
                world.getPlayersData(),
                LocationData.fromLocationList(world.getTrashCans()),
                world.getLaunchPads(),
                world.getActionButtons(),
                world.getSeed(),
                FunctionData.fromList(world.getFunctions()),
                world.getVersion(),
                world.getIngameTime(),
                world.getDaylightCycle(),
                world.getWeather(),
                world.getWeatherCycle(),
                world.getJoinLeaveMessages(),
                world.isJukeboxPlaying(),
                world.getDeathMessages(),
                world.getKeepInventory(),
                world.getPlayerCollisions(),
                world.getLockedReason(),
                world.getResourcePack(),
                world.getRandomTicks(),
                world.getWhitelistedPlayers().stream().map(player -> player.getUniqueId().toString()).toList()
        );
    }

    public String getHouseID() {
        return houseID;
    }

    public String getName() {
        return houseName;
    }

    public String getOwnerID() {
        return ownerID;
    }
}