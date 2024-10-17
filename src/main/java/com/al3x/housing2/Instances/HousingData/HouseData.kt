package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Enums.EventType
import com.al3x.housing2.Instances.HousingData.StatData.Companion.fromHashMap
import com.al3x.housing2.Instances.HousingWorld

data class HouseData(
    val ownerID: String,
    val houseID: String,
    var houseName: String,
    var description: String,
    var size: Int,
//    var guests: Int,
    var cookies: Double,
    var timeCreated: Long,
    var eventActions: HashMap<EventType, List<ActionData>>,
    var spawnLocation: LocationData,
    var scoreboard: List<String>,
    var houseNPCs: List<NPCData>,
    var globalStats : List<StatData>,
    var playerStats: HashMap<String, List<StatData>>,
    val seed: String
) {
    companion object {
        fun fromHousingWorld(world: HousingWorld): HouseData {
            val houseData = HouseData(
                world.ownerUUID.toString(),
                world.houseUUID.toString(),
                world.name,
                world.description,
                world.size,
                world.cookies,
                world.timeCreated,
                ActionData.fromHashMap(world.eventActions),
                LocationData.fromLocation(world.spawn),
                world.scoreboard,
                NPCData.fromList(world.npCs),
                arrayListOf(),
                fromHashMap(world.statManager.playerStats),
                world.seed
            )
            return houseData
        }
    }
}


