package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.*;

public class HologramEditorMenu extends Menu {

    private Main main;
    private Player player;
    private Hologram hologram;
    private Menu menu;

    private int add;

    public HologramEditorMenu(Main main, Player player, Hologram hologram) {
        super(player, "&7Edit Hologram", (hologram.getText().size() > 7) ? 9*5 : 9*4);
        this.add = (hologram.getText().size() > 7) ? 9 : 0;
        this.main = main;
        this.player = player;
        this.hologram = hologram;
        setupItems();
    }

    public HologramEditorMenu(Main main, Player player, Hologram hologram, Menu menu) {
        super(player, "&7Edit Hologram", (hologram.getText().size() > 7) ? 9*5 : 9*4);
        this.add = (hologram.getText().size() > 7) ? 9 : 0;
        this.main = main;
        this.player = player;
        this.hologram = hologram;
        this.menu = menu;
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
    public void initItems() {
        if (menu == null) {
            addItem(31 + add, ItemBuilder.create(Material.BARRIER).name("&cClose").build(), () -> player.closeInventory());
        } else {
            addItem(31 + add, ItemBuilder.create(Material.ARROW).name("&aBack").build(), () -> menu.open());
        }
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

        addItem(28 + add, ItemBuilder.create(Material.BLACK_WOOL).name("<green>Display Settings")
                .description("Click to edit the display settings of the hologram. eg. scale, alignment, etc.\n\n&cOnly use this if you know what you're doing!")
                .lClick(EDIT_YELLOW).build(), () -> new HologramDisplaySettingsMenu(main, player, hologram).open());

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

    private static class HologramDisplaySettingsMenu extends Menu{
        Main main;
        Player player;
        Hologram hologram;
        HousingWorld house;
        public HologramDisplaySettingsMenu(Main main, Player player, Hologram hologram) {
            super(player, "&7Display Settings", 9*4);
            this.main = main;
            this.player = player;
            this.hologram = hologram;
            this.house = hologram.getHouse();
            setupItems();
        }

        @Override
        public void initItems() {
            addItem(31, ItemBuilder.create(Material.ARROW).name("&aBack").build(), () -> new HologramEditorMenu(main, player, hologram).open());
            addItem(10, ItemBuilder.create(Material.PAPER).description("Scale set for each direction.").name("&aScale").info("&eCurrent Value", "").info(null, hologram.getScale()).lClick(EDIT_YELLOW).build(), () -> {
                player.sendMessage("§eEnter the new scale (x,y,z):");
                openChat(main, hologram.getScale(), message -> {
                    try {
                        hologram.setScale(message);
                        Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInvalid number!");
                    }
                });
            });
            addItem(11, ItemBuilder.create(Material.ARROW).description("Alignment of the hologram.").name("&aAlignment").info("&eCurrent Value", "").info(null, hologram.getAlignment().name()).lClick(EDIT_YELLOW).build(), () -> {
                EnumMenu<TextDisplay.TextAlignment> menu = new EnumMenu<>(main, "&7Alignment", TextDisplay.TextAlignment.values(), Material.PAPER, player, house, this, (alignment) -> {
                    hologram.setAlignment(alignment);
                    Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                });
                menu.open();
            });
            addItem(12, ItemBuilder.create(Material.BLUE_BANNER).name("&aBillboard Type").description("How the hologram faces the player.").info("&eCurrent Value", "").info(null, hologram.getBillboard().name()).lClick(EDIT_YELLOW).build(), () -> {
                EnumMenu<TextDisplay.Billboard> menu = new EnumMenu<>(main, "&7Billboard Type", TextDisplay.Billboard.values(), Material.BLUE_BANNER, player, house, this, (billboardType) -> {
                    hologram.setBillboard(billboardType);
                    Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                });
                menu.open();
            });
            addItem(13, ItemBuilder.create(hologram.isShadow() ? Material.BLACK_WOOL : Material.WHITE_WOOL).name("&aShadow").description("Whether the hologram has a shadow.").info("&eCurrent Value", "").info(null, hologram.isShadow() + "").lClick(EDIT_YELLOW).build(), () -> {
                hologram.setShadow(!hologram.isShadow());
                Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
            });
            addItem(14, ItemBuilder.create(Material.GRASS_BLOCK).name("&aSee Through Blocks").description("Whether the hologram can be seen through blocks.").info("&eCurrent Value", "").info(null, hologram.isSeeThroughBlocks() + "").lClick(EDIT_YELLOW).build(), () -> {
                hologram.setSeeThroughBlocks(!hologram.isSeeThroughBlocks());
                Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
            });
            addItem(15, ItemBuilder.create(Material.GREEN_DYE).name("&aBackground Color").description("The color of the hologram's background.\nFirst two are alpha/opacity, the rest are normal HEX Codes RRGGBB").info("&eCurrent Value", "").info(null, "#" + Integer.toHexString(hologram.getBackgroundColor())).lClick(EDIT_YELLOW).build(), () -> {
                player.sendMessage("§eEnter the new background color:");
                openChat(main, "#" + Integer.toHexString(hologram.getBackgroundColor()), message -> {
                    if (message.equals("-1")) {
                        hologram.setBackgroundColor(-1);
                        Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                        return;
                    }
                    try {
                        if (message.charAt(0) == '#') {
                            message = message.substring(1);
                        }

                        long longValue = Long.parseLong(message, 16);

                        int alpha = (int) (longValue >> 24 & 0xFF);
                        int red = (int) (longValue >> 16 & 0xFF);
                        int green = (int) (longValue >> 8 & 0xFF);
                        int blue = (int) (longValue & 0xFF);

                        hologram.setBackgroundColor(Color.fromARGB(alpha, red, green, blue).asARGB());
                        Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInvalid number!");
                    }
                    Bukkit.getScheduler().runTask(main, () -> new HologramDisplaySettingsMenu(main, player, hologram).open());
                });
            });
        }
    }
}
