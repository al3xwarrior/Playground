package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.CustomProperty;
import com.al3x.housing2.Action.Properties.IntegerProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
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
    public ApplyPotionEffectAction() {
        super(
                "apply_potion_effect_action",
                "Apply Potion Effect",
                "Applies a potion effect to the player.",
                Material.POTION,
                List.of("applyEffect")
        );

        getProperties().addAll(List.of(
                new CustomProperty<PotionEffectType>(
                        "potion",
                        "Potion",
                        "The potion effect to apply.",
                        Material.POTION
                ) {
                    @Override
                    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
                        //Create a list of all the potion effects
                        List<Duple<PotionEffectType, ItemBuilder>> potions = new ArrayList<>();
                        for (PotionEffectType type : PotionEffectType.values()) {
                            potions.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + type.getName())));
                        }
                        //Basically because PotionEffectType isnt a ENUM we cant just use the enum class
                        new PaginationMenu<>(Main.getInstance(),
                                "&eSelect a Potion Effect", potions,
                                player, house, menu, (potion) -> {
                            setValue(potion);
                            menu.open();
                        }).open();
                    }
                }.setValue(PotionEffectType.NIGHT_VISION),
                new IntegerProperty(
                        "level",
                        "Level",
                        "The level of the potion effect."
                ).setValue(1),
                new IntegerProperty(
                        "duration",
                        "Duration",
                        "The duration of the potion effect in ticks."
                ).setValue(20*30),
                new BooleanProperty(
                        "hideParticles",
                        "Hide Particles",
                        "Whether to hide the potion effect particles."
                ).setValue(false)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        PotionEffectType potionEffectType = getValue("potion", PotionEffectType.class);
        int level = getValue("level", Integer.class);
        int duration = getValue("duration", Integer.class);
        boolean hideParticles = getValue("hideParticles", Boolean.class);
        player.addPotionEffect(new PotionEffect(potionEffectType, duration, level - 1, true, !hideParticles));
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        PotionEffectType potionEffectType = getValue("potion", PotionEffectType.class);
        int level = getValue("level", Integer.class);
        int duration = getValue("duration", Integer.class);
        boolean hideParticles = getValue("hideParticles", Boolean.class);
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        le.addPotionEffect(new PotionEffect(potionEffectType, duration, level - 1, true, !hideParticles));
    }


    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
