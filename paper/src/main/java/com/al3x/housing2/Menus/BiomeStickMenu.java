package com.al3x.housing2.Menus;

import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class BiomeStickMenu extends Menu {

    private static HashMap<UUID, Duple<Biome, Integer>> biomeMap = new HashMap<>();

    public static void setBiomeMap(Player player, Biome biome, int range) {
        biomeMap.put(player.getUniqueId(), new Duple<>(biome, range));
    }

    public static Biome getPlayerBiomeStickBiome(Player player) {
        if (!biomeMap.containsKey(player.getUniqueId())) {
            setBiomeMap(player, Biome.PLAINS, 5);
        }
        return biomeMap.get(player.getUniqueId()).getFirst();
    }

    public static int getPlayerBiomeStickRange(Player player) {
        if (!biomeMap.containsKey(player.getUniqueId())) {
            setBiomeMap(player, Biome.PLAINS, 5);
        }
        return biomeMap.get(player.getUniqueId()).getSecond();
    }

    private Player player;
    private int page;
    private String search = "";
    int[] avaliableSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public BiomeStickMenu(Player player) {
        super(player, "&7Biome Stick", 54);
        this.player = player;
        this.page = 1;
        setupItems();
    }

    @Override
    public void setupItems() {
        PaginationList<Biome> paginationList = getBiomes();
        List<Biome> biomeList = paginationList.getPage(page);

        for (int i = 0; i < biomeList.size(); i++) {
            Biome biome = biomeList.get(i);

            ItemBuilder icon = ItemBuilder.create(Material.GRASS_BLOCK)
                    .name("&a" + biome.name())
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW);

            addItem(avaliableSlots[i], icon.build(), () -> {
                setBiomeMap(player, biome, getPlayerBiomeStickRange(player));
                player.sendMessage(colorize("&aBiome stick setting set to " + biome.name()));
                player.closeInventory();
            });
        }

        addItem(49, ItemBuilder.create(Material.BARRIER)
                .name("&cCancel")
                .description("&eClick to cancel")
                .build(), () -> {
            player.closeInventory();
        });

        addItem(50, ItemBuilder.create(Material.CHAIN_COMMAND_BLOCK)
                .name("&aRange")
                .description("&7Currently: &6" + getPlayerBiomeStickRange(player) + "\n\n&e&lCLICK TO INCREASE\n&e&lRIGHT-CLICK TO DECREASE")
                .punctuation(false)
                .build(), () -> {
                    setBiomeMap(player, getPlayerBiomeStickBiome(player), Math.min(getPlayerBiomeStickRange(player) + 1, 10));
                    new BiomeStickMenu(player).open();
                }, () -> {
                    setBiomeMap(player, getPlayerBiomeStickBiome(player), Math.max(getPlayerBiomeStickRange(player) - 1, 1));
            new BiomeStickMenu(player).open();
                }
        );

        if (paginationList.getPageCount() > 1) {
            ItemBuilder forwardArrow = new ItemBuilder();
            forwardArrow.material(Material.ARROW);
            forwardArrow.name("&aNext Page");
            forwardArrow.description("&ePage " + (page + 1));
            addItem(53, forwardArrow.build(), () -> {
                if (page + 1 > paginationList.getPageCount()) return;
                page++;
                open();
            });
        }

        if (page > 1) {
            ItemBuilder backArrow = new ItemBuilder();
            backArrow.material(Material.ARROW);
            backArrow.name("&aPrevious Page");
            backArrow.description("&ePage " + (page - 1));
            addItem(45, backArrow.build(), () -> {
                if (page - 1 < 1) return;
                page--;
                open();
            });
        }
    }

    private PaginationList<Biome> getBiomes() {
        List<Biome> biomeArray = new ArrayList<>(List.of(Biome.values())); // frick you depricated

        if (search != null) {
            biomeArray = biomeArray.stream().filter(biome -> StringUtilsKt.removeStringFormatting(biome.name()).contains(search.toLowerCase())).toList();
        }

        return new PaginationList<>(biomeArray, avaliableSlots.length);
    }
}
