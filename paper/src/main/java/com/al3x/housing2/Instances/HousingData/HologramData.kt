package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Hologram
import com.al3x.housing2.Instances.HousingWorld
import org.bukkit.entity.Display
import org.bukkit.entity.TextDisplay

data class HologramData(
    var text: List<String>,
    var location: LocationData,
    var spacing: Double,
    var scale: String?,
    var alignment: TextDisplay.TextAlignment?,
    var billboard: Display.Billboard?,
    var shadow: Boolean,
    var seeThroughBlocks: Boolean,
    var backgroundColor: Int
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
                        hologram.spacing,
                        hologram.scale,
                        hologram.alignment,
                        hologram.billboard,
                        hologram.isShadow,
                        hologram.isSeeThroughBlocks,
                        hologram.backgroundColor
                    )
                )
            }
            return hologramDatas
        }

        fun fromData(hologram: Hologram): HologramData {
            return HologramData(
                hologram.text,
                LocationData.fromLocation(hologram.location),
                hologram.spacing,
                hologram.scale,
                hologram.alignment,
                hologram.billboard,
                hologram.isShadow,
                hologram.isSeeThroughBlocks,
                hologram.backgroundColor
            )
        }

        fun toData(hologramData: HologramData): Hologram {
            return Hologram(
                null,
                hologramData.text,
                hologramData.location.toLocation(),
                hologramData.spacing,
                hologramData.scale,
                hologramData.alignment,
                hologramData.billboard,
                hologramData.shadow,
                hologramData.seeThroughBlocks,
                hologramData.backgroundColor
            )
        }

        fun toList(hologramDatas: List<HologramData>, house: HousingWorld): List<Hologram> {
            val holograms = mutableListOf<Hologram>();
            for (hologramData in hologramDatas) {
                holograms.add(
                    Hologram(
                        house,
                        hologramData.text,
                        hologramData.location.toLocation(),
                        hologramData.spacing,
                        hologramData.scale,
                        hologramData.alignment,
                        hologramData.billboard,
                        hologramData.shadow,
                        hologramData.seeThroughBlocks,
                        hologramData.backgroundColor
                    )
                )
            }
            return holograms
        }
    }
}