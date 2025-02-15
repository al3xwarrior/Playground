package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.adventure.SternalBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HousingScoreboard {

    private HousingWorld house;
    private static Main main = Main.getInstance();

    public HousingScoreboard(HousingWorld house) {
        this.house = house;
    }

    public static void updateScoreboard(Player player) {
        SternalBoard board = new SternalBoard(player);

        if (main.getHousesManager().getHouse(player.getWorld()) != null) {
            HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
            board.updateTitle(StringUtilsKt.housingStringFormatter(house.getScoreboardTitle(), house, player));
            board.updateLines(house.getScoreboard().stream()
                    .map((l) -> StringUtilsKt.housingStringFormatter(l, house, player))
                    .toList());
        } else {
            board.updateTitle(StringUtilsKt.housingStringFormatter("<gradient:gold:green><b>ᴘʟᴀʏɢʀᴏᴜɴᴅ"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDateTime now = LocalDateTime.now();
            board.updateLines(
                    Component.text("§7" + dtf.format(now) + " mega1A"),
                    Component.text(""),
                    Component.text("§fWelcome to the playground!"),
                    Component.empty(),
                    Component.text("§fYou are in the"),
                    Component.text("§flobby!")
            );
        }
    }
}
