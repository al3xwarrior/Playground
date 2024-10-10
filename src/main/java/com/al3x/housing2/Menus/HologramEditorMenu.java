package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionMenus.ChatActionMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class HologramEditorMenu extends Menu{

    private Main main;
    private Player player;
    private HousingNPC housingNPC;

    public HologramEditorMenu(Main main, Player player, HousingNPC housingNPC) {
        super(player, colorize("&6Edit Hologram"), 45);
        this.main = main;
        this.player = player;
        this.housingNPC = housingNPC;
    }

    @Override
    public void setupItems() {

        int[] validLines = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21};

        List<String> lines = housingNPC.getHologramLines();

        for (int i = 0; i < (Math.min(lines.size(), 10)); i++) {
            ItemStack line = new ItemStack(Material.PAPER);
            ItemMeta lineMeta = line.getItemMeta();
            lineMeta.setDisplayName(lines.get(i));
            int finalI = i;
            addItem(validLines[i], line, () -> {
                Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onPlayerChat(AsyncPlayerChatEvent e) {
                        e.setCancelled(true);
                        if (e.getPlayer().equals(player)) {
                            String newMessage = e.getMessage();
                            lines.set(finalI, newMessage);
                            housingNPC.setHologramLines(lines);
                            player.sendMessage(colorize("&aMessage set to: " + newMessage));

                            // Unregister this listener after capturing the message
                            AsyncPlayerChatEvent.getHandlerList().unregister(this);

                            // Reopen the ChatActionMenu
                            Bukkit.getScheduler().runTaskLater(main, () -> new HologramEditorMenu(main, player, housingNPC).open(), 1L); // Delay slightly to allow chat event to complete
                        }
                    }
                }, main);
            });
        }

    }
}
