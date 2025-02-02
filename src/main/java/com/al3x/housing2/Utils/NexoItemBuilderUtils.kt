package com.al3x.housing2.Utils

import com.al3x.housing2.Main
import com.nexomc.nexo.items.ItemBuilder
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun ItemBuilder.setTypeZ(material: Material): ItemBuilder {
    this.setType(material)
    return this
}

fun makeItem(ib: ItemBuilder): ItemStack {
    val newItem = ItemBuilder(Material.STICK)
    newItem.nexoMeta(ib.nexoMeta)
    newItem.itemName(ib.itemName)
    newItem.setType(ib.type)
    newItem.setAmount(ib.amount)
    val id = LegacyComponentSerializer.legacyAmpersand().serialize(ib.itemName!!).replace(" ", "_").lowercase();
    if (Main.getInstance().items.containsKey(id)) newItem.customModelData(Main.getInstance().items[id]!!)
    val item = newItem.build()
    val nbtItem = NbtItemBuilder(item)
    nbtItem.setString("nexoItem", id)
    nbtItem.build()
    return item
}

fun setTexture(item: ItemStack): ItemStack {
    val newItem = ItemBuilder(item)
    val id = NbtItemBuilder(item).getString("nexoItem")
    newItem.customModelData(Main.getInstance().items[id]!!)
    return newItem.build()
}