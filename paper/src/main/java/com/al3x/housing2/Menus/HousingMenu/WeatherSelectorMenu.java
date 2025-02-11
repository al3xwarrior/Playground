package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Enums.WeatherTypes;
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

public class WeatherSelectorMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    public WeatherSelectorMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Weather Selector"), 5*9);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {

        addItem(11, ItemBuilder.create(Material.SUNFLOWER)
                .name(colorize("&aSunny"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.SUNNY);
            house.getWorld().setStorm(false);
            house.getWorld().setThundering(false);
            setupItems();
        });
        addItem(20, ItemBuilder.create(house.getWeather() == WeatherTypes.SUNNY ? Material.LIME_DYE : Material.GRAY_DYE)
                .name(colorize("&aSunny"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.SUNNY);
            house.getWorld().setStorm(false);
            house.getWorld().setThundering(false);
            setupItems();
        });

        addItem(12, ItemBuilder.create(Material.WATER_BUCKET)
                .name(colorize("&aRaining"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.RAINING);
            house.getWorld().setStorm(true);
            house.getWorld().setThundering(false);
            setupItems();
        });
        addItem(21, ItemBuilder.create(house.getWeather() == WeatherTypes.RAINING ? Material.LIME_DYE : Material.GRAY_DYE)
                .name(colorize("&aRaining"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.RAINING);
            house.getWorld().setStorm(true);
            house.getWorld().setThundering(false);
            setupItems();
        });

        addItem(14, ItemBuilder.create(Material.FIRE_CHARGE)
                .name(colorize("&aStorming"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.STORMING);
            house.getWorld().setStorm(true);
            house.getWorld().setThundering(true);
            setupItems();
        });
        addItem(23, ItemBuilder.create(house.getWeather() == WeatherTypes.STORMING ? Material.LIME_DYE : Material.GRAY_DYE)
                .name(colorize("&aStorming"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.STORMING);
            house.getWorld().setStorm(true);
            house.getWorld().setThundering(true);
            setupItems();
        });

        addItem(15, ItemBuilder.create(Material.BLAZE_ROD)
                .name(colorize("&aThunder"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.THUNDER);
            house.getWorld().setStorm(false);
            house.getWorld().setThundering(true);
            setupItems();
        });
        addItem(24, ItemBuilder.create(house.getWeather() == WeatherTypes.THUNDER ? Material.LIME_DYE : Material.GRAY_DYE)
                .name(colorize("&aThunder"))
                .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                .build(), (e) -> {
            house.setWeather(WeatherTypes.THUNDER);
            house.getWorld().setStorm(false);
            house.getWorld().setThundering(true);
            setupItems();
        });

        addItem(39, ItemBuilder.create(Material.COMPASS)
                .name(colorize("&aToggle Weather Cycle"))
                .description("Toggle whether the weather cycle should run in this house.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), (e) -> {
            if (house.getWeatherCycle()) {
                house.setWeatherCycle(false);
                player.sendMessage(colorize("&eDisabled &cWeather Cycle"));
            } else {
                house.setWeatherCycle(true);
                player.sendMessage(colorize("&eEnabled &aWeather Cycle"));
            }
            house.getWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, house.getWeatherCycle());
        });

        addItem(40, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), (e) -> new HouseSettingsMenu(main, player, house).open()
        );
    }
}