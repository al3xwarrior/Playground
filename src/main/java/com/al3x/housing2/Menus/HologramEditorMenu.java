package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.*;

public class HologramEditorMenu extends Menu{

    private Main main;
    private Player player;
    private Hologram hologram;

    private int add;

    public HologramEditorMenu(Main main, Player player, Hologram hologram) {
        super(player, "&7Edit Hologram", (hologram.getText().size() > 7) ? 45 : 36);
        this.add = (hologram.getText().size() > 7) ? 9 : 0;
        this.main = main;
        this.player = player;
        this.hologram = hologram;
        setupItems();
    }

    @Override
    public void open() {
        if (hologram.isDestroyed()) {
            return;
        }
        super.open();
    }

    int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    @Override
    public void setupItems() {
        addItem(31 + add, ItemBuilder.create(Material.BARRIER).name("&cClose").build(), () -> player.closeInventory());
        addItem(35 + add, ItemBuilder.create(Material.TNT).name("&aRemove Hologram").lClick(REMOVE_YELLOW).build(), () -> {
            hologram.remove();
            player.closeInventory();
        });
        addItem(34 + add, ItemBuilder.create(Material.END_ROD).name("&aLine Spacing").info("&eCurrent Value", "").info(null, hologram.getSpacing()).lClick(EDIT_ACTIONS).build(), () -> {
            player.sendMessage("§eEnter the new line spacing:");
            openChat(main, hologram.getSpacing() + "", message -> {
                try {
                    hologram.setSpacing(Double.parseDouble(message));
                    Bukkit.getScheduler().runTask(main, () -> new HologramEditorMenu(main, player, hologram).open());
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number!");
                }
            });
        });

        addItem(33 + add, ItemBuilder.create(Material.PISTON).name("&aStart Height").info("&eCurrent Value", "").info(null, hologram.getLocation().getY()).lClick(EDIT_ACTIONS).build(), () -> {
            player.sendMessage("§eEnter the new start height:");
            openChat(main, hologram.getLocation().getY() + "", message -> {
                try {
                    Location loc = hologram.getLocation().clone();
                    loc.setY(Double.parseDouble(message));
                    hologram.setLocation(loc);
                    Bukkit.getScheduler().runTask(main, () -> new HologramEditorMenu(main, player, hologram).open());
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number!");
                }
            });
        });

        addItem(27 + add, ItemBuilder.create(Material.NAME_TAG).name("&aAdd Line").lClick(ADD_YELLOW).build(), () -> {
            if (hologram.getText().size() >= 14) {
                player.sendMessage(colorize("&cYou can't add more than 14 lines!"));
                return;
            }

            player.sendMessage("§eEnter the new line:");
            openChat(main, message -> {
                hologram.addLine(message);
                Bukkit.getScheduler().runTask(main, () -> new HologramEditorMenu(main, player, hologram).open());
            });
        });

        for (int i = 0; i < hologram.getText().size(); i++) {
            int slot = slots[i];
            String line = hologram.getText().get(i);
            int finalI = i;
            addItem(slot, ItemBuilder.create(Material.OAK_SIGN).name(line).lClick(EDIT_YELLOW).rClick(REMOVE_YELLOW).shiftClick().build(), (e) -> {
                if (e.isShiftClick()) {
                    shiftLine(line, e.isRightClick());
                    return;
                }

                if (e.isRightClick()) {
                    hologram.removeLine(finalI);
                    Bukkit.getScheduler().runTask(main, () -> new HologramEditorMenu(main, player, hologram).open());
                    return;
                }

                player.sendMessage("§eEnter the new line:");
                openChat(main, line, message -> {
                    hologram.setLine(finalI, message);

                    Bukkit.getScheduler().runTask(main, () -> new HologramEditorMenu(main, player, hologram).open());
                });
            });
        }
    }

    public void shiftLine(String action, boolean forward) {
        List<String> actions = hologram.getText();
        int index = actions.indexOf(action);

        if (actions == null || actions.size() < 2) return;

        actions.remove(index);

        if (forward) {
            actions.add((index == actions.size()) ? 0 : index + 1, action);
        } else {
            actions.add((index == 0) ? actions.size() : index - 1, action);
        }

        setupItems();
    }
}
