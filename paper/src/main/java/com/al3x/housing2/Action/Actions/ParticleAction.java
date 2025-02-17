package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.Actions.Utils.ParticleUtils;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.*;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.*;
import com.comphenix.packetwrapper.wrappers.play.clientbound.WrapperPlayServerWorldParticles;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.google.gson.internal.LinkedTreeMap;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.Vector;
import reactor.util.function.Tuple3;

import java.util.*;

import static com.al3x.housing2.Enums.Locations.*;
import static com.al3x.housing2.Enums.Particles.ParticleData.COLOR;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.SELECT_YELLOW;

public class ParticleAction extends HTSLImpl {
    private Particles particle;
    private Locations location = null;
    private String customLocation = null;
    private Locations location2 = null;
    private String customLocation2 = null;
    private PushDirection direction = null;
    private String customDirection = null;
    private boolean isLineRange = false;
    private ParticleType type;
    private Double radius;
    private Double amount;
    private HashMap<String, Object> customData = new HashMap<>();
    public static HashMap<UUID, Duple<String, Integer>> particlesCooldownMap = new HashMap<>();

    public ParticleAction() {
        super("Particle Action");
        this.particle = Particles.WHITE_SMOKE;
        this.direction = PushDirection.FORWARD;
        this.type = ParticleType.LINE;
        this.location = INVOKERS_LOCATION;
        this.location2 = INVOKERS_LOCATION;
        this.isLineRange = true;
        this.radius = 8D;
        this.amount = 20D;
    }

    @Override
    public String toString() {
        return "ParticleAction (Particle: " + particle + ", Type: " + type + ", Radius: " + radius + ", Direction: " + direction + ", Amount: " + amount + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("4461d9d06c0bf4a7af4b16fd12831e2be0cf42e6e55e9c0d311a2a8965a23b34");
        builder.name("&eDisplay Particles");
        builder.info("&eSettings", "");
        builder.info("Particle&6", particle.name());
        builder.info("Type&6", type.name());
        if ((type != ParticleType.CURVE && type != ParticleType.LINE) || isLineRange) {
            builder.info("Radius", "&6" + radius);
        }
        builder.info("Amount", "&a" + amount);
        builder.info("Location&a", ((location == Locations.CUSTOM || location == PLAYER_LOCATION) ? customLocation : location.name()));
        if ((type == ParticleType.LINE || type == ParticleType.CURVE) && !isLineRange) {
            builder.info("Location 2&a", ((location2 == Locations.CUSTOM || location2 == PLAYER_LOCATION) ? customLocation2 : location2.name()));
        }
        if (type == ParticleType.CURVE || type == ParticleType.LINE) {
            builder.info("Direction&a", direction.name());
        }
        if (customData != null && !customData.isEmpty()) {
            builder.info("&eCustom Data", "");
            for (Map.Entry<String, Object> entry : customData.entrySet()) {
                builder.info(entry.getKey(), entry.getValue().toString());
            }
        }
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("4461d9d06c0bf4a7af4b16fd12831e2be0cf42e6e55e9c0d311a2a8965a23b34");
        builder.name("&aDisplay Particles");
        builder.description("Do stuff with particles.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu, Player player) {
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.add(new ActionEditor.ActionItem("particle",
                ItemBuilder.create(this.particle.getMaterial())
                        .name("&eParticle")
                        .info("&7Current Value", "")
                        .info(null, "&a" + particle)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, Particles.values(), null
        ));
        items.add(new ActionEditor.ActionItem("type",
                ItemBuilder.create(type.getMaterial())
                        .name("&eType")
                        .info("&7Current Value", "")
                        .info(null, "&a" + type)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, ParticleType.values(), null
        ));
        items.add(new ActionEditor.ActionItem("location",
                ItemBuilder.create(Material.COMPASS)
                        .name("&eLocation")
                        .info("&7Current Value", "")
                        .info(null, "&a" + ((location == Locations.CUSTOM || location == PLAYER_LOCATION) ? customLocation : location))
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                (event, o) -> getCoordinate(event, o, customLocation, house, editMenu,
                        (coords, location) -> {
                            customLocation = coords;
                            this.location = location;
                            if (location == PLAYER_LOCATION) {
                                Location loc = player.getLocation();
                                this.customLocation = loc.getX() + "," + loc.getY() + "," + loc.getZ();
                            }
                            Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                        }
                )
        ));
        if ((type == ParticleType.LINE || type == ParticleType.CURVE) && !isLineRange) {
            ActionEditor.ActionItem[] item1 = new ActionEditor.ActionItem[1];
            ActionEditor.ActionItem i = new ActionEditor.ActionItem("location2",
                    ItemBuilder.create(Material.COMPASS)
                            .name("&eLocation 2")
                            .info("&7Current Value", "")
                            .info(null, "&a" + ((location2 == Locations.CUSTOM || location2 == PLAYER_LOCATION) ? customLocation2 : location2))
                            .info(null, "")
                            .info(null, "&7&lMiddle Click to toggle between")
                            .info(null, "&7&lline range and location")
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                            .mClick(ItemBuilder.ActionType.TOGGLE_RANGE),
                    (event, notExistant) -> {
                        if (event.getClick() == ClickType.MIDDLE) {
                            isLineRange = !isLineRange;
                            editMenu.setupItems();
                            return true;
                        }
                        List<Duple<Locations, ItemBuilder>> locs = new ArrayList<>();
                        for (Locations loc : Locations.values()) {
                            locs.add(new Duple<>(loc, ItemBuilder.create(Material.COMPASS).name("&e" + loc.name()).lClick(SELECT_YELLOW)));
                        }
                        new PaginationMenu<>(Main.getInstance(), "&eSelect a location type", locs, player, house, editMenu, (o) -> {
                            getCoordinate(event, o, customLocation2, house, editMenu,
                                    (coords, location) -> {
                                        customLocation2 = coords;
                                        this.location2 = location;
                                        if (location == PLAYER_LOCATION) {
                                            Location loc = player.getLocation();
                                            this.customLocation2 = loc.getX() + "," + loc.getY() + "," + loc.getZ();
                                        }
                                        Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                                    }
                            );
                        }).open();
                        return true;
                    }
            );
            item1[0] = i;
            items.add(i);

            items.add(new ActionEditor.ActionItem("direction",
                    ItemBuilder.create(Material.COMPASS)
                            .name("&eDirection")
                            .info("&7Current Value", "")
                            .info(null, "&a" + direction)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.ENUM, PushDirection.values(), Material.COMPASS,
                    (event, obj) -> getDirection(event, obj, house, editMenu, (str, dir) -> {
                        customDirection = str;
                        direction = dir;
                        editMenu.open();
                    }))
            );
        }
        if ((type != ParticleType.CURVE && type != ParticleType.LINE) || isLineRange) {
            ItemBuilder itemBuilder = ItemBuilder.create(Material.SLIME_BALL)
                    .name("&eRadius/Length")
                    .info("&7Current Value", "")
                    .info(null, "&a" + radius)
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            if (type == ParticleType.CURVE || type == ParticleType.LINE) {
                itemBuilder
                        .info(null, "")
                        .info(null, "&7&lMiddle Click to toggle between")
                        .info(null, "&7&lline range and location")
                        .mClick(ItemBuilder.ActionType.TOGGLE_RANGE);
            }
            items.add(new ActionEditor.ActionItem("radius",
                            itemBuilder,
                            (e, o) -> {
                                if (e.getClick() == ClickType.MIDDLE) {
                                    isLineRange = !isLineRange;
                                    editMenu.setupItems();
                                    return true;
                                }
                                player.sendMessage(colorize("&ePlease enter the number you wish to see in chat!"));
                                editMenu.openChat(Main.getInstance(), "" + radius, (s) -> {
                                    if (NumberUtilsKt.isDouble(s)) {
                                        radius = Double.parseDouble(s);
                                        if (radius < 0) {
                                            radius = 0D;
                                        }
                                        if (radius > 40) {
                                            radius = 40D;
                                        }
                                        Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                                    }
                                });
                                return true;
                            }
                    )
            );
        }

        //May be deprecating this soon
        items.add(new ActionEditor.ActionItem("amount",
                        ItemBuilder.create(Material.IRON_BARS)
                                .name("&eAmount")
                                .description("Amount of locations to spawn particles at. eg. A line with a radius of 8 and an amount of 8 will have 1 particle every block.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + amount)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 1, 40 //Pretty easy to change the max value
                )
        );

        if (particle.getData() != null) {
            items.addAll(ParticleUtils.customData(particle, customData, house, editMenu, player));
        }

        return new ActionEditor(5, "&eParticle Settings", items);
    }

    public List<Location> getCircle(Location center, double radius, int amount) {
        List<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    public List<Location> getLine(Location location, Location loc2, int amount, Player player, HousingWorld house) {
        List<Location> locations = new ArrayList<>();

        Vector direction = loc2.toVector().subtract(location.toVector()).normalize();
        double distance = location.distance(loc2);
        double increment = distance / amount;
        for (double i = 0; i < distance; i += increment) {
            Location loc = location.clone().add(direction.clone().multiply(i));
            locations.add(loc);
        }
        return locations;
    }

    public List<Location> getSquare(Location center, double radius, int amount) {
        List<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        double startX = center.clone().subtract(radius, 0, 0).getX();
        double startZ = center.clone().subtract(0, 0, radius).getZ();
        double endX = center.clone().add(radius, 0, 0).getX();
        double endZ = center.clone().add(0, 0, radius).getZ();
        for (double x = startX; x < endX; x++) {
            for (double z = startZ; z < endZ; z++) {
                if (x != startX || z != startZ) {
                    locations.add(new Location(world, x, center.getY(), z));
                }
            }
        }
        return locations;
    }

    public List<Location> getCurve(Location start, Location end, int amount, Player player, HousingWorld house) {
        List<Location> locations = new ArrayList<>();
        if (end == null) end = player.getEyeLocation().clone();
        double distance = start.distance(end);
        double increment = distance / amount;
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        //curve the line in the direction its going
        Vector curve = switch (this.direction) {
            case UP, FORWARD -> new Vector(0, direction.getY(), 0).normalize();
            case LEFT -> new Vector(direction.getZ(), 0, -direction.getX()).normalize();
            case RIGHT -> new Vector(-direction.getZ(), 0, direction.getX()).normalize();
            case DOWN -> new Vector(0, -direction.getY(), 0).normalize();
            case NORTH -> new Vector(0, 0, -direction.getZ()).normalize();
            case SOUTH -> new Vector(0, 0, direction.getZ()).normalize();
            case EAST -> new Vector(direction.getX(), 0, 0).normalize();
            case WEST -> new Vector(-direction.getX(), 0, 0).normalize();
            case CUSTOM -> {
                if (customDirection == null) {
                    yield null;
                }
                String[] split = customDirection.split(",");
                if (split.length != 2) {
                    yield null;
                }

                try {
                    float pitch = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[0]));
                    float yaw = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[1]));
                    Vector vector = new Vector();
                    double rotX = yaw;
                    double rotY = pitch;
                    vector.setY(-Math.sin(Math.toRadians(rotY)));
                    double xz = Math.cos(Math.toRadians(rotY));
                    vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
                    vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
                    yield vector;
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> new Vector(0, 0, 0);
        };
        if (curve == null) {
            return locations;
        }
        for (double i = 0; i < distance; i += increment) {
            Location loc = start.clone().add(direction.clone().multiply(i)).add(curve.clone().multiply(Math.sin(i / distance * Math.PI) * 2));
            locations.add(loc);
        }
        return locations;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Location location = locationFromLocations(player, house, null, this.location, this.customLocation);
        if (location == null) {
            return true;
        }
        summonParticles(player, house, location);
        return true;
    }

    private Location locationFromLocations(Player player, HousingWorld house, Location base, Locations location, String customLocation) {
        switch (location == null ? INVOKERS_LOCATION : location) {
            case INVOKERS_LOCATION -> {
                return player.getEyeLocation().clone();
            }
            case HOUSE_SPAWN -> {
                return house.getSpawn().clone();
            }
            case CUSTOM, PLAYER_LOCATION -> {
                if (base != null) {
                    return getLocationFromString(player, base, house, customLocation);
                }
                return getLocationFromString(player, house, customLocation);
            }
        }
        return null;
    }

    private Location getLocationFromLookingAt(Player player, HousingWorld house, Location base, int range) {
        Vector direction = base.getDirection();
        switch (this.direction) {
            case UP -> direction = new Vector(0, 1, 0);
            case DOWN -> direction = new Vector(0, -1, 0);
            case NORTH -> direction = new Vector(0, 0, -1);
            case SOUTH -> direction = new Vector(0, 0, 1);
            case EAST -> direction = new Vector(1, 0, 0);
            case WEST -> direction = new Vector(-1, 0, 0);
            case LEFT -> {
                Vector dir = base.getDirection();
                direction = new Vector(-dir.getZ(), 0, dir.getX()).normalize();
            }
            case RIGHT -> {
                Vector dir = base.getDirection();
                direction = new Vector(dir.getZ(), 0, -dir.getX()).normalize();
            }
            case CUSTOM -> {
                String[] split = customDirection.split(",");
                if (split.length != 2) {
                    return null;
                }
                //pitch,yaw
                try {
                    float pitch = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[0]));
                    float yaw = Float.parseFloat(HandlePlaceholders.parsePlaceholders(player, house, split[1]));
                    Vector vector = new Vector();
                    double rotX = yaw;
                    double rotY = pitch;
                    vector.setY(-Math.sin(Math.toRadians(rotY)));
                    double xz = Math.cos(Math.toRadians(rotY));
                    vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
                    vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
                    direction = vector;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        Location loc = base.clone();
        for (int i = 0; i < range; i++) {
            loc.add(direction);
            if (loc.getBlock().getType().isSolid()) {
                return loc;
            }
        }
        return loc;
    }

    private void summonParticles(Player player, HousingWorld house, Location location) {
        List<Location> locations = new ArrayList<>();
        switch (type) {
            case CIRCLE -> locations = getCircle(location, radius, amount.intValue());
            case CURVE -> {
                Location loc2;
                if (isLineRange) {
                    //Location
                    loc2 = getLocationFromLookingAt(player, house, location, radius.intValue());
                } else {
                    loc2 = locationFromLocations(player, house, (this.location != INVOKERS_LOCATION) ? location : null, location2, customLocation2);
                }

                if (loc2 == null) {
                    return;
                }
                locations = getCurve(location, loc2, amount.intValue(), player, house);
            }
            case LINE -> {
                Location loc2;
                if (isLineRange) {
                    //Location
                    loc2 = getLocationFromLookingAt(player, house, location, radius.intValue());
                } else {
                    loc2 = locationFromLocations(player, house, (this.location != INVOKERS_LOCATION) ? location : null, location2, customLocation2);
                }
                if (loc2 == null) {
                    return;
                }
                locations = getLine(location, loc2, amount.intValue(), player, house);
            }
            case SQUARE -> locations = getSquare(location, radius, amount.intValue());
        }

        if (particlesCooldownMap.containsKey(player.getUniqueId())) {
            Duple<String, Integer> duple = particlesCooldownMap.get(player.getUniqueId());
            if (duple.getFirst().equals(particle.name())) {
                if (duple.getSecond() > 2000) {
                    return;
                }
                particlesCooldownMap.put(player.getUniqueId(), new Duple<>(particle.name(), duple.getSecond() + 1));
            } else {
                particlesCooldownMap.put(player.getUniqueId(), new Duple<>(particle.name(), 0));
            }
        } else {
            particlesCooldownMap.put(player.getUniqueId(), new Duple<>(particle.name(), 0));
        }

        Object data = ParticleUtils.output(particle, customData);

        for (Location loc : locations) {
            for (Player p : house.getWorld().getPlayers()) {
                p.spawnParticle(particle.getParticle(), loc, 1, data);
            }
        }
    }

    @Override
    public boolean mustBeSync() {
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("particle", particle);
        data.put("type", type);
        data.put("location", location);
        data.put("customLocation", customLocation);
        data.put("location2", location2);
        data.put("customLocation2", customLocation2);
        data.put("isLineRange", isLineRange);
        data.put("radius", radius);
        data.put("amount", amount);
        data.put("direction", direction);
        data.put("customDirection", customDirection);
        data.put("customData", customData);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        particle = Particles.valueOf((String) data.get("particle"));
        type = ParticleType.valueOf((String) data.get("type"));
        location = Locations.valueOf((String) data.get("location"));
        customLocation = (String) data.get("customLocation");
        location2 = Locations.valueOf((String) data.get("location2"));
        customLocation2 = (String) data.get("customLocation2");
        isLineRange = (boolean) data.get("isLineRange");
        radius = (Double) data.get("radius");
        amount = (Double) data.get("amount");
        direction = PushDirection.valueOf((String) data.get("direction"));
        customDirection = (String) data.get("customDirection");
        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) data.get("customData");
        customData = new HashMap<>(map);
    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "particle";
    }

    @Override
    public String syntax() {
        return "particle <particle> <type> <radius> <amount> <location> <location2> [customData]";
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == Locations.PLAYER_LOCATION) ? customLocation : location.name();
        String loc2 = (location2 == CUSTOM || location2 == Locations.PLAYER_LOCATION) ? customLocation2 : location2.name();

        StringBuilder customData = new StringBuilder();
        for (Map.Entry<String, Object> entry : this.customData.entrySet()) {
            customData.append(" ").append(entry.getValue());
        }

        return " ".repeat(indent) + keyword() + " " + particle.name() + " " + type.name() + " " + radius + " " + amount + " " + loc + " " + loc2 + customData;
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        particle = Particles.valueOf(parts[0]);
        type = ParticleType.valueOf(parts[1]);
        radius = Double.parseDouble(parts[2]);
        amount = Double.parseDouble(parts[3]);
        if (Locations.fromString(parts[4]) != null) {
            location = Locations.fromString(parts[4]);
        } else {
            location = CUSTOM;
            customLocation = parts[4];
            if (customLocation.startsWith("\"")) {
                customLocation = customLocation.substring(1);
                parts = new ArrayList<>(Arrays.asList(parts).subList(4, parts.length)).toArray(new String[0]);
                while (!customLocation.endsWith("\"")) {
                    customLocation += " " + parts[1];
                    parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
                }
                customLocation = customLocation.substring(0, customLocation.length() - 1);
            }
            parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
        }
        if (Locations.fromString(parts[5]) != null) {
            location2 = Locations.fromString(parts[5]);
        } else {
            if (parts[5].contains(",")) {
                location2 = CUSTOM;
                customLocation = parts[5];
                if (customLocation.startsWith("\"")) {
                    customLocation = customLocation.substring(1);
                    parts = new ArrayList<>(Arrays.asList(parts).subList(5, parts.length)).toArray(new String[0]);
                    while (!customLocation.endsWith("\"")) {
                        customLocation += " " + parts[1];
                        parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
                    }
                    customLocation = customLocation.substring(0, customLocation.length() - 1);
                }
                parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
            } else {
                isLineRange = true;
                radius = Double.parseDouble(parts[5]);
            }
        }

        List<String> keys = ParticleUtils.keys(particle);
        if (keys != null) {
            for (int i = 6; i < parts.length; i++) {
                customData.put(keys.get(i - 6), parts[i]);
            }
        }

        return nextLines;
    }
}
