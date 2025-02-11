package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;
import static com.al3x.housing2.Utils.Color.colorize;

public class CookieManager {

    private final Main main;
    private final File dataFile;
    private final FileConfiguration config;
    private int totalCookiesGiven;
    private int week;

    private static ItemStack adminCookie;
    private static ItemStack defaultCookie;
    static {
        adminCookie = ItemBuilder.create(Material.COOKIE)
                .name("&cAdmin Cookies &7(Right-Click)")
                .description("&7Right Click to give the owner of this house a pack of cookies!\n\n&7Cookies are used as a rating system, other players can sort homes based on them!\n\n&7Cookies reset every week!\n\n&7A pack of &cAdmin Cookies &7contains &c25 &7cookies.")
                .build();
        NbtItemBuilder adminNbt = new NbtItemBuilder(adminCookie);
        adminNbt.setBoolean("housing_cookie", true);
        adminNbt.build();

        defaultCookie = ItemBuilder.create(Material.COOKIE)
                .name("&7Default Cookies (Right-Click)")
                .description("&7Right Click to give the owner of this house a pack of cookies!\n\n&7Cookies are used as a rating system, other players can sort homes based on them!\n\n&7Cookies reset every week!\n\n&7A pack of Default Cookies contains 1 cookie.")
                .build();
        NbtItemBuilder defaultNbt = new NbtItemBuilder(defaultCookie);
        defaultNbt.setBoolean("housing_cookie", true);
        defaultNbt.build();
    }


    /**
     * Give the player the cookie item
     * @param player The player to give the cookie to
     */
    public static void givePhysicalCookie(Player player) {
        if (player.hasPermission("housing2.admin")) {
            player.getInventory().setItem(4, adminCookie);
            return;
        }
        player.getInventory().setItem(4, defaultCookie);
    }

    public CookieManager(Main main, File pluginDataFolder) {
        this.main = main;

        // Initialize the data file
        this.dataFile = new File(pluginDataFolder, "cookies.yml");
        this.config = YamlConfiguration.loadConfiguration(dataFile);

        // Load the totalCookiesGiven value, defaulting to 0 if not found
        this.totalCookiesGiven = config.getInt("totalCookiesGiven", 0);
        this.week = config.getInt("week", 0);
    }

    public int getCookiesToGive(Player player) {
        if (player.hasPermission("housing2.admin")) {
            return 5;
        }
        return 1;
    }

    public void giveCookie(Player player, HousingWorld house) {
        if (player.getUniqueId().equals(house.getOwnerUUID())) {
            player.sendMessage(colorize("&cYou cannot give yourself cookies!"));
            return;
        }

        if (house.playerGivenCookies(player)) {
            player.sendMessage(colorize("&cYou have already given cookies to this house this week!"));
            return;
        }

        house.giveCookies(player, getCookiesToGive(player));
        incrementCookiesGiven();

        sendEventExecution(EventType.GIVE_COOKIE, player, house, null);
    }

    private void incrementCookiesGiven() {
        this.totalCookiesGiven++;
        saveData();
    }

    public int getTotalCookiesGiven() {
        return totalCookiesGiven;
    }

    public int getWeek() {
        return week;
    }

    private void saveData() {
        config.set("totalCookiesGiven", totalCookiesGiven);
        config.set("week", week);
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newWeek() {
        week++;
        saveData();

        ConcurrentHashMap<String, HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses();
        for (HousingWorld house : houses.values()) {
            house.setCookieWeek(week);
            house.clearCookies();
        }

    }
}