package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Actions.ActionEnum
import com.al3x.housing2.Enums.EventType
import com.al3x.housing2.Instances.HousingData.HousingStat.Companion.fromHashMap
import com.al3x.housing2.Instances.HousingNPC
import com.al3x.housing2.Instances.HousingWorld
import com.al3x.housing2.Instances.Stat
import java.util.UUID

data class HouseData(
    val ownerID: String,
    val houseID: String,
    var houseName: String,
    var description: String,
    var size: Int,
//    var guests: Int,
    var cookies: Double,
    var timeCreated: Long,
    var eventActions: HashMap<EventType, List<Action>>,
    var spawnLocation: Location,
    var scoreboard: List<String>,
    var houseNPCs: List<HouseNPC>,
    var globalStats : List<HousingStat>,
    var playerStats: HashMap<String, List<HousingStat>>,
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
                Action.fromHashMap(world.eventActions),
                Location.fromLocation(world.spawn),
                world.scoreboard,
                HouseNPC.fromList(world.npCs),
                arrayListOf(),
                fromHashMap(world.statManager.playerStats),
                world.seed
            )
            return houseData
        }
    }
}

data class Location(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    companion object {
        fun fromLocation(location: org.bukkit.Location): Location {
            return Location(location.world?.name?:"null", location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }

    fun toLocation(): org.bukkit.Location {
        return org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}

data class HouseNPC(
    val npcID: Int,
    val npcUUID: String,
    val npcName: String,
    val npcType: String,
    val npcLocation: Location,
    val npcSkin: String,
    val actions: List<Action>,
    val lookAtPlayer: Boolean
) {
    companion object {
        fun fromList(npcList: List<HousingNPC>): List<HouseNPC> {
            val list = mutableListOf<HouseNPC>()
            npcList.forEach {
                list.add(HouseNPC(it.npcID, it.npcUUID.toString(), it.name, it.entityType.name, Location.fromLocation(it.location), "null", Action.fromList(it.actions), it.isLookAtPlayer))
            }
            return list
        }
    }
}

data class HousingStat(
    val id: String,
    val name: String,
    val value: Double
) {
    companion object {
        fun fromHashMap(statMap: HashMap<UUID, List<Stat>>): HashMap<String, List<HousingStat>> {
            val map = hashMapOf<String, List<HousingStat>>()
            statMap.forEach { (uuid, statList) ->
                val list = mutableListOf<HousingStat>()
                statList.forEach {
                    list.add(HousingStat(it.uuid.toString(), it.statName, it.statNum))
                }
                map[uuid.toString()] = list
            }
            return map
        }

        fun toHashMap(statMap: HashMap<String, List<HousingStat>>): HashMap<UUID, List<Stat>> {
            val map = hashMapOf<UUID, List<Stat>>()
            statMap.forEach { (uuid, statList) ->
                val list = mutableListOf<Stat>()
                statList.forEach {
                    list.add(Stat(UUID.fromString(it.id), it.name, it.value))
                }
                map[UUID.fromString(uuid)] = list
            }
            return map
        }
    }
}

data class Action(
    val action: String,
    val data: HashMap<String, Any>
) {
    companion object {
        fun fromHashMap(actionMap: HashMap<EventType, List<com.al3x.housing2.Actions.Action>>): HashMap<EventType, List<Action>> {
            val map = hashMapOf<EventType, List<Action>>()
            actionMap.forEach { (eventType, actionList) ->
                val list = mutableListOf<Action>()
                actionList.forEach {
                    list.add(Action(it.name, it.data()))
                }
                map[eventType] = list
            }
            return map
        }

        fun fromList(actionList: List<com.al3x.housing2.Actions.Action>): List<Action> {
            val list = mutableListOf<Action>()
            actionList.forEach {
                list.add(Action(it.name, it.data()))
            }
            return list
        }

        fun toList(actionList: List<Action>): List<com.al3x.housing2.Actions.Action> {
            val list = mutableListOf<com.al3x.housing2.Actions.Action>()
            actionList.forEach {
                list.add(ActionEnum.getActionByName(it.action)!!.getActionInstance(it.data))
            }
            return list
        }

    }
}
