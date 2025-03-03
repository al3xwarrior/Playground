package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.HousingNPC

data class NPCData(
    val npcID: Int,
    val npcUUID: String,
    val npcName: String,
    val npcType: String,
    val npcLocation: LocationData,
    val npcSkin: String?,
    val actions: List<ActionData>,
    val lookAtPlayer: Boolean,
    val equipment: List<String>,
    val navigationType: String? = "null",
    val waypoints: List<LocationData>? = listOf(),
    val hologramData: HologramData? = null,
    val speed: Float? = 1.0f,
    var canBeDamaged: Boolean = false,
    var respawnTime: Int = 20,
    var maxHealth: Double = 20.0,
    var minecraftAI: Boolean = false,
    var isBaby: Boolean = false,
    var eventActions: Map<String, List<ActionData>>? = hashMapOf(),
    var stats: List<StatData>? = listOf()
) {
    companion object {
        fun fromList(npcList: List<HousingNPC>): List<NPCData> {
            val list = mutableListOf<NPCData>()
            npcList.forEach {
                list.add(
                    NPCData(
                        it.internalID,
                        it.npcUUID.toString(),
                        it.name,
                        it.entityType.name,
                        LocationData.fromLocation(it.location),
                        it.skinUUID,
                        ActionData.fromList(it.actions),
                        it.isLookAtPlayer,
                        it.equipment,
                        it.navigationType.name,
                        LocationData.fromLocationList(it.waypoints),
                        if (it.hologram != null) HologramData.fromData(it.hologram!!) else null,
                        it.speed.toFloat(),
                        it.isCanBeDamaged,
                        it.respawnTime,
                        it.maxHealth,
                        it.minecraftAI,
                        it.isBaby,
                        it.eventActions.map { entry -> entry.key.name to ActionData.fromList(entry.value) }.toMap()
                    )
                )
            }
            return list
        }
    }
}