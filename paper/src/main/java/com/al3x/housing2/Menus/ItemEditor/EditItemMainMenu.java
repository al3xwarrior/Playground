package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Listeners.HousingItems;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Enums.permissions.Permissions.ITEMS;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.*;

public class EditItemMainMenu extends Menu {
    public EditItemMainMenu(Player player) {
        super(player, "&7Edit Item", 9 * 6);
    }

    @Override
    public void initItems() {
        clearItems();

        Item customItem = Item.fromItemStack(player.getInventory().getItemInMainHand());
        ItemStack item = customItem.getBase();

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());

        addItem(13, item);

        //Rename Item
        addItem(28, ItemBuilder.create(Material.NAME_TAG)
                .name("&aRename Item")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new name for the item."));
            openChat(Main.getInstance(), item.getItemMeta().getDisplayName(), (newName) -> {
                ItemMeta meta = item.getItemMeta();
                meta.displayName(StringUtilsKt.housingStringFormatter(newName));
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                setupItems();
            });
        });

        //Edit Lore
        addItem(30, ItemBuilder.create(Material.BOOK)
                .name("&aEdit Lore")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            new EditLoreMenu(player).open();
        });

        //Edit Enchantments
        addItem(32, ItemBuilder.create(Material.ENCHANTED_BOOK)
                .name("&aEdit Enchantments")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            new EditEnchantmentMenu(player).open();
        });

        addItem(34, ItemBuilder.create(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name("&aEdit Actions")
                .description("Edit the actions which will be performed when the player uses this item.")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            new EditActionTypesMenu(player, customItem).open();
        });

        // Toggle Unbreakable
        addItem(37, ItemBuilder.create(item.getItemMeta().isUnbreakable() ? Material.LIME_DYE : Material.GRAY_DYE)
                .name("&aUnbreakable")
                .description("Toggle if the item is unbreakable or not.")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(!meta.isUnbreakable());
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            new EditItemMainMenu(player).open();
        });

        addItem(39, ItemBuilder.create(Material.BLACK_BANNER)
                .name("&aEdit Item Flags")
                .description("Toggle which flags are shown on the tooltip of the item.")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            new EditFlagMenu(player).open();
        });

        addItem(41, ItemBuilder.create(Material.GRASS_BLOCK)
                .name("&aSet Item Type")
                .description("Edit the material of the item.")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            new EnumMenu<>(Main.getInstance(), "Select Material", Material.values(), Material.HOPPER, player, house, this, (m) -> {
                ItemStack i = item.withType(m);
                ItemMeta meta = i.getItemMeta();
                if (meta.hasCustomModelData()) {
                    meta.setItemModel(new NamespacedKey("minecraft", "stick"));
                }
                i.setItemMeta(meta);
                player.getInventory().setItemInMainHand(i);
                setupItems();
            }).open();
        });

        addItem(43, ItemBuilder.create(Material.ANVIL)
                .name("&aSet Custom Model Data")
                .description("Edit the custom model data attribute on the item.")
                .lClick(EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the custom model data for the item."));
            ItemMeta meta = item.getItemMeta();
            openChat(Main.getInstance(), String.valueOf(meta.hasCustomModelData() ? meta.getCustomModelData() : 0), (newValue) -> {
                meta.setCustomModelData(Integer.valueOf(newValue));
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
            });
        });


    }
}
