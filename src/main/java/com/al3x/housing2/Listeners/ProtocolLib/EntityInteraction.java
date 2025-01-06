package com.al3x.housing2.Listeners.ProtocolLib;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HologramEditorMenu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.comphenix.packetwrapper.wrappers.play.serverbound.WrapperPlayClientUseEntity;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction.ATTACK;
import static com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction.INTERACT;

public class EntityInteraction {
    public static void registerInteraction(HousesManager housesManager) {
        Main.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                // Handle packet
                WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());
                int id = wrapper.getEntityId();
                Player player = event.getPlayer();

                if (!housesManager.hasPermissionInHouse(player, Permissions.ITEM_HOLOGRAM)) return;
                Hologram hologram = housesManager.getHouse(player).getHologramInstance(id);
                if (hologram == null) return;
                if (hologram.isDestroyed()) return;

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> new HologramEditorMenu(Main.getInstance(), player, hologram).open());
            }
        });

        Main.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Client.USE_ENTITY) {
            private static HashMap<UUID, Long> cooldown = new HashMap<>();

            @Override
            public void onPacketReceiving(PacketEvent event) {
                // Handle packet
                WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());
                int id = wrapper.getEntityId();
                Player player = event.getPlayer();

                if (!housesManager.hasPermissionInHouse(player, Permissions.ITEM_NPCS)) return;
                HousingNPC npc = housesManager.getHouse(player).getNPC(id);
                if (npc == null) return;

                if (cooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() - cooldown.get(player.getUniqueId()) < 20) return;
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                HousingWorld house = housesManager.getHouse(player);

                if (wrapper.getAction().getAction() != ATTACK) {
                    if (house.getOwnerUUID().equals(player.getUniqueId()) && player.isSneaking()) {
                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> new NPCMenu(Main.getInstance(), player, npc).open());
                        return;
                    }
                }

                npc.sendExecuteActions(house, player);
            }
        });
    }
}
