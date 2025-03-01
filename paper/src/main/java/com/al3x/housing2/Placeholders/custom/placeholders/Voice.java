package com.al3x.housing2.Placeholders.custom.placeholders;


import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.VoiceChat;
import org.bukkit.entity.Player;

public class Voice {
    public Voice() {
        new Group();
    }

    private static class Group extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%voice.group.name%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            String groupName = VoiceChat.getPlayerGroup(player);
            if (groupName == null) return "null";
            return groupName;
        }
    }
}
