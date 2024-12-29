package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HologramEditorMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class HologramInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public HologramInteractListener(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        if (!e.getRightClicked().isVisible()) e.setCancelled(true);
    }

    @EventHandler
    public void armorStandEditor(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();

        if (!(e.getRightClicked() instanceof ArmorStand)) return;
        if (!housesManager.playerIsInOwnHouse(player)) return;

        ArmorStand armorStand = (ArmorStand) e.getRightClicked();
        Hologram hologram = housesManager.getHouse(player).getHologramInstance(armorStand.getEntityId());

        if (hologram == null) return;

        new HologramEditorMenu(main, player, hologram).open();
    }

}
