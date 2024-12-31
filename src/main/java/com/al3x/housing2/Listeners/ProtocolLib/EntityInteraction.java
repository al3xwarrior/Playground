package com.al3x.housing2.Listeners.ProtocolLib;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HologramEditorMenu;
import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class EntityInteraction {
    public static void registerInteraction(HousesManager housesManager) {
        Main.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                // Handle packet
                WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());
                int id = wrapper.getTargetID();
                Player player = event.getPlayer();

                if (!housesManager.hasPermissionInHouse(player, Permissions.ITEM_HOLOGRAM)) return;
                Hologram hologram = housesManager.getHouse(player).getHologramInstance(id);
                if (hologram == null) return;
                if (hologram.isDestroyed()) return;

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> new HologramEditorMenu(Main.getInstance(), player, hologram).open());
            }
        });
    }
}
