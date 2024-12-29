package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;

public class ExplosionAction extends Action {
    private String customLocation;
    private Locations location;
    private double power; //Ints are not storeable anywhere

    public ExplosionAction() {
        super("Explosion Action");
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
        this.power = 4;
    }

    @Override
    public String toString() {
        return "Explosion Action (Location: " + (location == CUSTOM ? customLocation : location) + ", Power: " + power + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT_MINECART);
        builder.name("&eExplosion Player");
        builder.info("&eSettings", "");
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));
        builder.info("Power", "&a" + power);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.TNT_MINECART);
        builder.name("&aExplosion");
        builder.description("Create an explosion at a set location.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (location == CUSTOM ? customLocation : location))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                        (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                                (coords, location) -> {
                                    customLocation = coords;
                                    this.location = CUSTOM;
                                    if (location == PLAYER_LOCATION) {
                                        Location loc = player.getLocation();
                                        this.customLocation = loc.getX() + " " + loc.getY() + " " + loc.getZ();
                                    }
                                }
                        )
                ),
                new ActionEditor.ActionItem("power",
                        ItemBuilder.create(Material.BOOK)
                                .name("&aPower")
                                .info("&7Current Value", "")
                                .info(null, power)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT
                )
        );

        return new ActionEditor(4, "&eExplosion Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        World world = house.getWorld();
        switch (location) {
            case INVOKERS_LOCATION ->
                    world.createExplosion(player.getLocation(), (float) power, false, false);
            case HOUSE_SPAWN ->
                    world.createExplosion(house.getSpawn(), (float) power, false, false);
            case CUSTOM, PLAYER_LOCATION ->
                    world.createExplosion(getLocationFromString(player, house, customLocation), (float) power, false, false);
        }
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("location", location.name());
        data.put("customLocation", customLocation);
        data.put("power", power);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        customLocation = (String) data.get("customLocation");
        location = Locations.valueOf((String) data.get("location"));
        power = (double) data.get("power");
    }
}
