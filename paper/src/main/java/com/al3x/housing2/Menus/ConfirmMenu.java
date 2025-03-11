package com.al3x.housing2.Menus;

import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class ConfirmMenu extends Menu{
    Menu back;
    Runnable onConfirm;
    boolean backOnConfirm = true;

    public ConfirmMenu(Player player, Menu back, Runnable onConfirm) {
        super(player, "Confirm", 3 * 9);
        this.back = back;
        this.onConfirm = onConfirm;
    }

    public ConfirmMenu(Player player, Menu back, String title, Runnable onConfirm) {
        super(player, title, 3 * 9);
        this.back = back;
        this.onConfirm = onConfirm;
    }

    public ConfirmMenu(Player player, Menu back, String title, Runnable onConfirm, boolean backOnConfirm) {
        super(player, title, 3 * 9);
        this.back = back;
        this.onConfirm = onConfirm;
        this.backOnConfirm = backOnConfirm;
    }

    @Override
    public void initItems() {
        addItem(11, ItemBuilder.create(Material.GREEN_TERRACOTTA)
                .name(colorize("&aConfirm"))
                .build(), () -> {
            onConfirm.run();
            if (backOnConfirm) back.open();
        });

        addItem(15, ItemBuilder.create(Material.RED_TERRACOTTA)
                .name(colorize("&cCancel"))
                .build(), () -> {
            back.open();
        });
    }
}
