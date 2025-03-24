package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;
import static com.al3x.housing2.Utils.Color.colorize;

public class PlaySoundAction extends HTSLImpl {
    private Double volume;
    private Double pitch;
    private Sound sound;
    private String customLocation;
    private Locations location;

    public PlaySoundAction() {
        super("Play Sound Action");
        this.volume = 1.0D;
        this.pitch = 1.0D;
        this.sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
    }

    @Override
    public String toString() {
        return "PlaySoundAction (Volume: " + volume + ", Pitch: " + pitch + ", Sound: " + sound.toString() + ", Location: " + (location == CUSTOM ? customLocation : location) + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&ePlay Sound");
        builder.info("&eSettings", "");
        builder.info("Value", "&a" + sound);
        builder.info("Volume", "&a" + volume);
        builder.info("Pitch", "&a" + pitch);
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&aPlay Sound");
        builder.description("Play a sound with a custom pitch to the player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    @SuppressWarnings("removal")
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
                                    player.playSound(player.getLocation(), potion, this.volume.floatValue(), this.pitch.floatValue());
                                } else {
                                    sound = potion;
                                    backMenu.open();
                                }
                            }).open();
                            return true;
                        }
                ),
                new ActionEditor.ActionItem("volume",
                        ItemBuilder.create(Material.IRON_BLOCK)
                                .name("&eVolume")
                                .info("&7Current Value", "")
                                .info(null, "&a" + volume)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                ),
                new ActionEditor.ActionItem("pitch",
                        ItemBuilder.create(Material.BELL)
                                .name("&ePitch")
                                .info("&7Current Value", "")
                                .info(null, "&a" + pitch)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                ),
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (location == CUSTOM ? customLocation : location))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                        (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                                (coords, location) -> {
                                    if (location == CUSTOM) {
                                        customLocation = coords;
                                    } else {
                                        customLocation = null;
                                    }
                                    this.location = location;
                                    backMenu.open();
                                }
                        )
                )
        );

        return new ActionEditor(4, "&ePlay Sound Action Settings", items);
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        switch (location) {
            case INVOKERS_LOCATION ->
                    player.playSound(player.getLocation(), sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            case HOUSE_SPAWN ->
                    player.playSound(house.getSpawn(), sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            case CUSTOM -> {
                if (customLocation == null) return OutputType.ERROR;
                Location loc = getLocationFromString(player, house, customLocation);
                if (loc != null) player.playSound(loc, sound, NumberUtilsKt.toFloat(volume), NumberUtilsKt.toFloat(pitch));
            }
        }
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("volume", volume);
        data.put("pitch", pitch);
        data.put("sound", sound.name());
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        volume = (Double) data.get("volume");
        pitch = (Double) data.get("pitch");
        sound = Sound.valueOf((String) data.get("sound"));
        customLocation = (String) data.get("customLocation");
        location = Locations.valueOf((String) data.get("location"));
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == PLAYER_LOCATION) ? "\"" + customLocation + "\""  : location.name();
        return " ".repeat(indent) + keyword() + " " + sound.name() + " " + volume + " " + pitch + " " + loc;
    }

    @Override
    public String syntax() {
        return "sound <sound> <volume> <pitch> <location>";
    }

    @Override
    public String keyword() {
        return "sound";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        sound = Sound.valueOf(parts[0]);
        volume = Double.parseDouble(parts[1]);
        pitch = Double.parseDouble(parts[2]);
        if (Locations.fromString(parts[3]) != null) {
            location = Locations.fromString(parts[3]);
        } else {
            location = CUSTOM;
            Duple<String[], String> locationArg = handleArg(parts, 3);
            customLocation = locationArg.getSecond();
        }
        return nextLines;
    }
}
