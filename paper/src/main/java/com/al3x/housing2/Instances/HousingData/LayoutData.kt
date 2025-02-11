package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Layout

data class LayoutData (
    var name: String,
    var icon: String,
    var description: String,
    var inventory: List<String>,
    var hotbar: List<String>,
    var armor: List<String>,
    var offhand: String?
) {
    companion object {
        fun fromList(layouts: List<Layout>): List<LayoutData> {
            val layoutDatas = mutableListOf<LayoutData>()
            for (layout in layouts) {
                layoutDatas.add(layout.toData())
            }
            return layoutDatas
        }

        fun toList(layoutDatas: List<LayoutData>): List<Layout> {
            val layouts = mutableListOf<Layout>()
            for (layoutData in layoutDatas) {
                layouts.add(Layout(layoutData.name, layoutData.icon, layoutData.description, layoutData.inventory, layoutData.hotbar, layoutData.armor, layoutData.offhand))
            }
            return layouts
        }
    }
}