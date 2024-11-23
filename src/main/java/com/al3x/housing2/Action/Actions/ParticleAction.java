package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.ParticleType;
import com.al3x.housing2.Enums.Particles;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ParticleAction extends Action {
    private Particles particle;
    private PushDirection direction;
    private String customDirection;
    private ParticleType type;
    private Double radius;
    private Double amount;
    private boolean globallyVisible;

    public ParticleAction() {
        super("Particle Action");
        this.particle = Particles.WHITE_SMOKE;
        this.direction = PushDirection.FORWARD;
        this.type = ParticleType.LINE;
        this.radius = 8D;
        this.amount = 3D;
        this.globallyVisible = false;
    }

    public ParticleAction(Particles particle, ParticleType type, Double radius, Double amount, PushDirection direction, boolean globallyVisible) {
        super("Particle Action");
        this.particle = particle;
        this.direction = direction;
        this.type = type;
        this.radius = radius;
        this.amount = amount;
        this.globallyVisible = globallyVisible;
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
        builder.info("Radius/Length", "&a" + radius);
        builder.info("Amount", "&a" + amount);
        if (type == ParticleType.LINE) {
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
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
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
        if (type == ParticleType.LINE) {
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
                    }))
            );
        }
        items.add(new ActionEditor.ActionItem("radius",
                        ItemBuilder.create(Material.SLIME_BALL)
                                .name("&eRadius/Length")
                                .info("&7Current Value", "")
                                .info(null, "&a" + radius)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 1, 40 //Pretty easy to change the max value
                )
        );

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
                    Vector custom = location.getDirection().setY(0).normalize();
                    custom = custom.multiply(Math.cos(Math.toRadians(pitch)));
                    custom = custom.setY(Math.sin(Math.toRadians(pitch)));
                    custom = custom.setX(custom.getX() * Math.cos(Math.toRadians(yaw)));
                    custom = custom.setZ(custom.getZ() * Math.sin(Math.toRadians(yaw)));
                    direction = custom;
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

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Location location = player.getLocation();
        List<Location> locations = new ArrayList<>();
        switch (type) {
            case CIRCLE -> locations = getCircle(location, radius, 40);
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
        return true;
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
