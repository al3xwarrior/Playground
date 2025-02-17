package com.al3x.housing2.Instances.HousingData

import org.bukkit.Bukkit
import org.bukkit.Location

data class LocationData(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    companion object {
        fun fromLocation(location: Location): LocationData {
            return LocationData(location.world?.name?:"null", location.x, location.y, location.z, location.yaw, location.pitch)
        }

        fun fromLocationList(waypoints: List<Location>): List<LocationData> {
            val list = mutableListOf<LocationData>()
            waypoints.forEach {
                list.add(fromLocation(it))
            }
            return list
        }

        @JvmStatic
        fun fromString(key: String): LocationData {
            val split = key.split(", ")
            if (split.size != 4) {
                return LocationData(split[0], 0.0, 0.0, 0.0, 0f, 0f)
            }
            try {
                return LocationData(split[0], split[1].toDouble(), split[2].toDouble(), split[3].toDouble(), 0f, 0f)
            } catch (e: Exception) {
                return LocationData(split[0], 0.0, 0.0, 0.0, 0f, 0f)
            }
        }
    }

    fun toLocation(): Location {
        return Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}