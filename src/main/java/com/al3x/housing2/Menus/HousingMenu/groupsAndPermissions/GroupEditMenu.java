package com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions;

import com.al3x.housing2.Enums.Colors;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Enums.Colors.fromColorcode;
import static com.al3x.housing2.Utils.Color.colorize;

public class GroupEditMenu extends Menu {
    Main main;
    HousingWorld house;
    Group group;

    public GroupEditMenu(Player player, Main main, HousingWorld house, Group group) {
        super(player, "&eEdit Group: " + group.getName(), 9 * 6);
        this.main = main;
        this.house = house;
        this.group = group;

    }

    @Override
    public void setupItems() {
        int[] slots = {10, 12, 14, 16, 29, 31, 33};

        int i = 0;
        if (!group.getName().equalsIgnoreCase("default") && !group.getName().equalsIgnoreCase("owner")) {
            addItem(slots[i], ItemBuilder.create(Material.NAME_TAG).name("&aRename Group")
                    .description("Change the name of the group")
                    .info("&7Current Name", "&7" + group.getName())
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                    .build(), e -> {

                player.sendMessage(colorize("&eEnter the new name for the group:"));
                openChat(main, group.getName(), (s) -> {
                    group.setName(s);
                });
            });
            i++;
        }

        addItem(slots[i], ItemBuilder.create(Material.PAPER).name("&aChange Prefix")
                .description("Change the prefix of the group")
                .info("&7Current Prefix", "&7" + group.getPrefix())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new prefix for the group:"));
            openChat(main, group.getPrefix(), (s) -> {
                group.setPrefix(s);
            });
        });
        i++;


        addItem(slots[i], ItemBuilder.create(Material.PAPER).name("&aChange Suffix")
                .description("Change the suffix of the group")
                .info("&7Current Suffix", "&7" + group.getSuffix())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new suffix for the group:"));
            openChat(main, group.getSuffix(), (s) -> {
                group.setSuffix(s);
            });
        });
        i++;

        addItem(slots[i], ItemBuilder.create(Material.BOOK).name("&aChange Display Name")
                .description("Change the display name of the group")
                .info("&7Current Display Name", "&7" + group.getDisplayName())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new display name for the group:"));
            openChat(main, group.getDisplayName(), (s) -> {
                group.setDisplayName(s);
            });
        });
        i++;

        addItem(slots[i], ItemBuilder.create(fromColorcode(group.getColor()).getMaterial()).name("&aChange Color")
                .description("Change the color of the group")
                .info("&7Current Color", group.getColor() + StringUtilsKt.formatCapitalize(fromColorcode(group.getColor()).name()))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), e -> {
            new EnumMenu<>(main, "&7Select Group Color", Colors.values(), null, player, house, this, (color) -> {
                group.setColor(color.getColorcode());
                open();
            }).open();
        });
        i++;

        addItem(slots[i], ItemBuilder.create(Material.GOLD_INGOT).name("&aChange Priority")
                .description("Change the priority of the group")
                .info("&7Current Priority", "&7" + group.getPriority())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new priority for the group:"));
            openChat(main, String.valueOf(group.getPriority()), (s) -> {
                group.setPriority(Integer.parseInt(s));
            });
        });
        i++;

        if (!group.getName().equalsIgnoreCase("owner")) {
            addItem(slots[i], ItemBuilder.create(Material.PLAYER_HEAD).name("&aEdit Permissions")
                    .skullTexture("86f125004a8ffa6e4a4ec7b178606d0670c28a75b9cde59e011e66e91a66cf14")
                    .description("Change the permissions the players have within this group.")
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                    .build(), e -> {
                new PermissionsMenu(player, main, house, group).open();
            });
            i++;//in case we add more items
        }

        if (!group.getName().equalsIgnoreCase("owner") && house.getOwnerUUID().equals(player.getUniqueId())) {
            addItem(50, ItemBuilder.create(Material.DIAMOND).name("&aDefault Group")
                    .description("Set this group as the default group for new players.")
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                    .info("Current Default Group", "&a" + house.getDefaultGroup())
                    .build(), e -> {
                house.setDefaultGroup(group.getName());
            });
        }

        if (!group.getName().equalsIgnoreCase("owner") && !group.getName().equalsIgnoreCase("default")) {
            addItem(48, ItemBuilder.create(Material.TNT).name("&cDelete Group")
                    .description("Delete this group.")
                    .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                    .build(), e -> {
                house.getGroups().remove(group);
                new GroupsMenu(player, main, house).open();
            });
        }

        addItem(49, ItemBuilder.create(Material.ARROW).name("&aBack")
                .build(), e -> {
            new GroupsMenu(player, main, house).open();
        });
    }
}
