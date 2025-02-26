package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Group
import com.al3x.housing2.Instances.HousingWorld
import com.al3x.housing2.Instances.Stat
import com.al3x.housing2.Instances.Team
import net.minecraft.stats.Stats
import java.time.Instant

// Per house player data, so groups, inventory, etc
data class PlayerData(
    var group: String?,
    var team: String?,
    var inventory: String?,
    var armor: String?,
    var enderchest: String?,
    var muted: Boolean,
    var muteExpiration: Instant?,
    var banned: Boolean,
    var banExpiration: Instant?,
    var stats: List<StatData>,
    @Transient var cacheStats: List<Stat> = ArrayList()
) {
    fun getGroupInstance(house: HousingWorld): Group {
        return house.groups.find { it != null && it.name != null && it.name == group } ?: Group("No Group")
    }
    fun getTeamInstance(house: HousingWorld): Team {
        return house.teams.find { it.name == team } ?: Team("No Team")
    }
}