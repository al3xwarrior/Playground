package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.al3x.housing2.Utils.Color.colorize;

public class StringProperty extends ActionProperty<String> {
    public StringProperty(String id, String name, String description) {
        super(id, name, description, Material.STRING);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        menu.openChat(main, getValue(), (message) -> setValue(message, player));
    }

    public String parsedValue(HousingWorld house, Player player) {
        return Placeholder.handlePlaceholders(getValue(), house, player);
    }

    public Component component(HousingWorld house, Player player) {
        return StringUtilsKt.housingStringFormatter(getValue(), house, player);
    }

    public String parseNoSpace(HousingWorld house, Player player) {
        String s = Placeholder.handlePlaceholders(getValue(), house, player);
        if (s.contains(" ")) {
            player.sendMessage(colorize("&cSpaces are not allowed in this value!"));
            return null;
        }
        return s;
    }
}
