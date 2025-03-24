package com.al3x.housing2.Listeners

import com.al3x.housing2.Main
import com.al3x.housing2.Utils.Color.colorize
import com.al3x.housing2.Utils.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object TrashCanListener : Listener {

    private val head: ItemStack = ItemBuilder.create(Material.PLAYER_HEAD)
        .skullTexture("441f4edbc68c9061355242bd73effc9299a3252b9f11e82b5f1ec7b3b6ac0")
        .build()

    private val easterEggHead: ItemStack = ItemBuilder.create(Material.PLAYER_HEAD)
        .skullTexture("3cea77e0532b330f6d554eb7c0f4274bf0365c35eab2ed7f39997ad1370942bb")
        .build()

    private fun spawnArmorStands(location: Location) {
        val top: ArmorStand =
            location.world.spawnEntity(location.add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND) as ArmorStand
        top.setGravity(false)
        top.isVisible = false
        top.isSmall = true
        top.equipment.helmet = if (Math.random() < 0.01) easterEggHead else head

        val bottom: ArmorStand =
            location.world.spawnEntity(location.add(0.5, -0.7, 0.5), EntityType.ARMOR_STAND) as ArmorStand
        bottom.setGravity(false)
        bottom.isVisible = false
        bottom.isSmall = true
        bottom.equipment.helmet = head
    }

    private fun despawnArmorStands(location: Location) {
        location.world.getEntitiesByClass(ArmorStand::class.java)
            .filter { it.location.distance(location) < 1.0 }
            .forEach { it.remove() }
    }

    fun initTrashCans(locations: List<Location>) {
        locations.forEach { location ->
//            location.block.type = Material.BARRIER
            spawnArmorStands(location)
        }
    }

    @EventHandler
    fun clickTrashCan(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return

        val player = e.player
        val block = e.clickedBlock ?: return
        val house = Main.getInstance().getHousesManager().getHouse(player.world) ?: return

        val clickedOnTrashCan = house.isTrashCanAtLocation(block.location) && block.type == Material.BARRIER
        if (clickedOnTrashCan) player.openInventory(Bukkit.createInventory(player, 27, colorize("&aTrash Can")))
    }

    //    TODO: Give trash can item custom data
    @EventHandler
    fun placeTrashCan(e: BlockPlaceEvent) {
        val player = e.player
        val item = e.itemInHand

        if (!item.hasItemMeta()) return

        val name = item.itemMeta.customName() ?: return

        if (name.equals("§aTrash Can") && item.type == Material.CAULDRON) {
            val house = Main.getInstance().getHousesManager().getHouse(player.world) ?: return

            e.blockPlaced.type = Material.BARRIER
            house.addTrashCan(e.blockPlaced.location)
            spawnArmorStands(e.blockPlaced.location)
        }
    }

    @EventHandler
    fun breakTrashCan(e: BlockBreakEvent) {
        val player = e.player
        val block = e.block
        val house = Main.getInstance().getHousesManager().getHouse(player.world) ?: return

        if (house.isTrashCanAtLocation(block.location)) {
            house.removeTrashCan(block.location)
            despawnArmorStands(block.location)
        }
    }
}
