package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Action.Actions.StatValue
import com.al3x.housing2.Action.StatInstance
import com.al3x.housing2.Enums.StatOperation

class StatActionData {
    companion object {
        fun fromStatValue(statValue: StatValue): MoreStatData {
            return MoreStatData(
                statValue.literalValue,
                statValue.statInstances,
                statValue.value?.let { fromStatValue(it) },
                statValue.isExpression,
                statValue.isGlobal
            )
        }
    }
}

data class MoreStatData(
    val literal: String?,
    val statInstances: List<StatInstance>?,
    val value: MoreStatData?,
    val isExpression: Boolean,
    val isGlobal: Boolean = false
) {
    fun toStatValue(): StatValue {
        return StatValue(
            isGlobal,
            isExpression,
            literal,
            value?.toStatValue(),
            statInstances
        )
    }
}