package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.JukeBoxManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;

public class JukeboxMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private int page;
    private String search = "";
    int[] avaliableSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public JukeboxMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Jukebox"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.page = 1;
        setupItems();
    }

    @Override
    public void initItems() {
        List<String> songNames = JukeBoxManager.getSongNames();
        if (songNames == null) {
            addItem(22, ItemBuilder.create(Material.BEDROCK)
                    .name("&cNo songs found!")
                    .description("&7There are no songs found plugins songs folder.")
                    .build());
            return;
        }

        PaginationList<Song> paginationList = getSongs();
        List<Song> songList = paginationList.getPage(page);

        for (int i = 0; i < songList.size(); i++) {
            Song song = songList.get(i);
            if (song == null) {
                continue;
            }

            addItem(avaliableSlots[i], ItemBuilder.create(Material.JUKEBOX)
                    .name("&7" + song.getTitle())
                    .description("&7Author: &f" + song.getAuthor() + "\n&7Description: &f" + song.getDescription() + "\n&7Length: &f" + formattedTime(song.getLength()) + "s\n\n&e&lClick to " + (!house.isJukeboxPlaying() || !house.songInPlaylist(song) ? "&a&lAdd" : "&c&lRemove") + " &e&lthis song to the playlist!")
                    .glow(house.isJukeboxPlaying() && house.songInPlaylist(song))
                    .punctuation(false)
                    .build(), () -> {
                if (house.isJukeboxPlaying() && house.songInPlaylist(song)) {
                    house.removeSong(song);
                    open();
                    Bukkit.getLogger().info(house.isJukeboxPlaying() + " " + house.songInPlaylist(song));
                    Bukkit.getLogger().info("Removed song " + song.getTitle() + " from " + house.getOwner().getName() + "'s playlist.");
                    for (Song s : house.getSongs()) {
                        Bukkit.getLogger().info("Song: " + s.getTitle());
                    }
                } else {
                    house.addSong(song);
                    open();
                    Bukkit.getLogger().info(house.isJukeboxPlaying() + " " + house.songInPlaylist(song));
                    Bukkit.getLogger().info("Added song " + song.getTitle() + " to " + house.getOwner().getName() + "'s playlist.");
                    for (Song s : house.getSongs()) {
                        Bukkit.getLogger().info("Song: " + s.getTitle());
                    }
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            });
        }

        addItem(48, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), () -> new HousingMenu(main, player, house).open()
        );

        addItem(49, ItemBuilder.create(Material.TNT)
                .name("&cClear Playlist")
                .description("&7Click to clear the playlist!")
                .build(), () -> {
            house.stopMusic();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            open();
        });

        if (house.isJukeboxPlaying()) {
            addItem(50, ItemBuilder.create(Material.LIME_DYE)
                    .name("&7Currently Playing")
                    .description("&7Song Playing: \"&e" + house.getCurrentSong().getTitle() + "&7\"\n\n&7Click to stop the music!")
                    .punctuation(false)
                    .build(), () -> {
                house.stopMusic();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                open();
            });
        } else {
            addItem(50, ItemBuilder.create(Material.RED_DYE)
                    .name("&7Currently Stopped")
                    .description("&7Song Playing: \"&e" + house.getCurrentSong().getTitle() + "&7\"\n\nn&7Click to start the music!")
                    .punctuation(false)
                    .build(), () -> {
                house.startMusic();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                open();
            });
        }

        addItem(51, ItemBuilder.create(Material.COMPARATOR)
                .name("&cSkip Song")
                .description("&7Click to skip the current song!")
                .punctuation(false)
                .build(), () -> {
            house.skipSong();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            open();
        });

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

    private PaginationList<Song> getSongs() {
        List<Song> songsArray = new ArrayList<>();

        File[] files = new File(main.getDataFolder() + "/songs/").listFiles();
        for (File value : files) {
            if (value == null) break;
            File file = value;
            Song song = NBSDecoder.parse(file);
            songsArray.add(song);
        }

        if (search != null) {
            songsArray = songsArray.stream().filter(song -> song.getTitle().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(songsArray, avaliableSlots.length);
    }

    private String formattedTime(int seconds) {
        seconds = seconds / 20;
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes + ":" + (remainingSeconds < 10 ? "0" : "") + remainingSeconds;
    }
}
