package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.Utils.ParticleUtils;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.ParticleType;
import com.al3x.housing2.Enums.Particles;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.BiFunction;

import static com.al3x.housing2.Enums.Locations.*;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.SELECT_YELLOW;

// TODO: Implement old display item lore in new system

@ToString
public class ParticleAction extends HTSLImpl {
    private Particles particle = Particles.WHITE_SMOKE;
    private Locations location = INVOKERS_LOCATION;
    private String customLocation = null;
    private Locations location2 = INVOKERS_LOCATION;
    private String customLocation2 = null;
    private PushDirection direction = PushDirection.FORWARD;
    private String customDirection = null;
    private boolean isLineRange = true;
    private ParticleType type = ParticleType.LINE;
    private Double radius = 8D;
    private Double amount = 20D;
    private boolean globallyVisible = false;

    // Custom data for particles
    private Double speed = null;
    private Float size = null;
    private String color1 = null;
    private String color2 = null;

    public static HashMap<UUID, Duple<String, Integer>> particlesCooldownMap = new HashMap<>();

    public ParticleAction() {
        super(
                "particle_action",
                "Display Particle",
                "Displays a particle effect.",
                Material.FIREWORK_ROCKET,
                List.of("particle")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "particle",
                        "Particle",
                        "The particle to display.",
                        ActionProperty.PropertyType.ENUM, Particles.class
                ),
                new ActionProperty(
                        "type",
                        "Type",
                        "The type of particle effect.",
                        ActionProperty.PropertyType.ENUM,
                        ParticleType.class
                ),
                new ActionProperty(
                        "location",
                        "Location",
                        "The location to display the particle at.",
                        ActionProperty.PropertyType.CUSTOM,
                        (house, menu, player) -> locationConsumer(house, menu, player, true)
                ),
                new ActionProperty(
                        "radius",
                        "Radius/Length",
                        "The radius of the circle or length of the line.",
                        ActionProperty.PropertyType.NUMBER, 1.0, 20.0
                ).showIf(type == ParticleType.LINE || type == ParticleType.CURVE && isLineRange),
                new ActionProperty(
                        "location2",
                        "Location 2",
                        "The second location to display the particle at.",
                        ActionProperty.PropertyType.CUSTOM,
                        (house, menu, player) -> locationConsumer(house, menu, player, false)
                ).showIf(type == ParticleType.LINE || type == ParticleType.CURVE && !isLineRange),
                new ActionProperty(
                        "direction",
                        "Direction",
                        "The direction of the particle.",
                        ActionProperty.PropertyType.ENUM,
                        PushDirection.class
                ).showIf(type == ParticleType.CURVE || type == ParticleType.LINE),
                new ActionProperty(
                        "amount",
                        "Amount",
                        "The amount of particles to display.",
                        ActionProperty.PropertyType.NUMBER, 1.0, 100.0
                ).showIf(type != ParticleType.DOT),
                new ActionProperty(
                        "globallyVisible",
                        "Globally Visible",
                        "If true, the particle will be visible to all players in the world.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "size",
                        "Size",
                        "The size of the particle.",
                        ActionProperty.PropertyType.NUMBER, 1.0, 10.0
                ).showIf(particle.getData() != null && ParticleUtils.keys(particle).contains("size")),
                new ActionProperty(
                        "color",
                        "Color",
                        "The color of the particle.",
                        ActionProperty.PropertyType.COLOR
                ).showIf(particle.getData() != null && ParticleUtils.keys(particle).contains("color")),
                new ActionProperty(
                        "color2",
                        "Color 2",
                        "The second color of the particle.",
                        ActionProperty.PropertyType.COLOR
                ).showIf(particle.getData() != null && ParticleUtils.keys(particle).contains("color2")),
                new ActionProperty(
                        "speed",
                        "Extra",
                        "The speed of the particle.",
                        ActionProperty.PropertyType.NUMBER, 0.0, 10.0
                ).showIf(particle.getData() != null && ParticleUtils.keys(particle).contains("speed"))
                ));
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> locationConsumer(HousingWorld house, Menu backMenu, Player player, boolean loc1) {
        return (event, o) -> {
            if (event.getClick() == ClickType.MIDDLE) {
                isLineRange = !isLineRange;
                backMenu.open();
                return true;
            }
            return getCoordinate(event, o, loc1 ? customLocation : customLocation2, house, backMenu,
                    (coords, location) -> {
                        if (location == CUSTOM) {
                            if (loc1) customLocation = coords;
                            else customLocation2 = coords;
                        } else {
                            if (loc1) customLocation = null;
                            else customLocation2 = null;
                        }
                        if (loc1) this.location = location;
                        else this.location2 = location;
                        if (location == PLAYER_LOCATION) {
                            String playerCoords = player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ();
                            if (loc1) customLocation = playerCoords;
                            else customLocation2 = playerCoords;
                        }
                        backMenu.open();
                    }
            );
        };
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
        for (double x = startX; x <= endX; x++) {
            for (double z = startZ; z <= endZ; z++) {
                locations.add(new Location(world, x, center.getY(), z));
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
    public OutputType execute(Player player, HousingWorld house) {
        Location location = locationFromLocations(player, house, null, this.location, this.customLocation);
        if (location == null) {
            return OutputType.ERROR;
        }
        summonParticles(player, house, location);
        return OutputType.SUCCESS;
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
                    return getLocationFromString(player, base, player.getEyeLocation(), house, customLocation);
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
            case DOT -> locations.add(location);
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

        Object data = ParticleUtils.output(particle, color1, color2, size);
        if (globallyVisible) {
            for (Player p : house.getWorld().getPlayers()) {
                for (Location loc : locations) {
                    if (speed == null) {
                        p.spawnParticle(particle.getParticle(), loc, 1, data);
                    } else {
                        p.spawnParticle(particle.getParticle(), loc, 1, 0, 0, 0, speed, data);
                    }
                }
            }
        } else {
            for (Location loc : locations) {
                if (speed == null) {
                    player.spawnParticle(particle.getParticle(), loc, 1, data);
                } else {
                    player.spawnParticle(particle.getParticle(), loc, 1, 0, 0, 0, speed, data);
                }
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
        data.put("globallyVisible", globallyVisible);

        if (particle.getData() != null) {
            for (String key : ParticleUtils.keys(particle)) {
                switch (key) {
                    case "size" -> data.put("size", size);
                    case "color" -> data.put("color", color1);
                    case "color2" -> data.put("color2", color2);
                    case "speed" -> data.put("speed", speed);
                }
            }
        }

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
        if (data.containsKey("globallyVisible")) {
            globallyVisible = (boolean) data.get("globallyVisible");
        }

        if (particle.getData() != null) {
            for (String key : ParticleUtils.keys(particle)) {
                if (!data.containsKey(key)) {
                    continue;
                }
                switch (key) {
                    case "size" ->
                            size = data.get("size") == null ? 1F : NumberUtilsKt.toFloaT(data.get("size").toString());
                    case "color" -> color1 = (String) data.getOrDefault("color", "255,255,255");
                    case "color2" -> color2 = (String) data.getOrDefault("color2", "255,255,255");
                    case "speed" ->
                            speed = data.get("speed") == null ? 1F : NumberUtilsKt.toDoublE(data.get("speed").toString());
                }
            }
        }
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
    public String syntax() {
        return getScriptingKeywords().getFirst() + " <particle> <type> <radius> <amount> <location> <location2> [customData]";
    }

    @Override
    public String export(int indent) {
        String loc = (location == CUSTOM || location == Locations.PLAYER_LOCATION) ? customLocation : location.name();
        String loc2 = (location2 == CUSTOM || location2 == Locations.PLAYER_LOCATION) ? customLocation2 : location2.name();

        if (loc.contains("%")) {
            loc = "\"" + loc + "\"";
        }
        if (loc2.contains("%")) {
            loc2 = "\"" + loc2 + "\"";
        }

        StringBuilder customData = new StringBuilder();
        List<String> requireNonNull = ParticleUtils.keys(particle);
        if (requireNonNull == null) {
            return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + particle.name() + " " + type.name() + " " + radius + " " + amount + " " + loc + " " + loc2;
        }
        for (String key : requireNonNull) {
            switch (key) {
                case "size" -> customData.append(" ").append(size);
                case "color" -> customData.append(" ").append(color1);
                case "color2" -> customData.append(" ").append(color2);
                case "speed" -> customData.append(" ").append(speed);
            }
        }

        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + particle.name() + " " + type.name() + " " + radius + " " + amount + " " + loc + " " + loc2 + customData;
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        particle = Particles.valueOf(parts[0]);
        type = ParticleType.valueOf(parts[1]);
        radius = Double.parseDouble(parts[2]);
        amount = Double.parseDouble(parts[3]);

        Duple<String[], String> locationArg = handleArg(parts, 4);
        if (Locations.fromString(locationArg.getSecond()) != null) {
            location = Locations.fromString(locationArg.getSecond());
        } else {
            location = CUSTOM;
            customLocation = locationArg.getSecond();
        }
        parts = locationArg.getFirst();

        if (parts.length == 0) {
            return nextLines;
        }

        Duple<String[], String> location2Arg = handleArg(parts, 0);
        if (Locations.fromString(location2Arg.getSecond()) != null) {
            location2 = Locations.fromString(location2Arg.getSecond());
        } else {
            location2 = CUSTOM;
            customLocation2 = location2Arg.getSecond();
        }
        parts = location2Arg.getFirst();

        if (parts.length == 0) {
            return nextLines;
        }

        List<String> requireNonNull = ParticleUtils.keys(particle);
        if (requireNonNull == null) {
            return nextLines;
        }
        for (int i = 0; i < requireNonNull.size(); i++) {
            String key = requireNonNull.get(i);
            Duple<String[], String> arg = handleArg(parts, i);
            if (arg == null) {
                continue;
            }
            if (key.equals("size")) {
                size = Float.parseFloat(arg.getSecond());
            } else if (key.equals("color")) {
                color1 = arg.getSecond();
            } else if (key.equals("color2")) {
                color2 = arg.getSecond();
            } else if (key.equals("speed")) {
                speed = Double.parseDouble(arg.getSecond());
            }
            parts = arg.getFirst();
        }

        return nextLines;
    }
}
