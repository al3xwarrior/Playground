package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Hologram
import com.al3x.housing2.Instances.HousingWorld

data class HologramData(
    var text: List<String>,
    var location: LocationData,
    var spacing: Double
) {
    companion object {
        fun fromList(holograms: List<Hologram>): List<HologramData> {
            val hologramDatas = mutableListOf<HologramData>();
            for (hologram in holograms) {
                if (hologram.isDestroyed) continue
                hologramDatas.add(
                    HologramData(
                        hologram.text,
                        LocationData.fromLocation(hologram.location),
                        hologram.spacing
                    )
                )
            }
            return hologramDatas
        }

        fun toList(hologramDatas: List<HologramData>, house: HousingWorld): List<Hologram> {
            val holograms = mutableListOf<Hologram>();
            for (hologramData in hologramDatas) {
                holograms.add(
                    Hologram(
                        house,
                        hologramData.text,
                        hologramData.location.toLocation(),
                        hologramData.spacing
                    )
                )
            }
            return holograms
        }
    }
}