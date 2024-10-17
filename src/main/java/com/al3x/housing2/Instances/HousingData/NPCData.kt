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
    val lookAtPlayer: Boolean
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
                        it.isLookAtPlayer
                    )
                )
            }
            return list
        }
    }
}