package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import com.al3x.housing2.Utils.NumberUtilsKt;
import kotlin.NumbersKt;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class ApplyPotionEffectAction extends HTSLImpl {

    private PotionEffectType potionEffectType;
    private int level;
    private int duration;
    private boolean hideParticles;

    public ApplyPotionEffectAction() {
        super("Apply Potion Effect Action");
        this.potionEffectType = PotionEffectType.SPEED;
        this.level = 1;
        this.duration = 60;
        this.hideParticles = false;
    }

    public ApplyPotionEffectAction(PotionEffectType potionEffectType, int level, int duration) {
        super("Apply Potion Effect Action");
        this.potionEffectType = potionEffectType;
        this.level = level;
        this.duration = duration;
    }

    @Override
    public String toString() {return "ApplyPotionEffectAction{" + "potionEffectType=" + potionEffectType + ", level=" + level + ", duration=" + duration + '}';}

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.POTION);
        builder.name("&eApply Potion Effect Action").description("Applys a potion effect to the player");
        builder.info("&eSettings", "");
        builder.info("Potion", "&a" + potionEffectType.getName());
        builder.info("Level", "&a" + level);
        builder.info("Duration", "&a" + duration + " ticks");
        builder.lClick(ActionType.EDIT_YELLOW).rClick(ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.POTION);
        builder.name("&eApply Potion Effect");
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
                new ActionItem("level",
                        ItemBuilder.create(Material.POTION)
                                .name("&aLevel")
                                .info("&7Current Value", "")
                                .info(null, level)
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.INT, 1, 100 //Pretty easy way to do min and max
                ),
                new ActionItem("duration",
                        ItemBuilder.create(Material.POTION)
                                .name("&aDuration")
                                .info("&7Current Value", "")
                                .info(null, duration)
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.INT, -1, 2000000 //Pretty easy way to do min and max
                ),
                new ActionItem("hideParticles",
                        ItemBuilder.create(Material.POTION)
                                .name("&aHide Particles")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (hideParticles ? "Yes" : "No"))
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&ePotion Effect Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.addPotionEffect(new PotionEffect(potionEffectType, duration, level - 1, true, !hideParticles));
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("potion", potionEffectType.getName());
        data.put("duration", duration);
        data.put("level", level);
        data.put("hideParticles", hideParticles);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (!data.containsKey("potion")) return;
        potionEffectType = PotionEffectType.getByName((String) data.get("potion"));
        if (!data.containsKey("duration")) return;
        duration = NumberUtilsKt.toInt((Double) data.get("duration"));
        if (!data.containsKey("level")) return;
        level = NumberUtilsKt.toInt((Double) data.get("level"));
        if (!data.containsKey("hideParticles")) {
            hideParticles = false;
            return;
        };
        hideParticles = (Boolean) data.get("hideParticles");
    }

    @Override
    public String keyword() {
        return "applyPotion";
    }
}
