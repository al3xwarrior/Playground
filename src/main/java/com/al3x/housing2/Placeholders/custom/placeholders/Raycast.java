package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Runnables;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.al3x.housing2.Utils.Truple;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

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
            new Coords();
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
            String[] args = input.split("/");
            if (args.length < 2) {
                return "null";
            }
            String[] a = input.split("/");
            String arg1 = String.join("/", Arrays.asList(a).subList(1, a.length));
            try {
                int range = Integer.parseInt(Placeholder.handlePlaceholders(arg1, house, player, true));
                return player.getTargetBlock(null, range).getType().name();
            } catch (NumberFormatException e) {
                return "null";
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
                String[] args = input.split("/");
                if (args.length < 2) {
                    return "null";
                }
                String[] a = input.split("/");
                String arg1 = String.join("/", Arrays.asList(a).subList(1, a.length));
                try {
                    int range = Integer.parseInt(Placeholder.handlePlaceholders(arg1, house, player, true));
                    Location loc = player.getTargetBlock(null, range).getLocation();
                    return String.valueOf(loc.getBlockX());
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
                String[] args = input.split("/");
                if (args.length < 2) {
                    return "null";
                }
                String[] a = input.split("/");
                String arg1 = String.join("/", Arrays.asList(a).subList(1, a.length));
                try {
                    int range = Integer.parseInt(Placeholder.handlePlaceholders(arg1, house, player, true));
                    Location loc = player.getTargetBlock(null, range).getLocation();
                    return String.valueOf(loc.getBlockY());
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
                String[] args = input.split("/");
                if (args.length < 2) {
                    return "null";
                }
                String[] a = input.split("/");
                String arg1 = String.join("/", Arrays.asList(a).subList(1, a.length));
                try {
                    int range = Integer.parseInt(Placeholder.handlePlaceholders(arg1, house, player, true));
                    Location loc = player.getTargetBlock(null, range).getLocation();
                    return String.valueOf(loc.getBlockZ());
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
                String[] args = input.split("/");
                if (args.length < 2) {
                    return "null";
                }
                String[] a = input.split("/");
                String arg1 = String.join("/", Arrays.asList(a).subList(1, a.length));
                try {
                    int range = Integer.parseInt(Placeholder.handlePlaceholders(arg1, house, player, true));
                    Location loc = player.getTargetBlock(null, range).getLocation();
                    return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
                try {
                    Double range = argsHandled.getFirst();
                    Entity entity = getEntityLookingAt(player, range, argsHandled.getSecond(), argsHandled.getThird()).getFirst();
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
            new Y();
            new Z();
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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
                Truple<Double, String, String> argsHandled = handleRaycastArgs(args);
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

    private static Duple<Entity, Vector> getEntityLookingAt(Player player, double range, String pitch, String yaw) {
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
                    eyePitch += Double.parseDouble(pitch.substring(1));
                }

                if (NumberUtilsKt.isDouble(yaw)) {
                    eyeYaw = Double.parseDouble(yaw);
                }

                if (yaw.startsWith("~")) {
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

    private static Truple<Double, String, String> handleRaycastArgs(String args) {
        if (args.contains(" ")) {
            String[] split = args.split(" ");
            if (split.length < 3) {
                try {
                    String arg1S = Placeholder.handlePlaceholders(split[0], null, null, true);
                    double arg1 = Double.parseDouble(arg1S);
                    return new Truple<>(arg1, null, null);
                } catch (NumberFormatException e) {
                    return new Truple<>(0.0, null, null);
                }
            }
            try {
                String arg1S = Placeholder.handlePlaceholders(split[0], null, null, true);
                double arg1 = Double.parseDouble(arg1S);
                String arg2 = Placeholder.handlePlaceholders(split[1], null, null, true);
                String arg3 = Placeholder.handlePlaceholders(split[2], null, null, true);
                return new Truple<>(arg1, arg2, arg3);
            } catch (NumberFormatException e) {
                return new Truple<>(0.0, null, null);
            }
        } else {
            try {
                String arg1S = Placeholder.handlePlaceholders(args, null, null, true);
                double arg1 = Double.parseDouble(arg1S);
                return new Truple<>(arg1, null, null);
            } catch (NumberFormatException e) {
                return new Truple<>(0.0, null, null);
            }
        }
    }
}
