package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AttackEvent {
    public AttackEvent() {
        new Attacker();
        new Victim();
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
                    return entity.getName();
                }

                return "null";
            }
        }
    }
}
