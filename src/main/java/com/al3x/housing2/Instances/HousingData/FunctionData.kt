package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Instances.Function
import com.al3x.housing2.Main
import org.bukkit.Material
import java.util.*

data class FunctionData(
    val name: String,
    val id: String,
    val description: String,
    val ticks: Double?,
    val material: String,
    val global: Boolean = false,
    val actions: List<ActionData>
) {
    companion object {
        fun fromList(list: List<Function>): List<FunctionData> {
            val functionData = arrayListOf<FunctionData>()
            list.forEach {
                functionData.add(
                    FunctionData(
                        it.name,
                        it.id.toString(),
                        it.description,
                        it.ticks,
                        it.material.name,
                        it.isGlobal,
                        ActionData.fromList(it.actions)
                    )
                )
            }
            return functionData
        }

        fun toList(list: List<FunctionData>): List<Function> {
            val functions = arrayListOf<Function>()
            list.forEach {
                functions.add(
                    Function(
                        it.name,
                        UUID.fromString(it.id),
                        it.ticks,
                        Material.valueOf(it.material),
                        it.description,
                        ActionData.toList(it.actions),
                        it.global
                    )
                )
            }
            return functions
        }
    }
}
