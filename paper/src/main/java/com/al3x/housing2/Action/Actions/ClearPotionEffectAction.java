package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.CustomProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

@ToString
public class ClearPotionEffectAction extends HTSLImpl implements NPCAction {
    public ClearPotionEffectAction() {
        super(
                "clear_potion_effect_action",
                "Clear Potion Effect",
                "Clears a potion effect from the player.",
                Material.GLASS_BOTTLE,
                List.of("clearEffect")
        );

        getProperties().addAll(List.of(
                new CustomProperty<PotionEffectType>(
                        "potion",
                        "Potion",
                        "The potion effect to clear.",
                        Material.POTION
                ) {
                    @Override
                    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
                        List<Duple<PotionEffectType, ItemBuilder>> potions = new ArrayList<>();
                        for (PotionEffectType type : PotionEffectType.values()) {
                            potions.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + type.getName())));
                        }

                        new PaginationMenu<>(Main.getInstance(),
                                "&eSelect a Potion Effect", potions,
                                player, house, menu, (potion) -> {
                            setValue(potion);
                            menu.open();
                        }).open();
                    }
                }.setValue(PotionEffectType.SPEED),
                new BooleanProperty(
                        "clearall",
                        "Clear All",
                        "If true, this will clear all potion effects except the one selected."
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        combinedExecute(player);
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        combinedExecute(le);
    }

    private void combinedExecute(LivingEntity le) {
        PotionEffectType potionEffectType = getValue("potion", PotionEffectType.class);
        if (getValue("clearall", Boolean.class)) {
            le.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType() != potionEffectType) {
                    le.removePotionEffect(potionEffect.getType());
                }
            });
        } else {
            le.removePotionEffect(potionEffectType);
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
