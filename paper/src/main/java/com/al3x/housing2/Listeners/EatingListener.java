package com.al3x.housing2.Listeners;

import com.al3x.housing2.Main;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class EatingListener implements Listener {

    private static ArrayList<UUID> playersEating = new ArrayList<>();

    // weird listener to have right? Originally it was made for sword blocking but
    // decided to also include eating conditions cause it has the same idea

    public static boolean isPlayerEating(Player player) {
        return playersEating.contains(player.getUniqueId());
    }

    public EatingListener(Main main) {

        // Packet for listening to when a player stops eating
        // dont ask me why the BlockDig packet is used for this
        // but i spent hours on another project finding out that
        // this is indeed the way to do it!
        main.getProtocolManager().addPacketListener(new PacketAdapter(
                main,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.BLOCK_DIG
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                playersEating.remove(event.getPlayer().getUniqueId());
            }
        });
    }

    @EventHandler
    public void onBlock(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        // If the player is holding a consumable item
        if (isEatable(item)) {
            playersEating.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onFinishEating(PlayerItemConsumeEvent e) {
        playersEating.remove(e.getPlayer().getUniqueId());
    }

    private boolean isEatable(ItemStack item) {

        if (item.getType().isEdible()) return true;

        net.minecraft.world.item.ItemStack craftItemStack = CraftItemStack.asNMSCopy(item);
        for (var typedDataComponent : craftItemStack.getComponents()) {
            if (typedDataComponent.toString().contains("minecraft:consumable")) return true;
        }

        return false;
    }

}