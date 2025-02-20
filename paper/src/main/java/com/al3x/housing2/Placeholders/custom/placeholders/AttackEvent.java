package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AttackEvent {
    public AttackEvent() {
        new Attacker();
        new Victim();
        new Damage();
    }

    private static class Attacker extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.attack/attacker%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastAttacked.containsKey(player.getUniqueId())) return player.getName();
            return "null";
        }
    }

    private static class Victim extends Placeholder {
        public Victim() {
            new Type();
        }
        @Override
        public String getPlaceholder() {
            return "%event.attack/victim%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastAttacked.containsKey(player.getUniqueId())) {
                Entity entity = com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastAttacked.get(player.getUniqueId());
                return entity.getName();
            }

            return "null";
        }

        private static class Type extends Placeholder {
            @Override
            public String getPlaceholder() {
                return "%event.attack/victim.type%";
            }

            @Override
            public String handlePlaceholder(String input, HousingWorld house, Player player) {
                if (player == null) {
                    return "null";
                }
                if (com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastAttacked.containsKey(player.getUniqueId())) {
                    Entity entity = com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastAttacked.get(player.getUniqueId());
                    if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        return "NPC";
                    } else {
                        return entity.getType().name();
                    }
                }

                return "null";
            }
        }
    }

    private static class Damage extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.attack.damage%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastDamage.containsKey(player.getUniqueId())) {
                double damage = com.al3x.housing2.Listeners.HouseEvents.AttackEvent.lastDamage.get(player.getUniqueId()).getDamage();
                return String.valueOf(damage);
            }

            return "null";
        }
    }
}
