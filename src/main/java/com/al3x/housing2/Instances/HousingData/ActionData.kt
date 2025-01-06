package com.al3x.housing2.Instances.HousingData

import com.al3x.housing2.Action.Action
import com.al3x.housing2.Action.ActionEnum
import com.al3x.housing2.Enums.EventType
import com.al3x.housing2.Instances.HousingWorld

data class ActionData(
    val action: String,
    val data: HashMap<String, Any>
) {
    companion object {
        fun fromHashMap(actionMap: HashMap<EventType, List<Action>>, house: HousingWorld): HashMap<EventType, List<ActionData>> {
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

        fun fromList(actionList: List<Action>): List<ActionData> {
            val list = mutableListOf<ActionData>()
            actionList.forEach {
                list.add(ActionData(it.name, it.data()))
            }
            return list
        }
        fun toList(actionList: List<ActionData>): List<Action> {
            val list = mutableListOf<Action>()
            actionList.forEach {
                if (ActionEnum.getActionByName(it.action) == null) {
                    throw IllegalArgumentException("Action ${it.action} does not exist")
                }
                list.add(ActionEnum.getActionByName(it.action)!!.getActionInstance(it.data))
            }
            return list
        }

        fun toData(Action: Action): ActionData {
            return ActionData(Action.name, Action.data())
        }

        fun fromData(data: ActionData): Action {
            if (ActionEnum.getActionByName(data.action) == null) {
                throw IllegalArgumentException("Action ${data.action} does not exist")
            }
            return ActionEnum.getActionByName(data.action)!!.getActionInstance(data.data)
        }

    }
}