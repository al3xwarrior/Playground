package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.Projectile;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SetGamemodeAction extends HTSLImpl {
    private Gamemodes gamemode;

    public SetGamemodeAction() {
        super("Set Gamemode Action");
        this.gamemode = Gamemodes.SURVIVAL;
    }

    public SetGamemodeAction(Gamemodes gamemode) {
        super("Launch Projectile Action");
        this.gamemode = Gamemodes.SURVIVAL;
    }

    @Override
    public String toString() {
        return "SetGamemodeAction (Gamemode: " + gamemode + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.DAYLIGHT_DETECTOR);
        builder.name("&eSet Gamemode Action");
        builder.info("&eSettings", "");
        builder.info("Gamemode", "&a" + gamemode.toString());

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DAYLIGHT_DETECTOR);
        builder.name("&aSet Gamemode Action");
        builder.description("Sets a players gamemode.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("gamemode",
                        ItemBuilder.create(Material.DAYLIGHT_DETECTOR)
                                .name("&eGamemode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + gamemode)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Projectile.values(), null
                )
        );

        return new ActionEditor(4, "&eSet Gamemode Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (player != null) {
            player.setGameMode(gamemode.getGameMode());
        }
        return true;
    }

    @Override
    public boolean mustBeSync() {
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("gamemode", gamemode);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "gamemode";
    }
}
