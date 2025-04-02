package com.al3x.housing2.Listeners;

import com.al3x.housing2.Data.PlayerData;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.EditPlayerMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.PlayerListingMenu;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.List;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;

import static com.al3x.housing2.Utils.Color.colorize;

public class SignClickEvent implements Listener {
    public SignClickEvent() {
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        // Only process right-click block interactions
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null) return;

        // Check if the clicked block is a sign
        if (!(block.getState() instanceof Sign sign)) return;

        // Using Paper API to get the side the player interacted with (Paper 1.20+)
        var signSide = sign.getTargetSide(e.getPlayer());
        List<Component> originalLines = signSide.lines();
        boolean modified = false;
        List<Component> newLines = new ArrayList<>();

        // Loop through each line and remove any click events
        for (Component line : originalLines) {
            if (hasClickEvent(line)) {
                // Remove click event while preserving other styling
                Component newLine = line.style(style -> style.clickEvent(null));
                newLines.add(newLine);
                modified = true;
            } else {
                newLines.add(line);
            }
        }

        // If any line was modified, update the sign using the new API method
        if (modified) {
            for (int i = 0; i < newLines.size(); i++) {
                signSide.line(i, newLines.get(i));
            }
            sign.update(true);
        }
    }

    // Recursive method to check if a Component (or any of its children) has a click event
    private boolean hasClickEvent(Component component) {
        if (component.clickEvent() != null) {
            return true;
        }
        for (Component child : component.children()) {
            if (hasClickEvent(child)) {
                return true;
            }
        }
        return false;
    }
}
