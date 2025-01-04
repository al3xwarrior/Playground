package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HasPermissionCondition extends Condition {
    private Permissions permission = null;

    public HasPermissionCondition() {
        super("Has Permission");
    }

    @Override
    public String toString() {
        return "PermissionRequirementCondition (permission: " + (permission == null ? "&aNot Set" : "&6" + permission) + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.FILLED_MAP);
        builder.name("&aRequired Permission");
        builder.description("Requires the user to have the specified permission.");
        builder.info("&eSettings", "");
        builder.info("Permission", (permission == null ? "&aNot Set" : "&6" + permission));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.FILLED_MAP);
        builder.name("&eRequired Permission");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("permission",
                        ItemBuilder.create(Material.FILLED_MAP)
                                .name("&ePermission")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (permission == null ? "Not Set" : permission))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        (e, o) -> {
                            List<Duple<Permissions, ItemBuilder>> permissions = new ArrayList<>();
                            for (Permissions type : Permissions.values()) {
                                permissions.add(new Duple<>(type, ItemBuilder.create(Material.PLAYER_HEAD)
                                        .skullTexture("86f125004a8ffa6e4a4ec7b178606d0670c28a75b9cde59e011e66e91a66cf14")
                                        .name("&6" + type.getDisplayName())));
                            }
                            new PaginationMenu<>(Main.getInstance(),
                                    "&eSelect a permission", permissions,
                                    (Player) e.getWhoClicked(), house, backMenu, (permission) -> {
                                this.permission = permission;
                                backMenu.open();
                            }).open();
                            return true;
                        }
                )
        );
        return new ActionEditor(4, "Required Permission", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return permission == null || house.hasPermission(player, permission);
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("permission", permission);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
