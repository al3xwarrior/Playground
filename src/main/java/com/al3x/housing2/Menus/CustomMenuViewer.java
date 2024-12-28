package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.entity.Player;

public class CustomMenuViewer extends Menu{
    CustomMenu customMenu;
    public CustomMenuViewer(Player player, CustomMenu customMenu) {
        super(player, customMenu.getTitle(), 9 * customMenu.getRows());
        this.customMenu = customMenu;
    }

    @Override
    public void setupItems() {
        Main main = Main.getInstance();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        for (int i = 0; i < 9 * customMenu.getRows(); i++) {
            int finalI = i;
            if (customMenu.getItems().get(i) == null) {
                continue;
            }
            addItem(i, customMenu.getItems().get(i).getFirst(), (e) -> {
                customMenu.getItems().get(finalI).getSecond().forEach(action -> action.execute(player, house, e));
            });
        }
    }
}
