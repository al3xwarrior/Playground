package com.al3x.housing2.Instances.HousingData

data class LocationData(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    companion object {
        fun fromLocation(location: org.bukkit.Location): LocationData {
            return LocationData(location.world?.name?:"null", location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }

    fun toLocation(): org.bukkit.Location {
        return org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}