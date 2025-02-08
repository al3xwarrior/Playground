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

import java.util.Map;

import static com.al3x.housing2.Enums.Colors.fromColorcode;
import static com.al3x.housing2.Utils.Color.colorize;

public class GroupEditMenu extends Menu {
    private final Map<String, String> colorStrings = Map.ofEntries(
            Map.entry("§a", "1391203d3752afc71d8f8cb0da8dbc7f13a3baafe0bf5f911cb21c38341f87db"),
            Map.entry("§b", "2d6a8b47da923b7d10142447fdbdcfd1e8e82eb484964252bb36ddb5f73b51c2"),
            Map.entry("§c", "ac14600ace50695c7c9bcf09e42afd9f53c9e20daa1524c95db4197dd3116412"),
            Map.entry("§d", "dcf2835180cbfec3b317d6a47491a74ae71435ba169a57925b9096ea2f9c61b6"),
            Map.entry("§e", "86f125004a8ffa6e4a4ec7b178606d0670c28a75b9cde59e011e66e91a66cf14"),
            Map.entry("§f", "8e0e8acabad27d4616fae9e472c0de60853d203c1c6f31367c939b619f3e3831"),
            Map.entry("§1", "e1194fe9edf583c0ebe7dc1d34309beefb229bb15b6a8c3c7b0c76de27c8b7bf"),
            Map.entry("§2", "1e38fa3131e2da04a8f8d50872a8232d7d9dea340d06c9097ffa3cc48208df1d"),
            Map.entry("§3", "31f57051130e850848e8e37e72110a16f09dbdab7d9d6e33a9fecfd348d5a110"),
            Map.entry("§4", "68d40935279771adc63936ed9c8463abdf5c5ba78d2e86cb1ec10b4d1d225fb"),
            Map.entry("§5", "9b82e72b8e4832e5a114ab0fc127c8acb83f31fd4d266d08b2cacc5b6401a400"),
            Map.entry("§6", "2090d09e173ee34138c3b01b48ee0be534bbb1ace0ddf5ff98e66f7b02113995"),
            Map.entry("§7", "1c8e0ddf2432f4332b87691b5952c7679763ef4f275b874e9bceb888ed5b5b9"),
            Map.entry("§8", "ff9bb9e56125c8227b94bbda9f6e0f862931c229255ba8f1205d13c44c1bb561"),
            Map.entry("§9", "3f9c32138c9764c639aebd819cd91992aed01bf448f0e710a03ab443ac490ee9")
    );

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
        addItem(slots[i], ItemBuilder.create(Material.NAME_TAG).name("&aRename Group")
                .description("Change the name of the group")
                .info("&7Current Name", "&7" + group.getName())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            if (!group.getName().equalsIgnoreCase("default") && !group.getName().equalsIgnoreCase("owner")) {
                player.sendMessage(colorize("&eEnter the new name for the group:"));
                openChat(main, group.getName(), (s) -> {
                    group.setName(s);
                });
            } else {
                player.sendMessage(colorize("&cYou cannot rename this group!"));
            }
        });
        i++;

        addItem(slots[i], ItemBuilder.create(Material.PAPER).name("&aChange Prefix")
                .description("Change the prefix of the group")
                .info("&7Current Prefix", "&7" + group.getPrefix())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
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
                .build(), () -> {
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
                .build(), () -> {
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
                .build(), () -> {
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
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new priority for the group:"));
            openChat(main, String.valueOf(group.getPriority()), (s) -> {
                group.setPriority(Integer.parseInt(s));
            });
        });
        i++;


        addItem(slots[i], ItemBuilder.create(Material.PLAYER_HEAD).name("&aEdit Permissions")
                .skullTexture(colorStrings.get(group.getColor()))
                .description("Change the permissions the players have within this group.")
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), () -> {
            if (!group.getName().equalsIgnoreCase("owner")) {
                new PermissionsMenu(player, main, house, group).open();
            } else {
                player.sendMessage(colorize("&cYou cannot edit this group's permissions!"));
            }
        });
        i++;//in case we add more items

        if (!group.getName().equalsIgnoreCase("owner") && house.getOwnerUUID().equals(player.getUniqueId())) {
            addItem(50, ItemBuilder.create(Material.DIAMOND).name("&aDefault Group")
                    .description("Set this group as the default group for new players.")
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                    .info("Current Default Group", "&a" + house.getDefaultGroup())
                    .build(), () -> {
                house.setDefaultGroup(group.getName());
            });
        }

        if (!group.getName().equalsIgnoreCase("owner") && !group.getName().equalsIgnoreCase("default")) {
            addItem(48, ItemBuilder.create(Material.TNT).name("&cDelete Group")
                    .description("Delete this group.")
                    .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                    .build(), () -> {
                house.getGroups().remove(group);
                new GroupsMenu(player, main, house).open();
            });
        }

        addItem(49, ItemBuilder.create(Material.ARROW).name("&aBack")
                .build(), () -> {
            new GroupsMenu(player, main, house).open();
        });
    }
}
