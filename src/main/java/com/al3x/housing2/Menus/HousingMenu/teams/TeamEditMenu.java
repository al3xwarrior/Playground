package com.al3x.housing2.Menus.HousingMenu.teams;

import com.al3x.housing2.Enums.Colors;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Team;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Enums.Colors.fromColorcode;
import static com.al3x.housing2.Utils.Color.colorize;

public class TeamEditMenu extends Menu {
    Main main;
    HousingWorld house;
    Team team;

    public TeamEditMenu(Player player, Main main, HousingWorld house, Team team) {
        super(player, "&eEdit team: " + team.getName(), 9 * 6);
        this.main = main;
        this.house = house;
        this.team = team;
    }

    @Override
    public void setupItems() {
        int[] slots = {11, 13, 15, 29, 31, 33};

        int i = 0;
        addItem(slots[i], ItemBuilder.create(Material.NAME_TAG).name("&aRename Team")
                .description("Change the name of the team")
                .info("&7Current Name", "&7" + team.getName())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {

            player.sendMessage(colorize("&eEnter the new name for the team:"));
            openChat(main, team.getName(), (s) -> {
                team.setName(s);
            });
        });
        i++;

        addItem(slots[i], ItemBuilder.create(Material.PAPER).name("&aChange Prefix")
                .description("Change the prefix of the team")
                .info("&7Current Prefix", "&7" + team.getPrefix())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new prefix for the team:"));
            openChat(main, team.getPrefix(), (s) -> {
                team.setPrefix(s);
            });
        });
        i++;


        addItem(slots[i], ItemBuilder.create(Material.PAPER).name("&aChange Suffix")
                .description("Change the suffix of the team")
                .info("&7Current Suffix", "&7" + team.getSuffix())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new suffix for the team:"));
            openChat(main, team.getSuffix(), (s) -> {
                team.setSuffix(s);
            });
        });
        i++;

        addItem(slots[i], ItemBuilder.create(Material.BOOK).name("&aChange Display Name")
                .description("Change the display name of the team")
                .info("&7Current Display Name", "&7" + team.getDisplayName())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new display name for the team:"));
            openChat(main, team.getDisplayName(), (s) -> {
                team.setDisplayName(s);
            });
        });
        i++;

        addItem(slots[i], ItemBuilder.create(fromColorcode(team.getColor()).getMaterial()).name("&aChange Color")
                .description("Change the color of the team")
                .info("&7Current Color", team.getColor() + StringUtilsKt.formatCapitalize(fromColorcode(team.getColor()).name()))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            new EnumMenu<>(main, "&7Select team Color", Colors.values(), null, player, house, this, (color) -> {
                team.setColor(color.getColorcode());
                open();
            }).open();
        });
        i++;

    addItem(slots[i], ItemBuilder.create(Material.IRON_SWORD).name("&aToggle Friendly Fire")
                .description("If disabled, players on this team cannot attack each other.")
                .info("&7Current", (team.isFriendlyFire() ? "&aEnabled" : "&cDisabled"))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), () -> {
            team.setFriendlyFire(!team.isFriendlyFire());
            setupItems();
        });
        i++;

        addItem(48, ItemBuilder.create(Material.TNT).name("&cDelete Team")
                .description("Delete this team.")
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), () -> {
            house.getTeams().remove(team);
            new TeamsMenu(player, main, house).open();
        });

        addItem(49, ItemBuilder.create(Material.ARROW).name("&aBack")
                .build(), () -> {
            new TeamsMenu(player, main, house).open();
        });
    }
}
