package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Enums.permissions.Permissions
import com.al3x.housing2.Instances.Group

data class GroupData(
    var name: String,
    var prefix: String,
    var color: String,
    var displayName: String,
    var suffix: String,
    var priority: Int,
    var permissions: HashMap<Permissions, Any>
) {
    companion object {
        fun fromList(list: List<Group>): List<GroupData> {
            val groupData = mutableListOf<GroupData>()
            list.forEach {
                groupData.add(it.toData())
            }
            return groupData
        }

        fun toList(data: List<GroupData>): List<Group> {
            val groups = mutableListOf<Group>()
            data.forEach {
                groups.add(Group(it))
            }
            return groups
        }
    }
}