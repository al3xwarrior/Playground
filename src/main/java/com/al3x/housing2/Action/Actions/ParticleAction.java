package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.*;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Enums.Locations.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class ParticleAction extends Action {
    private Particles particle;
    private Locations location = null;
    private String customLocation = null;
    private Locations location2 = null;
    private String customLocation2 = null;
    private PushDirection direction = null;
    private String customDirection = null;
    private ParticleType type;
    private Double radius;
    private Double amount;
    private boolean globallyVisible;

    public ParticleAction() {
        super("Particle Action");
        this.particle = Particles.WHITE_SMOKE;
        this.direction = PushDirection.FORWARD;
        this.type = ParticleType.LINE;
        this.location = INVOKERS_LOCATION;
        this.location2 = INVOKERS_LOCATION;
        this.radius = 8D;
        this.amount = 3D;
        this.globallyVisible = false;
    }

    @Override
    public String toString() {
        return "ParticleAction (Particle: " + particle + ", Type: " + type + ", Radius: " + radius + ", Direction: " + direction + ", Globally Visible: " + globallyVisible + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("4461d9d06c0bf4a7af4b16fd12831e2be0cf42e6e55e9c0d311a2a8965a23b34");
        builder.name("&eParticle Action");
        builder.info("&eSettings", "");
        builder.info("Particle&6", particle.name());
        builder.info("Type&6", type.name());
        if (type != ParticleType.CURVE) {
            builder.info("Radius", "&6" + radius);
        }
        builder.info("Amount", "&a" + amount);
        builder.info("Location&a", ((location == Locations.CUSTOM || location == PLAYER_LOCATION) ? customLocation : location.name()));
        if (type == ParticleType.CURVE) {
            builder.info("Location 2&a", ((location2 == Locations.CUSTOM || location2 == PLAYER_LOCATION) ? customLocation2 : location2.name()));
        }
        if (type == ParticleType.LINE || type == ParticleType.CURVE) {
            builder.info("Direction", "&6" + direction);
        }
        builder.info("Globally Visible", (globallyVisible ? "&aTrue" : "&cFalse"));

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("4461d9d06c0bf4a7af4b16fd12831e2be0cf42e6e55e9c0d311a2a8965a23b34");
        builder.name("&aParticle Action");
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
        if (type == ParticleType.CURVE) {
            items.add(new ActionEditor.ActionItem("location2",
                    ItemBuilder.create(Material.COMPASS)
                            .name("&eLocation 2")
                            .info("&7Current Value", "")
                            .info(null, "&a" + ((location2 == Locations.CUSTOM || location2 == PLAYER_LOCATION) ? customLocation2 : location2))
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                    (event, o) -> getCoordinate(event, o, customLocation2, house, editMenu,
                            (coords, location) -> {
                                customLocation2 = coords;
                                this.location2 = location;
                                if (location == PLAYER_LOCATION) {
                                    Location loc = player.getLocation();
                                    this.customLocation2 = loc.getX() + "," + loc.getY() + "," + loc.getZ();
                                }
                                Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                            }
                    )
            ));
        }
        if (type == ParticleType.LINE || type == ParticleType.CURVE) {
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
        if (type != ParticleType.CURVE) {
            items.add(new ActionEditor.ActionItem("radius",
                            ItemBuilder.create(Material.SLIME_BALL)
                                    .name("&eRadius/Length")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + radius)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.DOUBLE, 1, 40 //Pretty easy to change the max value
                    )
            );
        }

        items.add(new ActionEditor.ActionItem("amount",
                        ItemBuilder.create(Material.IRON_BARS)
                                .name("&eAmount")
                                .description("Amount of particles to spawn around the locations or along the line.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + amount)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 1, 20 //Pretty easy to change the max value
                )
        );

        items.add(new ActionEditor.ActionItem("globallyVisible",
                ItemBuilder.create(Material.ENDER_EYE)
                        .name("&eGlobally Visible")
                        .description("Should the particles be visible to everyone? (Not just the player)")
                        .info("&7Current Value", "")
                        .info(null, "&a" + globallyVisible)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.BOOLEAN
        ));

        return new ActionEditor(4, "&eParticle Settings", items);
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

    public List<Location> getLine(Location location, double range) {
        List<Location> locations = new ArrayList<>();
        Vector direction = location.getDirection();
        switch (this.direction) {
            case UP -> direction = new Vector(0, 1, 0);
            case DOWN -> direction = new Vector(0, -1, 0);
            case NORTH -> direction = new Vector(0, 0, -1);
            case SOUTH -> direction = new Vector(0, 0, 1);
            case EAST -> direction = new Vector(1, 0, 0);
            case WEST -> direction = new Vector(-1, 0, 0);
            case LEFT -> {
                Vector dir = location.getDirection();
                direction = new Vector(-dir.getZ(), 0, dir.getX()).normalize();
            }
            case RIGHT -> {
                Vector dir = location.getDirection();
                direction = new Vector(dir.getZ(), 0, -dir.getX()).normalize();
            }
            case CUSTOM -> {
                String[] split = customDirection.split(",");
                if (split.length != 2) {
                    return locations;
                }
                //pitch,yaw
                try {
                    float pitch = Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) location, null, split[0]));
                    float yaw = Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) location, null, split[1]));
                    Vector vector = new Vector();
                    double rotX = yaw;
                    double rotY = pitch;
                    vector.setY(-Math.sin(Math.toRadians(rotY)));
                    double xz = Math.cos(Math.toRadians(rotY));
                    vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
                    vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
                    direction = vector;
                } catch (NumberFormatException e) {
                    return locations;
                }
            }
        }
        for (double i = 0; i < range; i+=0.5) {
            Location loc = location.add(direction);
            locations.add(loc.clone());
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
        for (double i = 0; i < distance; i+=increment) {
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

    private void summonParticles(Player player, HousingWorld house, Location location) {
        List<Location> locations = new ArrayList<>();
        switch (type) {
            case CIRCLE -> locations = getCircle(location, radius, 40);
            case CURVE -> {
                Location loc2 = locationFromLocations(player, house, (this.location != INVOKERS_LOCATION) ? location : null, location2, customLocation2);
                locations = getCurve(location, loc2, 40, player, house);
            }
            case LINE -> locations = getLine(location, radius);
            case SQUARE -> locations = getSquare(location, radius, 40);
        }
        for (Location loc : locations) {
            if (globallyVisible) {
                loc.getWorld().spawnParticle(particle.getParticle(), loc, NumberUtilsKt.toInt(amount));
            } else {
                player.spawnParticle(particle.getParticle(), loc, NumberUtilsKt.toInt(amount));
            }
        }
    }

    @Override
    public boolean mustBeSync() {
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("particle", particle);
        data.put("type", type);
        data.put("location", location);
        data.put("customLocation", customLocation);
        data.put("location2", location2);
        data.put("customLocation2", customLocation2);
        data.put("radius", radius);
        data.put("amount", amount);
        data.put("direction", direction);
        data.put("customDirection", customDirection);
        data.put("globallyVisible", globallyVisible);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
