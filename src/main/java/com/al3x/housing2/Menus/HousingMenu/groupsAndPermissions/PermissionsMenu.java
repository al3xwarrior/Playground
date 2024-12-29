package com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PermissionsMenu extends PaginationMenu<Permissions> {
    Main main;
    HousingWorld house;
    Group group;

    public PermissionsMenu(Player player, Main main, HousingWorld house, Group group) {
        super(main, "&eEdit Permissions: " + group.getName(), player, house);
        this.main = main;
        this.house = house;
        this.group = group;

        setItems(getItems(group));
        setBackMenu(new GroupEditMenu(player, main, house, group));
        setConsumer((perm) -> {
            group.getPermissions().compute(perm, (k, value) -> perm.cycle(value));
            setupItems();
        });
    }

    private static List<Duple<Permissions, ItemBuilder>> getItems (Group group) {
        HashMap<Permissions, Object> permissions = group.getPermissions();
        return Arrays.stream(Permissions.values()).map(perm -> {
            Object value = permissions.get(perm);
            String afterName = (value instanceof Boolean) ? ((Boolean) value ? "§aOn" : "§7Off") : value.toString();
            ItemBuilder item = ItemBuilder.create(Material.PLAYER_HEAD)
                    .skullTexture("86f125004a8ffa6e4a4ec7b178606d0670c28a75b9cde59e011e66e91a66cf14")
                    .name("&a" + perm.getDisplayName() + ": " + afterName)
                    .description(perm.getDescription());
            if (value instanceof Boolean) {
                item.lClick(ItemBuilder.ActionType.TOGGLE_YELLOW);
            } else {
                item.lClick(ItemBuilder.ActionType.CYCLE_FORWARD);
                item.rClick(ItemBuilder.ActionType.CYCLE_BACKWARD);
            }

            return new Duple<>(perm, item);
        }).toList();
    }
}