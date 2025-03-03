package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class EditFlagMenu extends Menu {
    public EditFlagMenu(Player player) {
        super(player, "&7Edit Flag", 9 * 4);
    }

    @Override
    public void setupItems() {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        boolean hideEnchants = meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
        addItem(10, ItemBuilder.create(hideEnchants ? Material.GRAY_DYE : Material.LIME_DYE)
                .name("&aEnchantments")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (hideEnchants) {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
            } else {
                if (meta.getEnchants().isEmpty()) { // Needs at least 1 enchantment to work
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                    item.removeEnchantment(Enchantment.UNBREAKING);
                } else {
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                }
            }
            if (!item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS) && !hideEnchants) {
                player.sendMessage(colorize("&cAn error occurred while trying to apply this flag!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            player.getInventory().setItemInMainHand(item);
            setupItems();
        });

        boolean hideAttributes = meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        addItem(11, ItemBuilder.create(hideAttributes ? Material.GRAY_DYE : Material.LIME_DYE)
                .name("&aAttributes")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (hideAttributes) {
                meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
            } else {
                if (meta.getAttributeModifiers() == null) { // Needs at least 1 attribute to work
                    meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(new NamespacedKey(".", "."), 0.0, AttributeModifier.Operation.ADD_NUMBER));
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);
                    meta.removeAttributeModifier(Attribute.ARMOR);
                } else {
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);
                }
            }
            if (!item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) && !hideAttributes) {
                player.sendMessage(colorize("&cAn error occurred while trying to apply this flag!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            player.getInventory().setItemInMainHand(item);
            setupItems();
        });

        boolean hideAdditional = meta.hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        addItem(12, ItemBuilder.create(hideAdditional ? Material.GRAY_DYE : Material.LIME_DYE)
                .name("&aAdditional")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (hideAdditional) {
                meta.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            } else {
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            }

            item.setItemMeta(meta);
            if (!item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP) && !hideAdditional) {
                player.sendMessage(colorize("&cAn error occurred while trying to apply this flag!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            player.getInventory().setItemInMainHand(item);
            setupItems();
        });

        boolean hideUnbreakable = meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        addItem(13, ItemBuilder.create(hideUnbreakable ? Material.GRAY_DYE : Material.LIME_DYE)
                .name("&aUnbreakable")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            if (hideUnbreakable) {
                meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            } else {
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }

            item.setItemMeta(meta);
            if (!item.getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE) && !hideUnbreakable) {
                player.sendMessage(colorize("&cAn error occurred while trying to apply this flag!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            player.getInventory().setItemInMainHand(item);
            setupItems();
        });

        //back
        addItem(31, ItemBuilder.create(Material.BARRIER)
                .name("&cBack")
                .build(), () -> {
            new EditItemMainMenu(player).open();
        });
    }
}
