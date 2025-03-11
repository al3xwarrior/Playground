package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseSettingsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class TimeSelectorMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    public TimeSelectorMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Time Selector"), 5*9);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void initItems() {

        for (int i = 0; i < 9; i++) {
            int finalI = i;
            int i1 = (i * 3 + 6) % 12;
            i1 = i1 == 0 ? 12 : i1;
            String morning = (i * 3 + 6) % 24 < 12 ? "am" : "pm";

            addItem(9 + i, ItemBuilder.create(Material.CLOCK)
                    .name(colorize(String.format("&a%s %s", i1, morning)))
                    .extraLore(colorize(String.format("&eClick to set to %s %s", i1, morning)))
                    .build(), (e) -> {
                house.setIngameTime(3000L * finalI);
                house.getWorld().setTime(3000L * finalI);
                setupItems();
            });
            addItem(18 + i, ItemBuilder.create(house.getIngameTime() == 3000L * i ? Material.LIME_DYE : Material.GRAY_DYE)
                    .name(colorize(String.format("&a%s %s", i1, morning)))
                    .extraLore(colorize(String.format("&eClick to set to %s %s", i1, morning)))
                    .build(), (e) -> {
                house.setIngameTime(3000L * finalI);
                house.getWorld().setTime(3000L * finalI);
                setupItems();
            });
        }

        addItem(39, ItemBuilder.create(Material.DAYLIGHT_DETECTOR)
                .name(colorize("&aToggle Daylight Cycle"))
                .description("Toggle whether the daylight cycle should run in this house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), (e) -> {
            if (house.getDaylightCycle()) {
                house.setDayLightCycle(false);
                player.sendMessage(colorize("&eDisabled &cDaylight Cycle"));
            } else {
                house.setDayLightCycle(true);
                player.sendMessage(colorize("&eEnabled &aDaylight Cycle"));
            }
            house.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, house.getDaylightCycle());
        });

        addItem(40, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), (e) -> new HouseSettingsMenu(main, player, house).open()
        );

        addItem(41, ItemBuilder.create(Material.OAK_SIGN)
                .name(colorize("&eSet Time"))
                .description("Manually enter in the time")
                .build(), (e) -> {
            player.sendMessage(colorize("&eType the time you would like it to be"));
            openChat(main, String.valueOf(house.getIngameTime()), (message) -> {
                try {
                    Long time = Long.parseLong(message);

                    if (time < 0) {
                        player.sendMessage(colorize("&cInvalid Input!"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }

                    Bukkit.getScheduler().runTask(main, () -> {
                        house.setIngameTime(time);
                        house.getWorld().setTime(time);
                    });
                } catch (NumberFormatException err) {
                    player.sendMessage(colorize("&cInvalid Input!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            });
        });
    }
}