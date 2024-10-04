package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Enums.EventType.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class RandomAction implements Action {
    private ArrayList<Action> subActions;

    public RandomAction(ArrayList<Action> subactions) {
        this.subActions = subactions;
    }

    public RandomAction() {
        this.subActions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RandomAction (SubActions: " + subActions.stream().map(Action::toString).reduce((a, b) -> a + ", " + b).orElse("") + ")";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eRandom Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Change the settings for this action"),
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
        if (subActions.isEmpty()) {
            return true;
        }

        Action action = subActions.get((int) (house.getRandom().nextDouble() * subActions.size()));
        return action.execute(player, house);
    }

    public ArrayList<Action> getSubActions() {
        return subActions;
    }

    public void setSubActions(ArrayList<Action> subActions) {
        this.subActions = subActions;
    }
}
