package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent {
    public ChatEvent() {
        new Message();
    }

    private static class Message extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%event.chat/message%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (com.al3x.housing2.Listeners.HouseEvents.ChatEvent.lastChatEvent.containsKey(player.getUniqueId())) {
                AsyncPlayerChatEvent event = com.al3x.housing2.Listeners.HouseEvents.ChatEvent.lastChatEvent.get(player.getUniqueId());
                return event.getMessage();
            }
            return "null";
        }
    }
}
