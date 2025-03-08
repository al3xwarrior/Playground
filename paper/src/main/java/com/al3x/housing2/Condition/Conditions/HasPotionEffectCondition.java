package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class HasPotionEffectCondition extends CHTSLImpl implements NPCCondition {
    private PotionEffectType potionEffect = null;

    public HasPotionEffectCondition() {
        super("Has Potion Effect");
        potionEffect = PotionEffectType.GLOWING;
    }

    @Override
    public String toString() {
        return "HasPotionEffectCondition (potionEffect: " + (potionEffect == null ? "&aNot Set" : "&6" + potionEffect) + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.POTION);
        builder.name("&aHas Potion Effect");
        builder.description("Requires the user to have the specified potion effect.");
        builder.info("Potion Effect", (potionEffect == null ? "&aNot Set" : "&6" + potionEffect.getKey()));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.POTION);
        builder.name("&eHas Potion Effect");
        builder.description("Requires the user to have the specified potion effect.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("potionEffect",
                        ItemBuilder.create(Material.POTION)
                                .name("&eEffect")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (potionEffect == null ? "Not Set" : potionEffect.getKey()))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        (e, o) -> {
                            List<Duple<PotionEffectType, ItemBuilder>> potions = new ArrayList<>();
                            for (PotionEffectType type : PotionEffectType.values()) {
                                potions.add(new Duple<>(type, ItemBuilder.create(Material.POTION).name("&6" + type.getName())));
                            }
                            //Basically because PotionEffectType isnt a ENUM we cant just use the enum class
                            new PaginationMenu<>(Main.getInstance(),
                                    "&eSelect a Potion Effect", potions,
                                    (Player) e.getWhoClicked(), house, backMenu, (potion) -> {
                                potionEffect = potion;
                                backMenu.open();
                            }).open();
                            return true;
                        }
                )
        );
        return new ActionEditor(4, "Has Potion Effect", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return player.hasPotionEffect(potionEffect);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("potionEffect", potionEffect == null ? PotionEffectType.SPEED.getName() : potionEffect.getName());
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Condition> condtionClass) {
        potionEffect = PotionEffectType.getByName((String) data.get("potionEffect"));
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + keyword() + " " + potionEffect.getName();
    }

    @Override
    public void importCondition(String condition, List<String> nextLines) {
        potionEffect = PotionEffectType.getByName(condition);
    }

    @Override
    public String keyword() {
        return "hasPotionEffect";
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            return le.hasPotionEffect(potionEffect);
        } else {
            return false;
        }
    }
}
