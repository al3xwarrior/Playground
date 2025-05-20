package com.al3x.housing2.Utils;

import com.github.retrooper.packetevents.util.Quaternion4f;
import org.bukkit.Location;

public class QuaternionUtils {
    public static Quaternion4f multiply(Quaternion4f a, Quaternion4f b) {
        float x = a.getW() * b.getX() + a.getX() * b.getW() + a.getY() * b.getZ() - a.getZ() * b.getY();
        float y = a.getW() * b.getY() - a.getX() * b.getZ() + a.getY() * b.getW() + a.getZ() * b.getX();
        float z = a.getW() * b.getZ() + a.getX() * b.getY() - a.getY() * b.getX() + a.getZ() * b.getW();
        float w = a.getW() * b.getW() - a.getX() * b.getX() - a.getY() * b.getY() - a.getZ() * b.getZ();
        return new Quaternion4f(x, y, z, w);
    }

    public static Quaternion4f calculateFacingRotation(Location holoLoc, Location playerLoc) {
        double dx = playerLoc.getX() - holoLoc.getX();
        double dy = playerLoc.getY() - holoLoc.getY();
        double dz = playerLoc.getZ() - holoLoc.getZ();

        // Horizontal distance in the X-Z plane.
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        // Calculate yaw (Minecraft's yaw formula: arctan2(dz, dx) in degrees, adjusted by -90)
        double yaw = 90 - Math.toDegrees(Math.atan2(dz, dx));

        // Calculate pitch: negative arctan2(dy, horizontalDistance)
        double pitch = -Math.toDegrees(Math.atan2(dy, horizontalDistance));

        // Convert angles to radians and compute half angles.
        float halfYaw = (float) (Math.toRadians(yaw) / 2);
        float halfPitch = (float) (Math.toRadians(pitch) / 2);

        // Create a quaternion for yaw (rotation about the Y-axis).
        Quaternion4f yawQuat = new Quaternion4f(
                0,
                (float) Math.sin(halfYaw),
                0,
                (float) Math.cos(halfYaw)
        );

        // Create a quaternion for pitch (rotation about the X-axis).
        Quaternion4f pitchQuat = new Quaternion4f(
                (float) Math.sin(halfPitch),
                0,
                0,
                (float) Math.cos(halfPitch)
        );

        // Combine the quaternions: apply yaw first, then pitch.
        // Since Quaternion multiplication is not commutative, order matters.
        Quaternion4f facingRotation = multiply(yawQuat, pitchQuat);

        return facingRotation;
    }
}
