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
    }

    fun toLocation(): Location {
        return Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}