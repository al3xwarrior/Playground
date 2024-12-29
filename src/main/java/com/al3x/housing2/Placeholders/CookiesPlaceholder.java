package com.al3x.housing2.Placeholders;

import com.al3x.housing2.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CookiesPlaceholder extends PlaceholderExpansion {

    private Main main;

    public CookiesPlaceholder(Main main) {
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cookies";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Al3x";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("global")) {
            return String.valueOf(main.getCookieManager().getTotalCookiesGiven());
        }
        return "0";
    }
}