package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        int[] avaliableSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29};

        List<String> scoreboard = house.getScoreboard();
        for (int i = 0; i < scoreboard.size(); i++) {
            final String[] line = {scoreboard.get(i)};

            int finalI = i;
            boolean hasPlaceholders = line[0].contains("%");
            addItem(avaliableSlots[finalI], ItemBuilder.create(Material.PAPER)
                    .name("&eLine #" + finalI)
                    .description("\n&eLine: \n" + colorize(line[0]) + (hasPlaceholders ? "\n\n&eHow this line appears for you:\n" + colorize(parsePlaceholders(player, house, line[0])) : ""))
                    .punctuation(false)
                    .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                    .rClick(ItemBuilder.ActionType.DELETE_YELLOW)
                    .shiftClick()
                    .build(), () -> {

                        player.sendMessage(colorize("&ePlease enter the new string for this scoreboard line:"));
                        openChat(main, line[0], (message) -> {
                            scoreboard.set(finalI, message);
                            house.getScoreboardInstance().createBoard();
                        });

                    }, () -> {
                        scoreboard.remove(finalI);
                        house.getScoreboardInstance().createBoard();
                        house.setScoreboard(scoreboard);
                        player.sendMessage(colorize("&cLine removed!"));
                        Bukkit.getScheduler().runTask(main, () -> new ScoreboardMenu(main, player, house).open());
                    }, (e) -> {
                        shiftLine(line[0], finalI, e.isRightClick());
                        house.getScoreboardInstance().createBoard();
                    }
            );
        }

        addItem(41, ItemBuilder.create(Material.PAPER)
                        .name("&aAdd Line")
                        .description("&7Add a new line to the scoreboard. The max lines are 15")
                        .build(), () -> {
                    if (house.getScoreboard().size() >= 15) {
                        player.sendMessage(colorize("&cYou are at the max lines!"));
                        return;
                    }

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);


                    List<String> newScoreboard = new ArrayList<>(house.getScoreboard());
                    newScoreboard.add("&eHello World!");
                    house.setScoreboard(newScoreboard);
                    house.getScoreboardInstance().createBoard();
                    new ScoreboardMenu(main, player, house).open();
                }
        );

        addItem(40, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), (e) -> new SystemsMenu(main, player, house).open()
        );
    }

    public void shiftLine(String action, int index, boolean forward) {
        List<String> actions = house.getScoreboard();

        if (actions == null || actions.size() < 2) return;

        actions.remove(index);

        if (forward) {
            actions.add((index == actions.size()) ? 0 : index + 1, action);
        } else {
            actions.add((index == 0) ? actions.size() : index - 1, action);
        }

        setupItems();
    }
}
