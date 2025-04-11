package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.al3x.housing2.Utils.Color.colorize;

public class ColorProperty extends ActionProperty<String> {
    public ColorProperty(String id, String name, String description) {
        super(id, name, description, Material.PAINTING);
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        player.sendMessage(colorize("&ePlease enter the color in RGB format (0-255) separated by commas."));
        menu.openChat(main, getValue(), (s) -> {
            String[] split = s.split(",");
            if (split.length != 3) {
                player.sendMessage(colorize("&cInvalid color format!"));
                return;
            }
            try {
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                    player.sendMessage(colorize("&cInvalid color format!"));
                    return;
                }
                setValue(s);
                player.sendMessage(colorize("&aColor set to " + r + ", " + g + ", " + b + "."));
                Bukkit.getScheduler().runTask(main, menu::open);
            } catch (NumberFormatException e) {
                player.sendMessage(colorize("&cInvalid color format!"));
            }
        });
    }
}
