package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Action.Actions.StatValue
import com.al3x.housing2.Enums.StatOperation

class StatActionData {
    companion object {
        fun fromStatValue(statValue: StatValue): MoreStatData {
            return MoreStatData(statValue.mode.name, statValue.literalValue, statValue.value1?.let { fromStatValue(it) }, statValue.value2?.let { fromStatValue(it) }, statValue.isExpression)
        }
    }
}

data class MoreStatData(
    val mode: String?,
    val literal: String?,
    val value1: MoreStatData?,
    val value2: MoreStatData?,
    val isExpression: Boolean,
    val isGlobal: Boolean = false
) {
    fun toStatValue(): StatValue {
        return StatValue(isGlobal, isExpression, literal, StatOperation.valueOf(mode.orEmpty()), value1?.toStatValue(), value2?.toStatValue())
    }
}