package com.al3x.housing2.Menus.HousingMenu.layout;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ConfirmMenu;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ChangeArmorMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Layout layout;

    public ChangeArmorMenu(Main main, Player player, HousingWorld house, Layout layout) {
        super(player, colorize("&7Change Armor"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.layout = layout;
    }

    @Override
    public void setupItems() {
        //Helmet
        ItemStack helmet = layout.getArmor().get(3);
        addItem(13, helmet == null ? ItemBuilder.create(Material.CHAINMAIL_HELMET)
                .name(colorize("&aHelmet"))
                .description("Change the item that is in the Helmet slot")
                .build() : helmet, (e) -> {
            new ItemSelectMenu(player, this, (item) -> {
                layout.getArmor().set(3, item);
            }).open();
        });

        //Chestplate
        ItemStack chestplate = layout.getArmor().get(2);
        addItem(22, chestplate == null ? ItemBuilder.create(Material.CHAINMAIL_CHESTPLATE)
                .name(colorize("&aChestplate"))
                .description("Change the item that is in the Chestplate slot")
                .build() : chestplate, (e) -> {
            new ItemSelectMenu(player, this, (item) -> {
                layout.getArmor().set(2, item);
            }).open();
        });

        //Leggings
        ItemStack leggings = layout.getArmor().get(1);
        addItem(31, leggings == null ? ItemBuilder.create(Material.CHAINMAIL_LEGGINGS)
                .name(colorize("&aLeggings"))
                .description("Change the item that is in the Leggings slot")
                .build() : leggings, (e) -> {
            new ItemSelectMenu(player, this, (item) -> {
                layout.getArmor().set(1, item);
            }).open();
        });

        //Boots
        ItemStack boots = layout.getArmor().get(0);
        addItem(40, boots == null ? ItemBuilder.create(Material.CHAINMAIL_BOOTS)
                .name(colorize("&aBoots"))
                .description("Change the item that is in the Boots slot")
                .build() : boots, (e) -> {
            new ItemSelectMenu(player, this, (item) -> {
                layout.getArmor().set(0, item);
            }).open();
        });

        //Back
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Layouts Menu")
                .build(), (e) -> {
            new LayoutEditMenu(main, player, house, layout).open();
        });
    }
}
