package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Enums.EventType.*;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class CancelAction extends Action {

    public CancelAction() {
        super("Cancel Action");
    }

    @Override
    public String toString() {
        return "CancelAction";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.TNT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eCancel Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Apply the action to cancel this event."),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(FISH_CAUGHT, PLAYER_ENTER_PORTAL, PLAYER_BLOCK_BREAK, PLAYER_BLOCK_PLACE, PLAYER_DROP_ITEM, PLAYER_PICKUP_ITEM, PLAYER_TOGGLE_FLIGHT);
    }

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }
}
