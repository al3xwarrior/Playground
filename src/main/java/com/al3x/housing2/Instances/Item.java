package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.StringToBase64;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;

/*
Should it be like skyblock where you can have abilities? or should it be like hypixel where you can just make items do things?
There is alot I can expand on here, but I think for now I will keep it like hypixel. With just a few more features.
 */
public class Item {
    private Main main;
    private ItemStack base;
    private HashMap<ClickType, List<Action>> actions;

    public Item(ItemStack base) {
        this.main = Main.getInstance();
        this.base = base;
        this.actions = new HashMap<>();
        List<ClickType> defaultClickTypes = List.of(ClickType.LEFT, ClickType.RIGHT);
        for (ClickType clickType : defaultClickTypes) {
            actions.put(clickType, List.of());
        }
    }

    public ItemStack getBase() {
        return base;
    }

    public HashMap<ClickType, List<Action>> getActions() {
        return actions;
    }

    public boolean hasActions() {
        for (List<Action> actionList : actions.values()) {
            if (!actionList.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    public static Item fromItemStack(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return null;
        }
        Item newItem = new Item(item);
        HashMap<ClickType, List<Action>> actions = newItem.getActions();
        Main main = Main.getInstance();
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey(main, "actions"))) {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            PersistentDataContainer actionsContainer = dataContainer.get(new NamespacedKey(main, "actions"), PersistentDataType.TAG_CONTAINER);
            if (actionsContainer != null) {
                for (ClickType clickType : actions.keySet()) {
                    String base64 = actionsContainer.get(new NamespacedKey(main, clickType.name()), PersistentDataType.STRING);
                    if (base64 != null) {
                        actions.put(clickType, StringToBase64.actionFromBase64(base64));
                    }
                }
            }
        }
        return newItem;
    }

    public ItemStack build() {
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            PersistentDataContainer newContainer = dataContainer.getAdapterContext().newPersistentDataContainer();
            NamespacedKey actionsKey = new NamespacedKey(main, "actions");
            for (ClickType clickType : actions.keySet()) {
                List<Action> actionList = actions.get(clickType);
                newContainer.set(new NamespacedKey(main, clickType.name()), PersistentDataType.STRING, StringToBase64.actionToBase64(actionList));
            }
            dataContainer.set(actionsKey, PersistentDataType.TAG_CONTAINER, newContainer);
        }
        base.setItemMeta(meta);
        return base;
    }
}
