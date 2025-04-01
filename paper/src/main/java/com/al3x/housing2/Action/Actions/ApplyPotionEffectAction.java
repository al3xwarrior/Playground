package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Getter
@Setter
@ToString
public class ApplyPotionEffectAction extends HTSLImpl implements NPCAction {

    private PotionEffectType potionEffectType = PotionEffectType.SPEED;
    private int level = 1;
    private int duration = 60;
    private boolean hideParticles = false;

    public ApplyPotionEffectAction() {
        super(
                "apply_potion_effect_action",
                "Apply Potion Effect",
                "Applies a potion effect to the player.",
                Material.POTION
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "potion",
                        "Potion",
                        "The potion effect to apply.",
                        ActionProperty.PropertyType.CUSTOM,
                        this::potionConsumer
                ),
                new ActionProperty(
                        "level",
                        "Level",
                        "The level of the potion effect.",
                        ActionProperty.PropertyType.INT
                ),
                new ActionProperty(
                        "duration",
                        "Duration",
                        "The duration of the potion effect in ticks.",
                        ActionProperty.PropertyType.INT
                ),
                new ActionProperty(
                        "hideParticles",
                        "Hide Particles",
                        "Whether to hide the potion effect particles.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
    }

    private BiFunction<InventoryClickEvent, Object, Boolean> potionConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, obj) -> {
            //Create a list of all the potion effects
            List<Duple<PotionEffectType, ItemBuilder>> potions = new ArrayList<>();
            for (PotionEffectType type : PotionEffectType.values()) {
                potions.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + type.getName())));
            }
            //Basically because PotionEffectType isnt a ENUM we cant just use the enum class
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
        player.addPotionEffect(new PotionEffect(potionEffectType, duration, level - 1, true, !hideParticles));
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        le.addPotionEffect(new PotionEffect(potionEffectType, duration, level - 1, true, !hideParticles));
    }


    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
