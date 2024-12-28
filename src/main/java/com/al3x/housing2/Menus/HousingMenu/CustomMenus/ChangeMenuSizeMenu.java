package com.al3x.housing2.Menus.HousingMenu.CustomMenus;

import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChangeMenuSizeMenu extends Menu {
    CustomMenu customMenu;
    public ChangeMenuSizeMenu(Player player, CustomMenu customMenu) {
        super(player, "Change Menu Size", 9 * 4);
        this.customMenu = customMenu;
    }

    @Override
    public void setupItems() {
        Main main = Main.getInstance();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        for (int i = 1; i <= 6; i++) {
            int finalI = i;
            addItem(i + 9, ItemBuilder.create(Material.BEACON)
                    .name("&a" + i + " Rows")
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                    .build(), (e) -> {
                customMenu.setRows(finalI);
                new CustomMenuEditMenu(player, customMenu).open();
            });
        }

        //back
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name("&cBack")
                .build(), (e) -> {
            new CustomMenuEditMenu(player, customMenu).open();
        });
    }
}
