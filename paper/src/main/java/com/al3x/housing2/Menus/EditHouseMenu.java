package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class EditHouseMenu extends Menu{

    private Main main;
    private Player player;
    private HouseData houseData;
    private HousingWorld house;

    public EditHouseMenu(Main main, Player player, HousingWorld house, HouseData houseData) {
        super(player, "&cHousing Browser", 36);
        this.main = main;
        this.player = player;
        this.house = house;
        this.houseData = houseData;
        setupItems();
    }

    @Override
    public void setupItems() {
        // Change Icon Item
        String hIcon = houseData.getIcon() == null ? "OAK_DOOR" : houseData.getIcon();
        ItemStack item;
        if (Material.getMaterial(hIcon) != null) {
            item = new ItemStack(Material.getMaterial(hIcon));
        } else {
            try {
                item = Serialization.itemStackFromBase64(hIcon);
            } catch (IOException e) {
                item = new ItemStack(Material.OAK_DOOR);
            }
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorize("&eChange Icon"));
        meta.setLore(Arrays.asList("§7Change the icon used when displaying this house", "", "§eLeft Click to change Material", "§eMiddle Click to select item"));
        item.setItemMeta(meta);
        addItem(11, item, (e) -> {
                    if (e.isLeftClick()) {
                        EnumMenu<Material> enumMenu = new EnumMenu<>(main, "&aSelect Icon", Material.values(), Material.GRASS_BLOCK, player, house, this, (material) -> {
                            if (material == null) {
                                player.sendMessage(colorize("&cSomething went wrong..."));
                                return;
                            }
                            houseData.setIcon(material.name());
                            if (house != null) {
                                house.setIcon(material);
                            }
                            open();
                        });
                        enumMenu.open();
                    }

                    if (e.getClick() == ClickType.MIDDLE) {
                        new ItemSelectMenu(player, this, (itemStack) -> {
                            if (itemStack == null) {
                                player.sendMessage(colorize("&cSomething went wrong..."));
                                return;
                            }
                            houseData.setIcon(Serialization.itemStackToBase64(itemStack));
                            if (house != null) {
                                house.setIcon(itemStack);
                            }
                            open();
                        }).open();
                    }
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
                    new ConfirmMenu(player, this, "&cYou want to delete your house?", () -> {
                        main.getHousesManager().deleteHouse(UUID.fromString(houseData.getHouseID()));
                    }).open();
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
