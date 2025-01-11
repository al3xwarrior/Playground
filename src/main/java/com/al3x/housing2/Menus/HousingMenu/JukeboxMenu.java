package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.JukeBoxManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class JukeboxMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private int page;

    public JukeboxMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Jukebox"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.page = 1;
        setupItems();
    }

    public JukeboxMenu(Main main, Player player, HousingWorld house, int page) {
        super(player, colorize("&7Jukebox"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.page = page;
        setupItems();
    }

    @Override
    public void setupItems() {
        int[] avaliableSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        List<String> songNames = JukeBoxManager.getSongNames();

        int start = (page - 1) * 21;
        int end = Math.min(start + 21, songNames.size() - start);

        for (int i = 0; i < end; i++) {
            String songName = songNames.get(start + i);
            Song song = JukeBoxManager.getSong(songName);

            addItem(avaliableSlots[i], ItemBuilder.create(Material.JUKEBOX)
                    .name("&7" + song.getTitle())
                    .description("&7Author: &f" + song.getAuthor() + "\n&7Description: &f" + song.getDescription() + "\n&7Length: &f" + song.getLength() + "s\n\n&e&lClick to " + (!house.isJukeboxPlaying() || !house.songInPlaylist(song) ? "&a&lAdd" : "&c&lRemove") + " &e&lthis song to the playlist!")
                    .glow(house.isJukeboxPlaying() && house.songInPlaylist(song))
                    .punctuation(false)
                    .build(), (e) -> {
                if (house.isJukeboxPlaying() && house.songInPlaylist(song)) {
                    Bukkit.getLogger().info("Removing song");
                    house.removeSong(song);
                    new JukeboxMenu(main, player, house, page).open();
                } else {
                    Bukkit.getLogger().info("Adding song");
                    house.addSong(song);
                    new JukeboxMenu(main, player, house, page).open();
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            });
        }

        addItem(48, ItemBuilder.create(Material.ARROW)
                .name("&cGo Back")
                .build(), (e) -> new HousingMenu(main, player, house).open()
        );

        addItem(49, ItemBuilder.create(Material.ANVIL)
                .name("&cClear Playlist")
                .description("&7Click to clear the playlist!")
                .build(), (e) -> {
            house.stopMusic();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            new JukeboxMenu(main, player, house, page).open();
        });

        if (house.isJukeboxPlaying()) {
            addItem(50, ItemBuilder.create(Material.LIME_DYE)
                    .name("&7Currently Playing")
                    .description("&7Click to stop the music!")
                    .punctuation(false)
                    .build(), (e) -> {
                house.stopMusic();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                new JukeboxMenu(main, player, house, page).open();
            });
        } else {
            addItem(50, ItemBuilder.create(Material.RED_DYE)
                    .name("&7Currently Stopped")
                    .description("&7Click to start the music!")
                    .punctuation(false)
                    .build(), (e) -> {
                house.startMusic();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                new JukeboxMenu(main, player, house, page).open();
            });
        }

        addItem(51, ItemBuilder.create(Material.COMPARATOR)
                .name("&cSkip Song")
                .description("&7Click to skip the current song!")
                .punctuation(false)
                .build(), () -> {
            house.skipSong();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            new JukeboxMenu(main, player, house, page).open();
        });

        if (page > 1){
            addItem(45, ItemBuilder.create(Material.ARROW)
                    .name("&cPrevious Page")
                    .build(), (e) -> new JukeboxMenu(main, player, house, page - 1).open()
            );
        }

        if (page * 21 < songNames.size()) {
            addItem(53, ItemBuilder.create(Material.ARROW)
                    .name("&aNext Page")
                    .build(), (e) -> new JukeboxMenu(main, player, house, page + 1).open()
            );
        }
    }
}
