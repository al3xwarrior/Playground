package com.al3x.housing2.Instances;

import com.al3x.housing2.Instances.HousingData.LayoutData;
import com.al3x.housing2.Utils.Serialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Layout {
    private String name;
    private String description;
    private Material icon;
    private List<ItemStack> inventory;
    private List<ItemStack> hotbar;
    private List<ItemStack> armor;
    private ItemStack offhand;

    public Layout(String name) {
        this.name = name;
        this.description = "&7Default Description";
        this.icon = Material.DARK_OAK_DOOR;
        this.inventory = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            this.inventory.add(null);
        }
        this.hotbar = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            this.hotbar.add(null);
        }
        this.armor = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.armor.add(null);
        }
        this.offhand = null;
    }

    public Layout(String name, String description, String icon, List<String> inventory, List<String> hotbar, List<String> armor, String offhand) {
        this.name = name;
        this.description = description;
        this.icon = Material.getMaterial(icon);
        this.inventory = inventory.stream().map(item -> {
            try {
                return Serialization.itemStackFromBase64(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        this.hotbar = hotbar.stream().map(item -> {
            try {
                return Serialization.itemStackFromBase64(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        this.armor = armor.stream().map(item -> {
            try {
                return Serialization.itemStackFromBase64(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        if (offhand != null) {
            try {
                this.offhand = Serialization.itemStackFromBase64(offhand);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    public List<ItemStack> getHotbar() {
        return hotbar;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    public ItemStack getOffhand() {
        return offhand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }


    public void setOffhand(ItemStack offhand) {
        this.offhand = offhand;
    }

    public void apply(Player player) {
        player.getInventory().clear();
        List<ItemStack> newInventory = new ArrayList<>(hotbar);
        newInventory.addAll(inventory);
        player.getInventory().setContents(newInventory.toArray(new ItemStack[0]));
        player.getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
        player.getInventory().setItemInOffHand(offhand);
    }

    public void save(List<ItemStack> guiInventory, List<ItemStack> guiHotbar, List<ItemStack> guiArmor, ItemStack guiOffhand) {
        inventory = new ArrayList<>(guiInventory);
        hotbar = new ArrayList<>(guiHotbar);
        armor = new ArrayList<>(guiArmor);
        offhand = guiOffhand;
    }

    /*
    New format for saving data that easier to understand, without having to go through 100 different classes :)
     */
    public LayoutData toData() {
        return new LayoutData(name, description, icon.name(), inventory.stream().map(item -> {
            try {
                return Serialization.itemStackToBase64(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList(), hotbar.stream().map(item -> {
            try {
                return Serialization.itemStackToBase64(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList(), armor.stream().map(item -> {
            try {
                return Serialization.itemStackToBase64(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList(), offhand != null ? Serialization.itemStackToBase64(offhand) : null);
    }
}
