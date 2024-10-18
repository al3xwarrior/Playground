package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Stat
import java.util.*
import kotlin.collections.HashMap

data class StatData(
    val id: String,
    val name: String,
    val value: Double
) {
    companion object {
        fun fromHashMap(statMap: HashMap<UUID, List<Stat>>): HashMap<String, List<StatData>> {
            val map = hashMapOf<String, List<StatData>>()
            statMap.forEach { (uuid, statList) ->
                val list = mutableListOf<StatData>()
                statList.forEach {
                    list.add(StatData(it.uuid.toString(), it.statName, it.statNum))
                }
                map[uuid.toString()] = list
            }
            return map
        }

        fun toHashMap(statMap: HashMap<String, List<StatData>>): HashMap<UUID, List<Stat>> {
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

        fun fromList(statList: List<Stat>): List<StatData> {
            val list = mutableListOf<StatData>()
            statList.forEach {
                list.add(StatData(it.uuid.toString(), it.statName, it.statNum))
            }
            return list
        }

        fun toList(statList: List<StatData>): List<Stat> {
            val list = mutableListOf<Stat>()
            statList.forEach {
                list.add(Stat(UUID.fromString(it.id), it.name, it.value))
            }
            return list
        }
    }
}