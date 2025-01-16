package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Runnables;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

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
            try {
                int range = Integer.parseInt(args[1]);
                Location loc = player.getTargetBlock(null, range).getLocation();
                return String.valueOf(loc.getBlockX());
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
                try {
                    int range = Integer.parseInt(args[1]);
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
                try {
                    int range = Integer.parseInt(args[1]);
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
                try {
                    int range = Integer.parseInt(args[1]);
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
                try {
                    int range = Integer.parseInt(args[1]);
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        return entity.getName();
                    }
                    return "null";
                } catch (NumberFormatException e) {
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        return entity.getType().name();
                    }
                    return "null";
                } catch (NumberFormatException e) {
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        return String.valueOf(entity.getLocation().getBlockX());
                    }
                    return "null";
                } catch (NumberFormatException e) {
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        return String.valueOf(entity.getLocation().getBlockY());
                    }
                    return "null";
                } catch (NumberFormatException e) {
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        return String.valueOf(entity.getLocation().getBlockZ());
                    }
                    return "null";
                } catch (NumberFormatException e) {
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
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (!input.contains("/")) {
                    return "null";
                }
                try {
                    int range = Integer.parseInt(input.split("/")[1]);
                    Entity entity = getEntityLookingAt(player, range);
                    if (entity != null) {
                        Location loc = entity.getLocation();
                        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
                    }
                    return "null,null,null";
                } catch (NumberFormatException e) {
                    return "null";
                }
            }
        }
    }

    private static Entity getEntityLookingAt(Player player, double range) {
        try {
            Location eye = player.getEyeLocation();
            Vector direction = eye.getDirection();
            for (Entity entity : Runnables.entityMap.get(player.getUniqueId())) {
                BoundingBox box = entity.getBoundingBox();
                if (box != null) {
                    RayTraceResult result = box.rayTrace(eye.toVector(), direction, range);
                    if (result != null) {
                        return entity;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
