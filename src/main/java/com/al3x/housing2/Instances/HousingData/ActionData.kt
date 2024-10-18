package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Action.ActionEnum
import com.al3x.housing2.Enums.EventType
import com.al3x.housing2.Instances.HousingWorld

data class ActionData(
    val action: String,
    val data: HashMap<String, Any>
) {
    companion object {
        fun fromHashMap(actionMap: HashMap<EventType, List<com.al3x.housing2.Action.Action>>, house: HousingWorld): HashMap<EventType, List<ActionData>> {
            val map = hashMapOf<EventType, List<ActionData>>()
            actionMap.forEach { (eventType, actionList) ->
                val list = mutableListOf<ActionData>()
                actionList.forEach {
                    list.add(ActionData(it.name, it.data()))
                }
                map[eventType] = list
            }
            return map
        }

        fun fromList(actionList: List<com.al3x.housing2.Action.Action>): List<ActionData> {
            val list = mutableListOf<ActionData>()
            actionList.forEach {
                list.add(ActionData(it.name, it.data()))
            }
            return list
        }
        fun toList(actionList: List<ActionData>): List<com.al3x.housing2.Action.Action> {
            val list = mutableListOf<com.al3x.housing2.Action.Action>()
            actionList.forEach {
                if (ActionEnum.getActionByName(it.action) == null) {
                    throw IllegalArgumentException("Action ${it.action} does not exist")
                }
                list.add(ActionEnum.getActionByName(it.action)!!.getActionInstance(it.data))
            }
            return list
        }

    }
}