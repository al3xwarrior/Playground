package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class HologramEditorMenu extends Menu{

    private Main main;
    private Player player;
    private Hologram hologram;

    public HologramEditorMenu(Main main, Player player, Hologram hologram) {
        super(player, "&7Edit Hologram", (hologram.getText().size() > 7) ? 45 : 36);
        this.main = main;
        this.player = player;
        this.hologram = hologram;
        setupItems();
    }

    int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21};

    @Override
    public void setupItems() {
        addItem(31, ItemBuilder.create(Material.BARRIER).name("&cClose").build(), () -> player.closeInventory());
        addItem(35, ItemBuilder.create(Material.TNT).name("&aRemove Hologram").description("&eClick to remove!").build(), () -> {
            hologram.remove();
            player.closeInventory();
        });

        addItem(27, ItemBuilder.create(Material.NAME_TAG).name("&aAdd Line").description("&eClick to add!").build(), () -> {
            if (hologram.getText().size() >= 10) {
                player.sendMessage(colorize("&cYou can't add more than 10 lines!"));
                return;
            }

            openChat(main, message -> {
                hologram.addLine(message);
                new HologramEditorMenu(main, player, hologram).open();
            });
        });

        for (int i = 0; i < hologram.getText().size(); i++) {
            int slot = slots[i];
            String line = hologram.getText().get(i);
            int finalI = i;
            addItem(slot, ItemBuilder.create(Material.OAK_SIGN).name(line).description("\n&eLeft Click to Edit!\n&eRight Click to Remove!").build(), () -> {
                openChat(main, message -> {
                    hologram.setLine(finalI, message);
                    new HologramEditorMenu(main, player, hologram).open();
                });
            }, () -> {
                hologram.removeLine(finalI);
                new HologramEditorMenu(main, player, hologram).open();
            });
        }
    }
}
