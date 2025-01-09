package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ClearPotionEffectAction extends HTSLImpl {

    private PotionEffectType potionEffectType;
    private boolean clearAll;

    public ClearPotionEffectAction() {
        super("Clear Potion Effect Action");
        this.potionEffectType = PotionEffectType.SPEED;
        this.clearAll = false;
    }

    public ClearPotionEffectAction(PotionEffectType potionEffectType, boolean clearAll) {
        super("Clear Potion Effect Action");
        this.potionEffectType = potionEffectType;
        this.clearAll = clearAll;
    }

    @Override
    public String toString() {
        return "ApplyPotionEffectAction{" + "potionEffectType=" + potionEffectType + ", clearAll=" + clearAll + '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GLASS_BOTTLE);
        builder.name("&eClear Potion Effect Action").description("Clears all or a specific potion effect");
        builder.info("&eSettings", "");
        builder.info("Potion", "&a" + potionEffectType.getName());
        builder.info("Clear All", ((clearAll) ? "&aYes" : "&cNo"));
        builder.lClick(ActionType.EDIT_YELLOW).rClick(ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GLASS_BOTTLE);
        builder.name("&eClear Potion Effect");
        builder.lClick(ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionItem> items = List.of(
                new ActionItem("potion",
                        ItemBuilder.create(Material.POTION)
                                .name("&aPotion")
                                .info("&7Current Value", "")
                                .info(null, "&6" + potionEffectType.getName())
                                .lClick(ActionType.CHANGE_YELLOW),
                        (event, obj) -> {
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
                        }
                ),
                new ActionItem("clearAll", //Needs to be the exact same as the variable name
                        ItemBuilder.create(Material.POTION)
                                .name("&aClear All")
                                .description("If toggled on, this will clear all potion effects except the one selected")
                                .info("&7Current Value", "")
                                .info(null, ((clearAll) ? "&aYes" : "&cNo"))
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&ePotion Effect Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (clearAll) {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType() != potionEffectType) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        } else {
            player.removePotionEffect(potionEffectType);
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("potion", potionEffectType.getName());
        data.put("clearall", clearAll);
        return data;
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

    @Override
    public String keyword() {
        return "clearEffect";
    }
}
