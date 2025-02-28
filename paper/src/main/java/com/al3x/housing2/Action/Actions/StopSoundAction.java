package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class StopSoundAction extends HTSLImpl {

    private Sound sound;
    private boolean clearAll;

    public StopSoundAction() {
        super("Stop Sound Action");
        this.sound = Sound.MUSIC_CREDITS;
        this.clearAll = false;
    }

    public StopSoundAction(Sound sound, boolean clearAll) {
        super("Stop Sound Action");
        this.sound = sound;
        this.clearAll = clearAll;
    }

    @Override
    public String toString() {
        return "StopSoundAction{" + "potionEffectType=" + sound + ", clearAll=" + clearAll + '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.QUARTZ);
        builder.name("&eStop Sound");
        builder.info("&eSettings", "");
        builder.info("Sound", "&a" + sound.name());
        builder.info("Clear All", ((clearAll) ? "&aYes" : "&cNo"));
        builder.lClick(ActionType.EDIT_YELLOW).rClick(ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.QUARTZ);
        builder.name("&eStop Sound");
        builder.description("Clears all or a specific sound effect");
        builder.lClick(ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("sound",
                        ItemBuilder.create(Material.NOTE_BLOCK)
                                .name("&eSound")
                                .info("&7Current Value", "")
                                .info(null, "&a" + sound)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        (event, obj) -> {
                            //Create a list of all the potion effects
                            List<Duple<Sound, ItemBuilder>> soundDuple = new ArrayList<>();
                            for (Sound type : Registry.SOUNDS) {
                                soundDuple.add(new Duple<>(type, ItemBuilder.create(Material.NOTE_BLOCK).name("&6" + StringUtilsKt.formatCapitalize(type.name()))));
                            }
                            //Basically because Sound isnt a ENUM we cant just use the enum class
                            new PaginationMenu<>(Main.getInstance(),
                                    "&eSelect a Sound", soundDuple,
                                    player, house, backMenu, (e, potion) -> {
                                if (e.isRightClick()) {
                                    player.playSound(player.getLocation(), potion, 1.0F, 1.0F);
                                } else {
                                    sound = potion;
                                    backMenu.open();
                                }
                            }).open();
                            return true;
                        }
                ),
                new ActionItem("clearAll", //Needs to be the exact same as the variable name
                        ItemBuilder.create((clearAll) ? Material.LIME_DYE : Material.RED_DYE)
                                .name("&aClear All")
                                .description("If toggled on, this will clear all sound effects except the one selected")
                                .info("&7Current Value", "")
                                .info(null, ((clearAll) ? "&aYes" : "&cNo"))
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&eSound Effect Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (clearAll) {
            for (Sound sound : Sound.values()) { //I don't love this, but its the same implementation as the clear potion effect action
                if (sound == this.sound) continue;
                player.stopSound(sound);
            }
        } else {
            player.stopSound(sound);
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("sound", sound.name());
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
        if (!data.containsKey("sound")) return;
        sound = Sound.valueOf((String) data.get("sound"));
        if (!data.containsKey("clearall")) return;
        clearAll = (boolean) data.get("clearall");
    }

    @Override
    public String keyword() {
        return "clearSound";
    }
}
