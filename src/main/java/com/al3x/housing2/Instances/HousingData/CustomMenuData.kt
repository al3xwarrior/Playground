package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.CustomMenu

data class CustomMenuData(
    var title: String?,
    var rows: Int,
    var items: List<CustomMenuItem?>
) {
    companion object {
        fun toList(customMenuData: List<CustomMenuData>): List<CustomMenu> {
            val list = mutableListOf<CustomMenu>()
            customMenuData.forEach {
                list.add(CustomMenu(it))
            }
            return list
        }

        fun fromList(customMenus: List<CustomMenu>): List<CustomMenuData> {
            val list = mutableListOf<CustomMenuData>()
            customMenus.forEach {
                list.add(it.toData())
            }
            return list
        }
    }
}

data class CustomMenuItem(
    var item: String?,
    var actions: List<ActionData>
)