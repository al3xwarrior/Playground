package com.al3x.housing2.Tests;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void all(Player p) {
        int index = 0;
        int size = 1; //Update this value to the amount of tests
        List<Boolean> results = new ArrayList<>();

        p.sendMessage("§cStarting all tests");

        new ActionSaveTest().execute(p, index++, size, results);

        p.sendMessage("§7All tests completed");
        int passed = (int) results.stream().filter(b -> b).count();
        int failed = results.size() - passed;
        p.sendMessage("§aPassed: " + passed + " §cFailed: " + failed);
    }

    public void execute(Player p, int index, int size, List<Boolean> results) {
        p.sendMessage("§cRunning test \"Test\" (" + index + "/" + size + ")");
        p.sendMessage("§cTest completed");
    }
}
