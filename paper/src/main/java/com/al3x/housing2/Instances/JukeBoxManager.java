package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JukeBoxManager {

    private static HashMap<String, Song> songs;

    static {
        songs = new HashMap<>();
        try {
            Files.list(Paths.get(Main.getInstance().getDataFolder() + "/songs/")).forEach(path -> {
                Song song = NBSDecoder.parse(path.toFile());
                songs.put(path.getFileName().toString(), song);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (songs.isEmpty()) {
            throw new RuntimeException("No songs to be loaded!");
        }
    }

    public static HashMap<String, Song> getSongs() {
        return songs;
    }

    public static Song getRandomSong() {
        List<String> songNames = new ArrayList<>(songs.keySet());
        return songs.get(songNames.get((int) (Math.random() * songNames.size())));
    }

    public static List<String> getSongNames() {
        if (songs.isEmpty()) return null;
        return new ArrayList<>(songs.keySet());
    }

    public static Song getSong(String name) {
        return songs.get(name);
    }

}
