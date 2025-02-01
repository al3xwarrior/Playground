package com.al3x.housing2.Utils

import com.nexomc.nexo.items.ItemBuilder
import org.bukkit.Material

fun ItemBuilder.setTypeZ(material: Material): ItemBuilder {
    this.setType(material)
    return this
}

fun makeItem(ib: ItemBuilder): ItemBuilder {
    val newItem = ItemBuilder(Material.STICK)
    newItem.nexoMeta(ib.nexoMeta)
    newItem.setType(ib.type)
    newItem.setAmount(ib.amount)
//    var field = ib.javaClass.declaredFields.find {
//        it.name == "customModelData"
//    }!!
//    field.isAccessible = true
//    newItem.customModelData(field.getInt(ib))
    // I will figure this out later -Sin_ender
    return newItem
}