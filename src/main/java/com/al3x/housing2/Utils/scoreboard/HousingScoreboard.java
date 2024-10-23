package com.al3x.housing2.Utils.scoreboard;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingScoreboard {
    public static HashMap<UUID, FastBoard> boards = new HashMap<>();

    public void setScoreboard(Player p) {
        Main main = Main.getInstance();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();

        FastBoard board = new FastBoard(p);
        board.updateTitle(colorize("&e&lHOUSING TUAH"));

        if (main.getHousesManager().getHouse(p.getWorld()) == null) {
            ArrayList<String> lines = new ArrayList<>();
            lobbyThing(p, dtf, now).forEach(line -> lines.add(colorize(line)));
            board.updateLines(lines);
            return;
        } else {
            HousingWorld house = main.getHousesManager().getHouse(p.getWorld());
            ArrayList<String> lines = new ArrayList<>();
            houseThing(p, house, dtf, now).forEach(line -> lines.add(colorize(line)));
            board.updateLines(lines);
        }

        boards.put(p.getUniqueId(), board);
    }

    private static List<String> houseThing(Player p, HousingWorld house, DateTimeFormatter dtf, LocalDateTime now) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("&7" + dtf.format(now) + " mega1A");
        lines.add("");
        for (String line : house.getScoreboard()) {
            lines.add(HandlePlaceholders.parsePlaceholders(p, house, line));
        }
        return lines;
    }

    public static void updateScoreboard(Player p) {
        Main main = Main.getInstance();
        if (!boards.containsKey(p.getUniqueId())) {
            new HousingScoreboard().setScoreboard(p);
            return;
        }

        FastBoard board = boards.get(p.getUniqueId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();

        if (main.getHousesManager().getHouse(p.getWorld()) == null) {
            ArrayList<String> lines = new ArrayList<>();
            lobbyThing(p, dtf, now).forEach(line -> lines.add(colorize(line)));
            board.updateLines(lines);
            return;
        } else {
            HousingWorld house = main.getHousesManager().getHouse(p.getWorld());
            ArrayList<String> lines = new ArrayList<>();
            houseThing(p, house, dtf, now).forEach(line -> lines.add(colorize(line)));
            board.updateLines(lines);
        }
    }

    private static List<String> lobbyThing(Player p, DateTimeFormatter dtf, LocalDateTime now) {
        return List.of(
                "&7" + dtf.format(now) + " mega1A",
                "",
                "&fWelcome to Housing",
                "&fTuah!",
                "",
                "&fYou are in the",
                "&flobby!"
        );
    }


}
