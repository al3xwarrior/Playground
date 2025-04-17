package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundProperty extends ActionProperty<Sound>  {
    public SoundProperty(String id, String name, String description) {
        super(id, name, description, Material.NOTE_BLOCK);
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        //Create a list of all the potion effects
        List<Duple<Sound, ItemBuilder>> soundDuple = new ArrayList<>();
        for (Sound type : Registry.SOUNDS) {
            soundDuple.add(new Duple<>(type, ItemBuilder.create(Material.NOTE_BLOCK).name("&6" + type.name())));
        }
        //Basically because Sound isnt a ENUM we cant just use the enum class
        new PaginationMenu<>(main,
                "&eSelect a Sound", soundDuple,
                player, house, menu, (e, sound) -> {
            if (e.isRightClick()) {
                player.playSound(player.getLocation(), sound,
                        menu.getAction().getValue("volume", Double.class).floatValue(),
                        menu.getAction().getValue("pitch", Double.class).floatValue()
                );
            } else {
                setValue(sound, player);
                menu.open();
            }
        }).open();
    }
}
