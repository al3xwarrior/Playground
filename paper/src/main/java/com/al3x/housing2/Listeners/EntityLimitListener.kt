package com.al3x.housing2.Listeners;

import com.al3x.housing2.Commands.StaffAlerts.isStaffAlerts
import com.al3x.housing2.Main
import com.al3x.housing2.Utils.Color.colorize
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntitySpawnEvent
import java.util.*

class EntityLimitListener : Listener {

    private val staffAlerts = mutableMapOf<UUID, Boolean>()
    private val limit = Main.getInstance().config.getInt("entityLimit")

    private fun alertStaff(world: World) {
        if (world.entities.size > limit) {
            Bukkit.getOnlinePlayers().forEach { player ->
                val house = Main.getInstance().getHousesManager().getHouse(world)
                if (player.hasPermission("housing2.admin") && isStaffAlerts(player)) {
                    player.sendMessage(colorize("&cEntity limit reached in ${house.name} &7(${house.ownerName})"))
                }
            }
        }
    }

    @EventHandler
    fun onCreatureSpawn(e: CreatureSpawnEvent) {
//        val reason = e.spawnReason;
////        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL) {
////            e.setCancelled(true); // I dont "THINK" natural spawns are needed
////            return;
////        }
//
//        val world = e.entity.world
//
//        if (world.entities.size >= limit) {
//            // Events that are either small and shouldnt be denied or part of Housing2 or another plugin
//            // CURED, CUSTOM, ENDER_PEARL
//            if (reason in listOf(
//                    CreatureSpawnEvent.SpawnReason.SPAWNER,
//                    CreatureSpawnEvent.SpawnReason.SPAWNER_EGG,
//                    CreatureSpawnEvent.SpawnReason.TRIAL_SPAWNER,
//                    CreatureSpawnEvent.SpawnReason.SILVERFISH_BLOCK,
//                    CreatureSpawnEvent.SpawnReason.TRAP
//                )
//            ) {
//                e.isCancelled = true
//            }
//
//            alertStaff(world);
//        }
    }

    @EventHandler
    fun onEntitySpawn(e: EntitySpawnEvent) {
        val world = e.entity.world
        if (world.entities.size >= limit) {
            e.isCancelled = true
            alertStaff(world)
        }
    }
//
//    @EventHandler
//    fun dropItem(e: PlayerDropItemEvent) {
//        val world = e.player.world
//        if (world.entities.size >= limit) {
//            e.isCancelled = true
//            alertStaff(world)
//        }
//    }
//
//    @EventHandler
//    fun blockDrop(e: BlockBreakEvent) {
//        val world = e.player.world
//        if (world.entities.size >= limit) {
//            e.isDropItems = false
//            alertStaff(world)
//        }
//    }
//
//    @EventHandler
//    fun blockDispenseItem(e: BlockDispenseEvent) {
//        val world = e.block.world
//        if (world.entities.size >= limit) {
//            e.isCancelled = true
//            alertStaff(world)
//        }
//    }
}
