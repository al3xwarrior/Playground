package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.StringUtilsKt;
import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import me.catcoder.sidebar.text.TextIterators;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingScoreboard {
    Sidebar<Component> sidebar;
    private HousingWorld house;
    Main main = Main.getInstance();

    public HousingScoreboard(HousingWorld house) {
        this.house = house;
        createBoard();
    }

    public void createBoard() {
        if (sidebar != null) {
            sidebar.destroy();
        }

        this.sidebar = ProtocolSidebar.newAdventureSidebar(Component.text("PLAYGROUND").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), main);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();

        sidebar.addLine(Component.text(dtf.format(now)).color(NamedTextColor.GRAY).append(Component.text(" mega1A").color(NamedTextColor.GRAY)));
        sidebar.addBlankLine();
        List<String> scoreboard = house.getScoreboard();
        for (int i = 0; i < scoreboard.size(); i++) {
            String line = scoreboard.get(i);
            if (i > 14) {
                break;
            }
            sidebar.addUpdatableLine(player -> StringUtilsKt.housingStringFormatter(line, house, player));
        }
        sidebar.updateLinesPeriodically(0, 20);
        for (Player player : house.getWorld().getPlayers()) {
            sidebar.addViewer(player);
        }
    }

    public void addPlayer(Player player) {
        sidebar.addViewer(player);
    }

    public void removePlayer(Player player) {
        sidebar.removeViewer(player);
    }
}
