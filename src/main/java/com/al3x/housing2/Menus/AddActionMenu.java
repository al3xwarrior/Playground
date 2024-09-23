package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class AddActionMenu extends Menu{

    private Player player;
    private HousingWorld house;
    private EventType event;

    public AddActionMenu(Player player, HousingWorld house, EventType event) {
        super(player, colorize("&aAdd Action"), 54);
        this.player = player;
        this.house = house;
        this.event = event;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack sendTitleItem = new ItemStack(Material.BOOK);
        ItemMeta sendTitleMeta = sendTitleItem.getItemMeta();
        sendTitleMeta.setDisplayName(colorize("&aSend Title Action"));
        sendTitleItem.setItemMeta(sendTitleMeta);
        addItem(10, sendTitleItem, () -> {
            eventActions.add(new SendTitleAction());
            house.setEventActions(event, eventActions);
            new ActionsMenu(player, house, event.toString(), event).open();
        });

        ItemStack chatMessageItem = new ItemStack(Material.PAPER);
        ItemMeta chatMessageMeta = chatMessageItem.getItemMeta();
        chatMessageItem.setDisplayName(colorize("&aSend Title Action"));
        chatMessageItem.setItemMeta(chatMessageMeta);
        addItem(10, chatMessageItem, () -> {
            eventActions.add(new ChatAction());
            house.setEventActions(event, eventActions);
            new ActionsMenu(player, house, event.toString(), event).open();
        });
    }
}
