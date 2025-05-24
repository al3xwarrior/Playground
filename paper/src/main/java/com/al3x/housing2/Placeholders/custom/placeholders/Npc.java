package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.citizensnpcs.trait.CurrentLocation;
import net.citizensnpcs.trait.ScaledMaxHealthTrait;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Npc {
    public Npc() {
        new Name();
        new Location(); //x,y,z,coords
        new Health();
        new MaxHealth();
        new IsDead();
    }

    private static class Name extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%npc.name/[id]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }
            String arg1 = StringUtilsKt.substringAfter(input, "/");

            try {
                int id = Integer.parseInt(arg1);
                if (house.getNPC(id) != null) {
                    return house.getNPC(id).getName();
                }
            } catch (Exception e) {
                return "null";
            }
            return "null";
        }
    }

    private static class Location {
        public Location() {
            new X();
            new Y();
            new Z();
            new Coords();
        }

        private static class X extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%npc.location.x/[id]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                String[] a = input.split("/");
                if (a.length < 2) {
                    return "null";
                }
                String arg1 = StringUtilsKt.substringAfter(input, "/");

                try {
                    int id = Integer.parseInt(arg1);
                    if (house.getNPC(id) != null) {
                        if (house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation() != null) {
                            return String.valueOf(house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getX());
                        }
                        return String.valueOf(house.getNPC(id).getLocation().getX());
                    }
                } catch (Exception e) {
                    return "null";
                }
                return "null";
            }
        }

        private static class Y extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%npc.location.y/[id]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                String[] a = input.split("/");
                if (a.length < 2) {
                    return "null";
                }
                String arg1 = StringUtilsKt.substringAfter(input, "/");

                try {
                    int id = Integer.parseInt(arg1);
                    if (house.getNPC(id) != null) {
                        if (house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation() != null) {
                            return String.valueOf(house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getY());
                        }
                        return String.valueOf(house.getNPC(id).getLocation().getY());
                    }
                } catch (Exception e) {
                    return "null";
                }
                return "null";
            }
        }

        private static class Z extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%npc.location.z/[id]%";
            }

            @Override
            public boolean hasArgs() {
                return true;
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                String[] a = input.split("/");
                if (a.length < 2) {
                    return "null";
                }
                String arg1 = StringUtilsKt.substringAfter(input, "/");

                try {
                    int id = Integer.parseInt(arg1);
                    if (house.getNPC(id) != null) {
                        if (house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation() != null) {
                            return String.valueOf(house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getZ());
                        }
                        return String.valueOf(house.getNPC(id).getLocation().getZ());
                    }
                } catch (Exception e) {
                    return "null";
                }
                return "null";
            }
        }

        private static class Coords extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%npc.location.coords/[id]%";
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                String[] a = input.split("/");
                if (a.length < 2) {
                    return "null";
                }
                String arg1 = StringUtilsKt.substringAfter(input, "/");

                try {
                    int id = Integer.parseInt(arg1);
                    if (house.getNPC(id) != null) {
                        if (house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation() != null) {
                            return house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getX() + "," + house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getY() + "," + house.getNPC(id).getCitizensNPC().getOrAddTrait(CurrentLocation.class).getLocation().getZ();
                        }
                        return house.getNPC(id).getLocation().getX() + "," + house.getNPC(id).getLocation().getY() + "," + house.getNPC(id).getLocation().getZ();
                    }
                } catch (Exception e) {
                    return "null";
                }
                return "null";
            }
        }
    }

    private static class Health extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%npc.health/[id]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }
            String arg1 = StringUtilsKt.substringAfter(input, "/");

            try {
                int id = Integer.parseInt(arg1);
                if (house.getNPC(id) != null && house.getNPC(id).getCitizensNPC().getEntity() instanceof LivingEntity le) {
                    if (le.getMaxHealth() < le.getHealth()) { // To fix a rendering bug I believe
                        return String.valueOf(le.getMaxHealth());
                    }
                    return String.valueOf(le.getHealth());
                }
            } catch (Exception e) {
                return "null";
            }
            return "null";
        }
    }

    private static class MaxHealth extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%npc.maxhealth/[id]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }
            String arg1 = StringUtilsKt.substringAfter(input, "/");

            try {
                int id = Integer.parseInt(arg1);
                if (house.getNPC(id) != null && house.getNPC(id).getCitizensNPC().getOrAddTrait(ScaledMaxHealthTrait.class) != null) {
                    return String.valueOf(house.getNPC(id).getCitizensNPC().getOrAddTrait(ScaledMaxHealthTrait.class).getMaxHealth());
                }
            } catch (Exception e) {
                return "null";
            }
            return "null";
        }
    }

    private static class IsDead extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%npc.isdead/[id]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            String[] a = input.split("/");
            if (a.length < 2) {
                return "null";
            }
            String arg1 = StringUtilsKt.substringAfter(input, "/");

            try {
                int id = Integer.parseInt(arg1);
                if (house.getNPC(id) != null && house.getNPC(id).getCitizensNPC().getEntity() instanceof LivingEntity le) {
                    return String.valueOf(le.isDead());
                }
            } catch (Exception e) {
                return "null";
            }
            return "null";
        }
    }
}
