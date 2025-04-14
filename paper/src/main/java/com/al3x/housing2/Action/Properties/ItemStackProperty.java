package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ItemStackProperty extends ActionProperty<ItemStack> implements ActionProperty.PropertySerializer<ItemStack, String> {
    public ItemStackProperty(String id, String name, String description) {
        super(id, name, description, Material.ITEM_FRAME);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new ItemSelectMenu(player, menu, item -> setValue(item, player));
    }

    @Override
    protected String displayValue() {
        return getValue() == null ? "&cNot Set" : StackUtils.getDisplayName(getValue());
    }

    @Override
    public String serialize() {
        if (getValue() == null) {
            return null;
        }
        return Serialization.itemStackToBase64(getValue());
    }

    @Override
    public ItemStack deserialize(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Serialization.itemStackFromBase64(value.toString());
        } catch (IOException e) {
            Main.getInstance().getLogger().throwing("ItemStackProperty", "deserialize", e);
            return null;
        }
    }
}
