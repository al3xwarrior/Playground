package com.al3x.housing2.Menus;

import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class ConfirmMenu extends Menu{
    Menu back;
    Runnable onConfirm;
    public ConfirmMenu(Player player, Menu back, Runnable onConfirm) {
        super(player, "Confirm", 3 * 9);
        this.back = back;
        this.onConfirm = onConfirm;
    }

    @Override
    public void setupItems() {
        addItem(11, ItemBuilder.create(Material.GREEN_TERRACOTTA)
                .name(colorize("&aConfirm"))
                .build(), (e) -> {
            onConfirm.run();
            back.open();
        });

        addItem(15, ItemBuilder.create(Material.RED_TERRACOTTA)
                .name(colorize("&cCancel"))
                .build(), (e) -> {
            back.open();
        });
    }
}
