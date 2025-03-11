package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseSettingsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class ResourcePackMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    public ResourcePackMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Resource Pack"), 36);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void initItems() {
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cGo Back"))
                .build(), () -> new HouseSettingsMenu(main, player, house).open());
    }
}
