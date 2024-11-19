package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.PvpSettings
import com.al3x.housing2.Instances.Regions

data class RegionData(
    val loaded: Boolean = true,
    val posA: LocationData,
    val posB: LocationData,
    val name: String = "",
    val exitActions: List<ActionData> = arrayListOf(),
    val enterActions: List<ActionData> = arrayListOf(),
    val pvpSettings: HashMap<PvpSettings, Boolean> = hashMapOf()
) {
    companion object {
        fun fromRegion(region: Regions): RegionData {
            val regionData = RegionData(
                region.isLoaded,
                LocationData.fromLocation(region.first),
                LocationData.fromLocation(region.second),
                region.name,
                ActionData.fromList(region.exitActions),
                ActionData.fromList(region.enterActions),
                region.pvpSettings
            )
            return regionData
        }

        fun fromList(regions: List<Regions>): List<RegionData> {
            val regionDataList = arrayListOf<RegionData>()
            regions.forEach { regionDataList.add(fromRegion(it)) }
            return regionDataList
        }

        fun toRegion(regionData: RegionData): Regions {
            val region = Regions(
                regionData.loaded,
                regionData.posA.toLocation(),
                regionData.posB.toLocation(),
                regionData.name,
                ActionData.toList(regionData.exitActions),
                ActionData.toList(regionData.enterActions),
                regionData.pvpSettings
            )
            return region
        }

        fun toList(regionDataList: List<RegionData>): List<Regions> {
            val regions = arrayListOf<Regions>()
            regionDataList.forEach { regions.add(toRegion(it)) }
            return regions
        }
    }
}
