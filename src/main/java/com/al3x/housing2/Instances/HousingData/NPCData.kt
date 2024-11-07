package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.HousingNPC

data class NPCData(
    val npcID: Int,
    val npcUUID: String,
    val npcName: String,
    val npcType: String,
    val npcLocation: LocationData,
    val npcSkin: String,
    val actions: List<ActionData>,
    val lookAtPlayer: Boolean,
    val equipment: List<String>,
    val navigationType: String? = "null",
    val waypoints: List<LocationData>? = listOf(),
    val speed: Float? = 1.0f
) {

    companion object {
        fun fromList(npcList: List<HousingNPC>): List<NPCData> {
            val list = mutableListOf<NPCData>()
            npcList.forEach {
                list.add(
                    NPCData(
                        it.npcID, 
                        it.npcUUID.toString(),
                        it.name, 
                        it.entityType.name, 
                        LocationData.fromLocation(it.location),
                        "null", 
                        ActionData.fromList(it.actions),
                        it.isLookAtPlayer,
                        it.equipment,
                        it.navigationType.name,
                        LocationData.fromLocationList(it.waypoints),
                        it.speed.toFloat()
                    )
                )
            }
            return list
        }
    }
}