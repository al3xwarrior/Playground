package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class EditHouseMenu extends Menu{

    private Main main;
    private Player player;
    private HousingWorld house;

    public EditHouseMenu(Main main, Player player, HousingWorld house) {
        super(player, "&cHousing Browser", 36);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {

        // Change Icon Item
        addItem(11, ItemBuilder.create(house.getIcon())
                .name(colorize("&eChange Icon"))
                .description("Change the icon used when displaying this house")
                .build(), () -> {
                    EnumMenu<Material> enumMenu = new EnumMenu<>(main, "Select Icon", Material.values(), Material.GRASS_BLOCK, player, house, this, (material) -> {
                        house.setIcon(material);
                        open();
                    });
                    enumMenu.open();
                }
        );

        // Dupe House Item
        addItem(13, ItemBuilder.create(Material.PRISMARINE)
                .name(colorize("&eDuplicate House"))
                .description(colorize("&cNot available yet :("))
                .build(), () -> {
                    player.sendMessage(colorize("&cThis feature is not available yet! Try asking Home Depot!"));
                }
        );

        // Delete house
        addItem(15, ItemBuilder.create(Material.RED_CONCRETE)
                .name(colorize("&cDelete House"))
                .description("Delete this house")
                .build(), () -> {
                    player.closeInventory();
                    main.getHousesManager().deleteHouse(house.getHouseUUID());
                }
        );

        // Go Back Arrow
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cGo Back"))
                .description("Go back to the house menu")
                .build(), () -> {
            new MyHousesMenu(main, player, player).open();
        });

    }
}
