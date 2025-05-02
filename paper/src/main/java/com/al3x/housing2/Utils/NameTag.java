package com.al3x.housing2.Utils;

import com.al3x.housing2.Main;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import me.tofaa.entitylib.ve.ViewerRule;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import net.kyori.adventure.text.Component;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class NameTag {
    public static HashMap<UUID, NameTag> nameTags = new HashMap<>();

    private WrapperEntity display;

    public NameTag(Player player, Component nameTag) {
        if (nameTags.containsKey(player.getUniqueId())) {
            nameTags.get(player.getUniqueId()).display.consumeEntityMeta(TextDisplayMeta.class, meta -> {
                meta.setText(nameTag);
            });
            this.display = nameTags.get(player.getUniqueId()).display;
            this.display.refresh();
            return;
        }
        this.display = new WrapperEntity(EntityTypes.TEXT_DISPLAY);
        TextDisplayMeta meta = (TextDisplayMeta) this.display.getEntityMeta();
        meta.setText(nameTag);
        meta.setUseDefaultBackground(true);
        meta.setShadow(false);
        meta.setBillboardConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);

        this.display.addViewer(player.getUniqueId());

        this.display.spawn(
                new Location(
                        player.getX(),
                        player.getY() + 2,
                        player.getZ(),
                        player.getYaw(),
                        player.getPitch()
                ));

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            World world = player.getWorld();
            for (Player online : world.getPlayers()) {
                if (online.getUniqueId() != player.getUniqueId()) {
                    if (!this.display.hasViewer(online.getUniqueId())) this.display.addViewer(online.getUniqueId());
                }
            }

            if (player.isOnline()) {
                this.display.teleport(new Location(
                        player.getX(),
                        player.getY() + 2,
                        player.getZ(),
                        player.getYaw(),
                        player.getPitch()
                ));
            } else {
                this.display.remove();
                nameTags.remove(player.getUniqueId());
            }
        }, 0L, 1L);

        nameTags.put(player.getUniqueId(), this);
    }

    public void stop(Player player) {
        this.display.remove();
        nameTags.remove(player.getUniqueId());
    }

    public static NameTag getNameTag(Player player) {
        if (nameTags.containsKey(player.getUniqueId())) {
            return nameTags.get(player.getUniqueId());
        }
        return null;
    }
}
