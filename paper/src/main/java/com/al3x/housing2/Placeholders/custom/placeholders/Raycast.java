package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Runnables;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.Truple;
import io.papermc.paper.util.MCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftFluidCollisionMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftRayTraceResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.level.ClipContext.Block.OUTLINE;

public class Raycast {
    public Raycast() {
        new Block();
        new NPC();
        new EntityPlaceholder();
    }

    private static class Block extends Placeholder {
        public Block() {
            new X();
            new Y();
            new Z();
            new ExactX();
            new ExactY();
            new ExactZ();
            new Coords();
            new ExactCoords();
        }

        @Override
        public String getPlaceholder() {
            return "%raycast.block/[range]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public List<String> getAliases() {
            return List.of("%raycast.block.type/[range]%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (!input.contains("/")) {
                return "null";
            }
            String[] a = input.split("/");
            String args = String.join("/", Arrays.asList(a).subList(1, a.length));
            Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
            try {
                Double range = argsHandled.getFirst();
                @NotNull Vector result = getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());

                return player.getWorld().getBlockAt(result.getBlockX(), result.getBlockY(), result.getBlockZ()).getType().name();
            } catch (NumberFormatException e) {
                return "null";
            }
        }

        private static class ExactX extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.exactx/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getX());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        private static class X extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.x/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getBlockX());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        private static class ExactY extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.exacty/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getY());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        private static class Y extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.y/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getBlockY());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        private static class ExactZ extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.exactz/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getZ());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        private static class Z extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.z/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    return String.valueOf(getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getBlockZ());
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }


        private static class ExactCoords extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.exactcoords/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    @NotNull Vector pos = getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    return pos.getX() + "," + pos.getY() + "," + pos.getZ();
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }

        //coords
        private static class Coords extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.block.coords/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    @NotNull Vector block = getBlockLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    return block.getBlockX() + "," + block.getBlockY() + "," + block.getBlockZ();
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }
    }

    //Just id for now
    private static class NPC {
        public NPC() {
            new Id();
        }

        private static class Id extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.npc.id/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> duple = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (duple == null) {
                        return "null";
                    }
                    Entity entity = duple.getFirst();
                    net.citizensnpcs.api.npc.NPC citizensNPC = CitizensAPI.getNPCRegistry().getNPC(entity);
                    if (citizensNPC != null) {
                        return String.valueOf(citizensNPC.getId());
                    }
                    return "null";
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }
    }

    //name, type, x, y, z, coords
    private static class EntityPlaceholder {
        public EntityPlaceholder() {
            new Name();
            new Type();
            new X();
            new ExactX();
            new Y();
            new ExactY();
            new Z();
            new ExactZ();
            new Coords();
        }

        private static class Name extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity/[range]%";
            }

            @Override
            public List<String> getAliases() {
                return List.of("%raycast.entity.name/[range]%");
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Entity entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getFirst();
                    if (entity != null) {
                        return entity.getName();
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class Type extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.type/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Entity entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getFirst();
                    if (entity != null) {
                        return entity.getType().name();
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class X extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.x/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getSecond().getX());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class ExactX extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.exactx/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getFirst().getX());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class Y extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.y/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getSecond().getY());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class ExactY extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.exacty/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getFirst().getY());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class Z extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.z/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getSecond().getZ());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class ExactZ extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.exactz/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        return String.valueOf(entity.getFirst().getZ());
                    }
                    return "null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }

        private static class Coords extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%raycast.entity.coords/[range]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                String[] a = input.split("/");
                String args = String.join("/", Arrays.asList(a).subList(1, a.length));
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args, house, player);
                try {
                    Double range = argsHandled.getFirst();
                    Duple<Entity, Vector> entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird());
                    if (entity != null) {
                        Vector loc = entity.getSecond();
                        return loc.getX() + "," + loc.getY() + "," + loc.getZ();
                    }
                    return "null,null,null";
                } catch (Exception e) {
                    return "null";
                }
            }
        }
    }

    private static @NotNull Vector getBlockLookingAt(Player player, double range, String yaw, String pitch) {
        try {
            Location eye = player.getEyeLocation();
            Vector direction = eye.getDirection();
            if (pitch != null && yaw != null) {
                double eyePitch = eye.getPitch();
                double eyeYaw = eye.getYaw();

                if (NumberUtilsKt.isDouble(pitch)) {
                    eyePitch = Double.parseDouble(pitch);
                }

                if (pitch.startsWith("~")) {
                    if (pitch.equals("~")) {
                        pitch = "~0";
                    }
                    eyePitch += Double.parseDouble(pitch.substring(1));
                }

                if (NumberUtilsKt.isDouble(yaw)) {
                    eyeYaw = Double.parseDouble(yaw);
                }

                if (yaw.startsWith("~")) {
                    if (yaw.equals("~")) {
                        yaw = "~0";
                    }
                    eyeYaw += Double.parseDouble(yaw.substring(1));
                }

                direction = new Vector();

                double rotX = eyeYaw;
                double rotY = eyePitch;

                direction.setY(-Math.sin(Math.toRadians(rotY)));

                double xz = Math.cos(Math.toRadians(rotY));

                direction.setX(-xz * Math.sin(Math.toRadians(rotX)));
                direction.setZ(xz * Math.cos(Math.toRadians(rotX)));
            }
            Vector dir = direction.clone().normalize().multiply(range);
            Vec3 startPos = MCUtil.toVec3(player.getEyeLocation());
            Vec3 endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());
            HitResult nmsHitResult = ((CraftWorld) player.getWorld()).getHandle().clip(new ClipContext(startPos, endPos, OUTLINE, CraftFluidCollisionMode.toNMS(FluidCollisionMode.NEVER), CollisionContext.empty()), (Predicate) null);
            RayTraceResult hitResult = CraftRayTraceResult.fromNMS(player.getWorld(), nmsHitResult);

            if (hitResult == null || hitResult.getHitBlock() == null) {
                return new Vector(endPos.x, endPos.y, endPos.z);
            }
            return hitResult.getHitPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Duple<Entity, Vector> getEntityLookingAt(Player player, double range, String yaw, String pitch) {
        try {
            Location eye = player.getEyeLocation();
            Vector direction = eye.getDirection();
            if (pitch != null && yaw != null) {
                double eyePitch = eye.getPitch();
                double eyeYaw = eye.getYaw();

                if (NumberUtilsKt.isDouble(pitch)) {
                    eyePitch = Double.parseDouble(pitch);
                }

                if (pitch.startsWith("~")) {
                    if (pitch.equals("~")) {
                        eyePitch = 0;
                    }
                    eyePitch += Double.parseDouble(pitch.substring(1));
                }

                if (NumberUtilsKt.isDouble(yaw)) {
                    eyeYaw = Double.parseDouble(yaw);
                }

                if (yaw.startsWith("~")) {
                    if (yaw.equals("~")) {
                        eyePitch = 0;
                    }
                    eyeYaw += Double.parseDouble(yaw.substring(1));
                }

                direction = new Vector();

                double rotX = eyeYaw;
                double rotY = eyePitch;

                direction.setY(-Math.sin(Math.toRadians(rotY)));

                double xz = Math.cos(Math.toRadians(rotY));

                direction.setX(-xz * Math.sin(Math.toRadians(rotX)));
                direction.setZ(xz * Math.cos(Math.toRadians(rotX)));
            }


            for (Entity entity : Runnables.entityMap.get(player.getUniqueId())) {
                BoundingBox box = entity.getBoundingBox();
                if (box != null) {
                    RayTraceResult result = box.rayTrace(eye.toVector(), direction, range);

                    if (result != null) {
                        return Duple.of(entity, result.getHitPosition());
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static Truple<Double, String, String> handleRaycastArgs(String args, HousingWorld house, Player player) {
        if (args.contains(" ")) {
            String[] split = args.split(" ");
            if (split.length < 3) {
                try {
                    String arg1S = Placeholder.handlePlaceholders(split[0], house, player, true);
                    double arg1 = Double.parseDouble(arg1S);
                    return new Truple<>(arg1, null, null);
                } catch (NumberFormatException e) {
                    return new Truple<>(0.0, null, null);
                }
            }
            try {
                String arg1S = Placeholder.handlePlaceholders(split[0], house, player, true);
                double arg1 = Double.parseDouble(arg1S);
                String arg2 = Placeholder.handlePlaceholders(split[1], house, player, true);
                String arg3 = Placeholder.handlePlaceholders(split[2], house, player, true);
                return new Truple<>(arg1, arg2, arg3);
            } catch (NumberFormatException e) {
                return new Truple<>(0.0, null, null);
            }
        } else {
            try {
                String arg1S = Placeholder.handlePlaceholders(args, house, player, true);
                double arg1 = Double.parseDouble(arg1S);
                return new Truple<>(arg1, null, null);
            } catch (NumberFormatException e) {
                return new Truple<>(0.0, null, null);
            }
        }
    }
}
