package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.NbtItemBuilder;
import com.al3x.housing2.Utils.StringToBase64;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.event.inventory.ClickType.CONTROL_DROP;

/*
Should it be like skyblock where you can have abilities? or should it be like hypixel where you can just make items do things?
There is alot I can expand on here, but I think for now I will keep it like hypixel. With just a few more features.
 */
public class Item {
    private Main main;
    private ItemStack base;
    private HashMap<ClickType, ArrayList<Action>> actions;

    public Item(ItemStack base) {
        this.main = Main.getInstance();
        this.base = base;
        this.actions = new HashMap<>();
        List<ClickType> defaultClickTypes = List.of(ClickType.LEFT, ClickType.RIGHT, ClickType.DROP, CONTROL_DROP, ClickType.SWAP_OFFHAND);
        for (ClickType clickType : defaultClickTypes) {
            actions.put(clickType, new ArrayList<>());
        }
    }

    public ItemStack getBase() {
        return base;
    }

    public HashMap<ClickType, ArrayList<Action>> getActions() {
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


    public static Item fromItemStack(ItemStack item, HousingWorld house) {
        if (item == null || item.getItemMeta() == null) {
            return null;
        }
        Item newItem = new Item(item);
        HashMap<ClickType, ArrayList<Action>> actions = newItem.getActions();
        NbtItemBuilder nbtItemBuilder = new NbtItemBuilder(item);
        NbtItemBuilder actionsBuilder = nbtItemBuilder.getChild("actions");
        if (actionsBuilder != null) {
            for (ClickType clickType : actions.keySet()) {
                String base64 = actionsBuilder.getString(clickType.name().toLowerCase());
                if (base64 != null) {
                    actions.put(clickType, StringToBase64.actionsFromBase64(base64, house));
                }
            }
        }
        return newItem;
    }

    public ItemStack build() {
        NbtItemBuilder nbtItemBuilder = new NbtItemBuilder(base);
        NbtItemBuilder actionsBuilder = nbtItemBuilder.addChild("actions");
        for (ClickType clickType : actions.keySet()) {
            List<Action> actionList = actions.get(clickType);
            actionsBuilder.setString(clickType.name().toLowerCase(), StringToBase64.actionsToBase64(actionList));
        }
        nbtItemBuilder.build();
        return base;
    }

    public static int getItemNBTSize(ItemStack item) {
        if (item == null || item.getType().isAir()) return 0;

        // Convert ItemStack to a Map
        Map<String, Object> itemData = item.serialize();

        // Convert to JSON and measure size
        String json = Main.getInstance().getGson().toJson(itemData);
        return json.getBytes(StandardCharsets.UTF_8).length; // Get byte size
    }
}
