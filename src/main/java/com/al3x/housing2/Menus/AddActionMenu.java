package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Actions.ChatAction;
import com.al3x.housing2.Actions.SendTitleAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class AddActionMenu extends Menu{

    private Main main;
    private Player player;
    private HousingWorld house;
    private EventType event;
    private List<Action> actions;

    public AddActionMenu(Main main, Player player, HousingWorld house, EventType event) {
        super(player, colorize("&aAdd Action"), 54);
        this.player = player;
        this.house = house;
        this.event = event;
        this.actions = (house.getEventActions(event) != null) ? house.getEventActions(event) : new ArrayList<>();
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack sendTitleItem = new ItemStack(Material.BOOK);
        ItemMeta sendTitleMeta = sendTitleItem.getItemMeta();
        sendTitleMeta.setDisplayName(colorize("&aSend Title Action"));
        sendTitleItem.setItemMeta(sendTitleMeta);
        addItem(14, sendTitleItem, () -> {
            actions.add(new SendTitleAction());
            house.setEventActions(event, actions);
            new ActionsMenu(main, player, house, event.toString(), event).open();
        });

        ItemStack chatMessageItem = new ItemStack(Material.PAPER);
        ItemMeta chatMessageMeta = chatMessageItem.getItemMeta();
        chatMessageMeta.setDisplayName(colorize("&aSend Chat Action"));
        chatMessageItem.setItemMeta(chatMessageMeta);
        addItem(23, chatMessageItem, () -> {
            actions.add(new ChatAction());
            house.setEventActions(event, actions);
            new ActionsMenu(main, player, house, event.toString(), event).open();
        });
    }
}
