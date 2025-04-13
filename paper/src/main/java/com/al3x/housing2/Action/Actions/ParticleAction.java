package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Actions.Utils.ParticleUtils;
import com.al3x.housing2.Action.Properties.*;
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
                new EnumProperty<>(
                        "particle",
                        "Particle",
                        "The particle to display.",
                        Particles.class
                ).setValue(Particles.WHITE_SMOKE),
                new EnumProperty<>(
                        "type",
                        "Type",
                        "The type of particle effect.",
                        ParticleType.class
                ).setValue(ParticleType.LINE),
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to display the particle at."
                ).setValue("INVOKERS_LOCATION"),
                new BooleanProperty(
                        "isLineRange",
                        "Is Line Range",
                        "Used for line and curve types. If true, the particle will be displayed in a line from the location to the direction. If false, the particle will be displayed at the location."
                ).setValue(true),
                new NumberProperty(
                        "radius",
                        "Radius/Length",
                        "The radius of the circle or length of the line.",
                        1.0, 20.0
                ).showIf(() -> getValue("type", ParticleType.class) == ParticleType.LINE || getValue("type", ParticleType.class) == ParticleType.CURVE && getValue("isLineRange", Boolean.class)).setValue("8"),
                new LocationProperty(
                        "location2",
                        "Location 2",
                        "The second location to display the particle at."
                ).showIf(() -> getValue("type", ParticleType.class) == ParticleType.LINE || getValue("type", ParticleType.class) == ParticleType.CURVE && !getValue("isLineRange", Boolean.class)).setValue("INVOKERS_LOCATION"),
                new EnumProperty<>(
                        "direction",
                        "Direction",
                        "The direction of the particle.",
                        PushDirection.class
                ).showIf(() -> getValue("type", ParticleType.class) == ParticleType.CURVE || (getValue("type", ParticleType.class) == ParticleType.LINE && getValue("isLineRange", Boolean.class))).setValue(PushDirection.FORWARD),
                new NumberProperty(
                        "amount",
                        "Amount",
                        "The amount of particles to display.",
                        1.0, 100.0
                ).showIf(() -> getValue("type", ParticleType.class) != ParticleType.DOT).setValue("20"),
                new BooleanProperty(
                        "globallyVisible",
                        "Globally Visible",
                        "If true, the particle will be visible to all players in the world."
                ),
                new NumberProperty(
                        "size",
                        "Size",
                        "The size of the particle.",
                        1.0, 10.0
                ).showIf(() -> getValue("particle", Particles.class).getData() != null && ParticleUtils.keys(getValue("particle", Particles.class)).contains("size")),
                new ColorProperty(
                        "color",
                        "Color",
                        "The color of the particle."
                ).showIf(() -> getValue("particle", Particles.class).getData() != null && ParticleUtils.keys(getValue("particle", Particles.class)).contains("color")),
                new ColorProperty(
                        "color2",
                        "Color 2",
                        "The second color of the particle."
                ).showIf(() -> getValue("particle", Particles.class).getData() != null && ParticleUtils.keys(getValue("particle", Particles.class)).contains("color2")),
                new NumberProperty(
                        "speed",
                        "Extra",
                        "The speed of the particle.",
                        0.0, 10.0
                ).showIf(() -> getValue("particle", Particles.class).getData() != null && ParticleUtils.keys(getValue("particle", Particles.class)).contains("speed"))
        ));
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
        Vector curve = switch (getValue("direction", PushDirection.class)) {
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
        for (double i = 0; i < distance; i += increment) {
            Location loc = start.clone().add(direction.clone().multiply(i)).add(curve.clone().multiply(Math.sin(i / distance * Math.PI) * 2));
            locations.add(loc);
        }
        return locations;
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Location location = getProperty("location", LocationProperty.class).getLocation(player, house, player.getLocation(), player.getEyeLocation());
        if (location == null) {
            return OutputType.ERROR;
        }
        summonParticles(player, house, location);
        return OutputType.SUCCESS;
    }

    private Location getLocationFromLookingAt(Player player, HousingWorld house, Location base, int range) {
        Vector direction = base.getDirection();
        switch (getValue("direction", PushDirection.class)) {
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
        Double radius = getProperty("radius", NumberProperty.class).parsedValue(house, player);
        Double amount = getProperty("amount", NumberProperty.class).parsedValue(house, player);
        boolean isLineRange = getValue("isLineRange", Boolean.class);
        Particles particle = getValue("particle", Particles.class);
        switch (getValue("type", ParticleType.class)) {
            case CIRCLE -> locations = getCircle(location, radius, amount.intValue());
            case CURVE -> {
                Location loc2;
                if (isLineRange) {
                    //Location
                    loc2 = getLocationFromLookingAt(player, house, location, radius.intValue());
                } else {
                    loc2 = getProperty("location2", LocationProperty.class).getLocation(player, house, location, player.getEyeLocation());
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
                    loc2 = getProperty("location2", LocationProperty.class).getLocation(player, house, location, player.getEyeLocation());
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
        String color1 = getValue("color", ColorProperty.class).getValue();
        String color2 = getValue("color2", ColorProperty.class).getValue();
        Float size = getProperty("size", NumberProperty.class).parsedValue(house, player).floatValue();
        boolean globallyVisible = getValue("globallyVisible", Boolean.class);
        Double speed = getProperty("speed", NumberProperty.class).parsedValue(house, player);
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
}
