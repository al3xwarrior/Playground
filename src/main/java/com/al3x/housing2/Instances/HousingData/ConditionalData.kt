package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Condition.Condition
import com.al3x.housing2.Condition.ConditionEnum

data class ConditionData(
    val condition: String,
    val data: HashMap<String, Any>
) {
    companion object {
        fun fromList(conditions: List<Condition>): List<ConditionData> {
            val list = mutableListOf<ConditionData>()
            conditions.forEach {
                list.add(ConditionData(it.name, it.data()))
            }
            return list
        }

        fun toList(conditionList: List<ConditionData>): List<Condition> {
            val list = mutableListOf<Condition>()
            conditionList.forEach {
                if (ConditionEnum.getConditionByName(it.condition) == null) {
                    throw IllegalArgumentException("Condition ${it.condition} does not exist")
                }
                list.add(ConditionEnum.getConditionByName(it.condition)!!.getConditionInstance(it.data))
            }
            return list
        }
    }
}