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
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionProperty extends ActionProperty<PotionEffectType> {
    public PotionProperty(String id, String name, String description) {
        super(id, name, description, Material.POTION);
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        //Create a list of all the potion effects
        List<Duple<PotionEffectType, ItemBuilder>> potionDuple = new ArrayList<>();
        for (PotionEffectType type : Registry.EFFECT) {
            potionDuple.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + StringUtilsKt.formatCapitalize(type.getName()))));
        }
        //Basically because Sound isnt a ENUM we cant just use the enum class
        new PaginationMenu<>(main,
                "&eSelect a Potion", potionDuple,
                player, house, menu, (e, sound) -> {
            setValue(sound, player);
            menu.open();
        }).open();
    }
}
