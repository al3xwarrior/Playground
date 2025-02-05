package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Enums.EventType
import com.al3x.housing2.Enums.HousePrivacy
import com.al3x.housing2.Enums.WeatherTypes
import com.al3x.housing2.Instances.HousingData.StatData.Companion.fromHashMap
import com.al3x.housing2.Instances.HousingData.StatData.Companion.fromList
import com.al3x.housing2.Instances.HousingWorld
import com.al3x.housing2.Instances.LaunchPad
import org.bukkit.Material

data class HouseData(
    val ownerID: String,
    val houseID: String,
    var ownerName: String? = null,
    var houseName: String,
    var description: String,
    var size: Int,
//    var guests: Int,
    var cookies: Int,
    var cookieWeek: Int,
    var privacy: String? = HousePrivacy.PRIVATE.name,
    var icon: String? = Material.OAK_DOOR.name,
    var timeCreated: Long,
    var eventActions: HashMap<EventType, List<ActionData>>,
    var spawnLocation: LocationData?,
    var scoreboard: List<String>,
    var houseNPCs: List<NPCData>,
    var globalStats : List<StatData>,
    var commands: List<CommandData>? = arrayListOf(),
    var regions: List<RegionData>? = arrayListOf(),
    var layouts: List<LayoutData>? = arrayListOf(),
    var holograms: List<HologramData>? = arrayListOf(),
    var customMenus: List<CustomMenuData>? = arrayListOf(),
    var groups: List<GroupData>? = arrayListOf(),
    var teams: List<TeamData>? = arrayListOf(),
    var defaultGroup: String? = "default",
    var playerData: HashMap<String, PlayerData>? = hashMapOf(),
    var trashCans: List<LocationData>? = arrayListOf(),
    var launchPads: List<LaunchPad>? = arrayListOf(),
    val seed: String,
    var functions: List<FunctionData>? = arrayListOf(),
    var version: Int?,
    var ingameTime: Long?,
    var dayLightCycle: Boolean?,
    var weather: WeatherTypes?,
    var weatherCycle: Boolean?
) {

    companion object {
        fun fromHousingWorld(world: HousingWorld): HouseData {
            val houseData = HouseData(
                world.ownerUUID.toString(),
                world.houseUUID.toString(),
                world.owner.name,
                world.name,
                world.description,
                world.size,
                world.cookies,
                world.cookieWeek,
                world.privacy.name,
                world.icon.name,
                world.timeCreated,
                ActionData.fromHashMap(world.eventActions, world),
                LocationData.fromLocation(world.spawn),
                world.scoreboard,
                NPCData.fromList(world.npCs),
                fromList(world.statManager.globalStats),
                CommandData.fromList(world.commands),
                RegionData.fromList(world.regions),
                LayoutData.fromList(world.layouts),
                HologramData.fromList(world.holograms),
                CustomMenuData.fromList(world.customMenus),
                GroupData.fromList(world.groups),
                TeamData.fromList(world.teams),
                world.defaultGroup,
                world.playersData,
                LocationData.fromLocationList(world.trashCans),
                world.launchPads,
                world.seed,
                FunctionData.fromList(world.functions),
                world.version,
                world.ingameTime,
                world.daylightCycle,
                world.weather,
                world.weatherCycle
            )
            return houseData
        }
    }
}


