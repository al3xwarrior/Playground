package com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PermissionsMenu extends Menu {
    Main main;
    HousingWorld house;
    Group group;

    int currentPage = 1;

    public PermissionsMenu(Player player, Main main, HousingWorld house, Group group) {
        super(player, "&eEdit Permissions: " + group.getName(), 9 * 6);
        this.main = main;
        this.house = house;
        this.group = group;
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        HashMap<Permissions, Object> permissions = group.getPermissions();
        PaginationList<Permissions> paginationList = new PaginationList<>(Arrays.stream(Permissions.values()).toList(), 21);
        List<Permissions> perms = paginationList.getPage(currentPage);

        for (int i = 0; i < perms.size(); i++) {
            Permissions perm = perms.get(i);
            Object value = permissions.get(perm);
            String afterName = (value instanceof Boolean) ? ((Boolean) value ? "§aOn" : "§7Off") : String.valueOf(value);
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

            addItem(slots[i], item.build(), () -> {
                permissions.put(perm, perm.cycle(value, true));
                group.setPermissions(permissions);
                setupItems();
            }, () -> {
                permissions.put(perm, perm.cycle(value, false));
                group.setPermissions(permissions);
                setupItems();
            });
        }

        if (currentPage > 1) {
            addItem(45, ItemBuilder.create(Material.ARROW).name("§aPrevious Page").build(), () -> {
                currentPage--;
                setupItems();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, ItemBuilder.create(Material.ARROW).name("§aNext Page").build(), () -> {
                currentPage++;
                setupItems();
            });
        }

        addItem(49, ItemBuilder.create(Material.ARROW).name("§aBack").build(), () -> {
            new GroupEditMenu(player, main, house, group).open();
        });
    }
}
