package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Group
import com.al3x.housing2.Instances.HousingWorld

// Per house player data, so groups, inventory, etc
data class PlayerData(
    var group: String?,
    var inventory: String?,
    var armor: String?,
    var enderchest: String?,
) {
    fun getGroupInstance(house: HousingWorld): Group {
        return house.groups.find { it.name == group } ?: house.groups.find { it.name == house.defaultGroup }!!
    }
}