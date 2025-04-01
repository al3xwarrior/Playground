package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
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

    private PotionEffectType potionEffectType = PotionEffectType.SPEED;
    private boolean clearAll = false;

    public ClearPotionEffectAction() {
        super(
                "clear_potion_effect_action",
                "Clear Potion Effect",
                "Clears a potion effect from the player.",
                Material.GLASS_BOTTLE,
                List.of("clearEffect")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "potion",
                        "Potion",
                        "The potion effect to clear.",
                        ActionProperty.PropertyType.CUSTOM,
                        this::potionConsumer
                ),
                new ActionProperty(
                        "clearall",
                        "Clear All",
                        "If true, this will clear all potion effects except the one selected.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> potionConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, obj) -> {
            List<Duple<PotionEffectType, ItemBuilder>> potions = new ArrayList<>();
            for (PotionEffectType type : PotionEffectType.values()) {
                potions.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + type.getName())));
            }

            new PaginationMenu<>(Main.getInstance(),
                    "&eSelect a Potion Effect", potions,
                    player, house, backMenu, (potion) -> {
                potionEffectType = potion;
                backMenu.open();
            }).open();
            return true;
        };
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (clearAll) {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType() != potionEffectType) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        } else {
            player.removePotionEffect(potionEffectType);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        if (clearAll) {
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

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        //Bunch of errors coming from PotionEffectType so I needed to add this
        if (!data.containsKey("potion")) return;
        potionEffectType = PotionEffectType.getByName((String) data.get("potion"));
        if (!data.containsKey("clearall")) return;
        clearAll = (boolean) data.get("clearall");
    }
}
