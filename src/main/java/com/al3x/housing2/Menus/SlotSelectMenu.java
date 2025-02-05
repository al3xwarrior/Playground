package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class SlotSelectMenu extends Menu {
    Main main;
    Consumer<Integer> slotRunnable;
    Menu backMenu;
    boolean others;
    public SlotSelectMenu(Player player, Main main, Menu backMenu, Consumer<Integer> slotRunnable) {
        super(player, "&7Select Inventory Slot", 9 * 6);
        this.main = main;
        this.slotRunnable = slotRunnable;
        this.backMenu = backMenu;
        this.others = false;
    }

    public SlotSelectMenu(Player player, Main main, Menu backMenu, boolean others, Consumer<Integer> slotRunnable) {
        super(player, "&7Select Inventory Slot", 9 * 6);
        this.main = main;
        this.slotRunnable = slotRunnable;
        this.backMenu = backMenu;
        this.others = others;
    }

    @Override
    public void setupItems() {
        //Helmet
        addItem(1, ItemBuilder.create(Material.CHAINMAIL_HELMET).name("&aHelmet")
                .description("&8Manual Slot 103")
                .build(), () -> {
            slotRunnable.accept(103);
            backMenu.open();
        });

        //Chestplate
        addItem(2, ItemBuilder.create(Material.CHAINMAIL_CHESTPLATE).name("&aChestplate")
                .description("&8Manual Slot 102")
                .build(), () -> {
            slotRunnable.accept(102);
            backMenu.open();
        });

        //Leggings
        addItem(3, ItemBuilder.create(Material.CHAINMAIL_LEGGINGS).name("&aLeggings")
                .description("&8Manual Slot 101")
                .build(), () -> {
            slotRunnable.accept(101);
            backMenu.open();
        });

        //Boots
        addItem(4, ItemBuilder.create(Material.CHAINMAIL_BOOTS).name("&aBoots")
                .description("&8Manual Slot 100")
                .build(), () -> {
            slotRunnable.accept(100);
            backMenu.open();
        });

        if (others) {
            //First Available Slot (-1)
            addItem(6, ItemBuilder.create(Material.WHITE_STAINED_GLASS).name("&aFirst Available Slot")
                    .description("&8Manual Slot -1")
                    .build(), () -> {
                slotRunnable.accept(-1);
                backMenu.open();
            });

            //Main Hand (-2)
            addItem(7, ItemBuilder.create(Material.WOODEN_SWORD).name("&aMain Hand")
                    .description("&8Manual Slot -2")
                    .build(), () -> {
                slotRunnable.accept(-2);
                backMenu.open();
            });

            //Off Hand (-3)
            addItem(8, ItemBuilder.create(Material.SHIELD).name("&aOff Hand")
                    .description("&8Manual Slot -3")
                    .build(), () -> {
                slotRunnable.accept(-3);
                backMenu.open();
            });
        }

        //Inventory
        for (int i = 0; i < 3*9; i++) {
            int finalI = i;
            addItem(i + 9, ItemBuilder.create(Material.GRAY_STAINED_GLASS_PANE).name("&aInventory Slot " + (i + 1))
                    .description("&8Manual Slot " + (i + 9))
                    .build(), () -> {
                slotRunnable.accept(finalI + 9);
                backMenu.open();
            });
        }

        //Hotbar
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            addItem(i + 4*9, ItemBuilder.create(Material.WHITE_STAINED_GLASS).name("&aHotbar Slot " + (i + 1))
                    .description("&8Manual Slot " + i)
                    .build(), () -> {
                slotRunnable.accept(finalI);
                backMenu.open();
            });
        }

        //Manual Input
        addItem(53, ItemBuilder.create(Material.PAPER).name("&aManual Input")
                .description("&8Enter a manual slot number")
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the manual slot number:"));
            openChat(main, "", (s) -> {
                try {
                    int slot = Integer.parseInt(s);
                    slotRunnable.accept(slot);
                    backMenu.open();
                } catch (NumberFormatException ex) {
                    player.sendMessage(colorize("&cInvalid number!"));
                }
            });
        });

        //Close
        addItem(49, ItemBuilder.create(Material.ARROW).name("&cClose")
                .description("&8Close this menu")
                .build(), () -> {
            player.closeInventory();
            backMenu.open();
        });
    }
}
