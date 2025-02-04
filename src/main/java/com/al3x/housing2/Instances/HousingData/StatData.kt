package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Stat
import java.util.*
import kotlin.collections.HashMap

data class StatData(
    val name: String,
    val value: String //Can be a string, double, or int
) {
    //Current 3 types of values that can be stored in a StatData object
    fun ofString(): String {
        return value
    }

    fun ofDouble(): Double {
        return value.toDouble()
    }

    fun ofInt(): Int {
        return value.toInt()
    }

    companion object {
        fun fromHashMap(statMap: HashMap<UUID, List<Stat>>): HashMap<String, List<StatData>> {
            val map = hashMapOf<String, List<StatData>>()
            statMap.forEach { (uuid, statList) ->
                val list = mutableListOf<StatData>()
                statList.forEach {
                    if (it.value == null) return@forEach
                    list.add(StatData(it.statName, it.value))
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
                    list.add(Stat(it.name, it.value))
                }
                map[UUID.fromString(uuid)] = list
            }
            return map
        }

        fun fromList(statList: List<Stat>): List<StatData> {
            val list = mutableListOf<StatData>()
            statList.forEach {
                if (it.value == null) return@forEach
                list.add(StatData(it.statName, it.value))
            }
            return list
        }

        fun toList(statList: List<StatData>): List<Stat> {
            val list = mutableListOf<Stat>()
            statList.forEach {
                list.add(Stat(it.name, it.value))
            }
            return list
        }
    }
}