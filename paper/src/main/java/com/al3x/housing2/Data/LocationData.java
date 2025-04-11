package com.al3x.housing2.Data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class LocationData {
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public LocationData() {

    }

    public LocationData(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static LocationData fromLocation(Location location) {
        return new LocationData(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static List<LocationData> fromLocationList(List<Location> waypoints) {
        List<LocationData> list = new ArrayList<>();
        for (Location location : waypoints) {
            list.add(fromLocation(location));
        }
        return list;
    }

    public static LocationData fromString(String key) {
        String[] split = key.split(", ");
        if (split.length != 4) {
            return new LocationData(split[0], 0.0, 0.0, 0.0, 0f, 0f);
        }
        try {
            return new LocationData(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), 0f, 0f);
        } catch (Exception e) {
            return new LocationData(split[0], 0.0, 0.0, 0.0, 0f, 0f);
        }
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}