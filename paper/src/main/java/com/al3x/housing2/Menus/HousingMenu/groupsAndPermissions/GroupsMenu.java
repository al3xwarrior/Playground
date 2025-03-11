package com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions;

import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
import com.al3x.housing2.Menus.HousingMenu.regions.RegionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_REGION;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_YELLOW;

public class GroupsMenu extends Menu {
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

    private int currentPage = 1;

    public GroupsMenu(Player player, Main main, HousingWorld house) {
        super(player, "&7Groups Menu", 9 * 6);
        this.main = main;
        this.house = house;
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Group> paginationList = new PaginationList<>(house.getGroups(), slots.length);
        List<Group> groups = paginationList.getPage(currentPage);
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                Group group = groups.get(i);
                ItemBuilder item = ItemBuilder.create(Material.PLAYER_HEAD);
                item.skullTexture(colorStrings.get(group.getColor()));
                item.name(colorize(group.getName()));
                item.description("Edit the \"" + group.getName() + "\" &7group permissions, name, tag and more.");
                item.lClick(EDIT_YELLOW);
                addItem(slots[i], item.build(), () -> {
                    new GroupEditMenu(player, main, house, group).open();
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build());
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), () -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), () -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new HousingMenu(main, player, house).open();
        });

        addItem(50, new ItemBuilder().material(Material.OAK_SIGN).name(colorize("&aCreate Group")).build(), () -> {
            player.sendMessage(colorize("&eEnter the name of the group:"));
            openChat(main, (s) -> {
                // Create region
                if (house.createGroup(s) != null) {
                    player.sendMessage(colorize("&aGroup created with name: " + s));
                    setupItems();
                } else {
                    player.sendMessage(colorize("&cThat name is already in use by another group!"));
                }
            });
        });
    }
}
