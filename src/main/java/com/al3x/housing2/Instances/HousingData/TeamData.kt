package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Team

data class TeamData(
    var name: String,
    var prefix: String,
    var color: String,
    var displayName: String,
    var suffix: String,
    var friendlyFire: Boolean,
) {
    companion object {
        fun fromList(list: List<Team>): List<TeamData> {
            val teamData = mutableListOf<TeamData>()
            list.forEach {
                teamData.add(it.toData())
            }
            return teamData
        }

        fun toList(data: List<TeamData>): List<Team> {
            val teams = mutableListOf<Team>()
            data.forEach {
                teams.add(Team(it))
            }
            return teams
        }
    }
}