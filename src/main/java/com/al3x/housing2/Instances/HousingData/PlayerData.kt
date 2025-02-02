package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Group
import com.al3x.housing2.Instances.HousingWorld
import com.al3x.housing2.Instances.Team
import net.minecraft.stats.Stats

// Per house player data, so groups, inventory, etc
data class PlayerData(
    var group: String?,
    var team: String?,
    var inventory: String?,
    var armor: String?,
    var enderchest: String?,
    var muted: Boolean,
    var banned: Boolean,
    var stats: List<StatData>,
) {
    fun getGroupInstance(house: HousingWorld): Group {
        return house.groups.find { it != null && it.name != null && it.name == group } ?: Group("No Group")
    }
    fun getTeamInstance(house: HousingWorld): Team {
        return house.teams.find { it.name == team } ?: Team("No Team")
    }
}