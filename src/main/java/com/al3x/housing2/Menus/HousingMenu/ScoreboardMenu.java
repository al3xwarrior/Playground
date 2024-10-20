package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class ScoreboardMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public ScoreboardMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Scoreboard"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        int[] avaliableSlots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29};

        List<String> scoreboard = house.getScoreboard();
        for (int i = 0; i < scoreboard.size(); i++) {
            final String[] line = {scoreboard.get(i)};

            int finalI = i;
            addItem(avaliableSlots[finalI], ItemBuilder.create(Material.PAPER)
                    .name("&eLine #" + finalI)
                    .description("\n&7Line: " + colorize(line[0]) + "\n\n&7How this line appears for you:\n" + colorize(parsePlaceholders(player, house, line[0])))
                    .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                    .build(), () -> {
                player.sendMessage(colorize("&ePlease enter the new string for this scoreboard line:"));
                openChat(main, (message) -> {
                    scoreboard.add(message);
                    player.sendMessage(colorize("&aLine set to: " + message));
                });
            }, () -> {
                scoreboard.remove(finalI);
                player.sendMessage(colorize("&aLine removed!"));
                new ScoreboardMenu(main, player, house).open();
            });
        }

        addItem(41, ItemBuilder.create(Material.PAPER)
                .name("&aAdd Line")
                .description("&7Add a new line to the scoreboard. The max lines are 15")
                .build(), () -> {
                    if (house.getScoreboard().size() >= 15) {
                        player.sendMessage(colorize("&cYou are at the max lines!"));
                        return;
                    }

                    List<String> newScoreboard = new ArrayList<>(house.getScoreboard());
                    newScoreboard.add("&eHello World!");
                    house.setScoreboard(newScoreboard);
                    new ScoreboardMenu(main, player, house).open();
                }
        );

        addItem(40, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), (e) -> new OwnerHousingMenu(main, player, house).open()
        );
    }
}
