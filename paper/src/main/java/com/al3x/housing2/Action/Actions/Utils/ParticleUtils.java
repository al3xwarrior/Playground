package com.al3x.housing2.Action.Actions.Utils;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.Particles;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class ParticleUtils {
    public static Object output(
            Particles particle,
            String color,
            String color2,
            Float size
    ) {
        if (particle.getData() == null) return null;
        switch (particle.getData()) {
            case COLOR -> {
                if (color == null) color = "255,255,255";
                String[] split = color.split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                return Color.fromARGB(255, r, g, b);
            }
            case DUST -> {
                if (color == null) color = "255,255,255";
                String[] split = color.split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                return new Particle.DustOptions(Color.fromRGB(r, g, b), (size == null) ? 1.0f : size);
            }
            case DUST_TRANSITION -> {
                if (color == null) color = "255,255,255";
                if (color2 == null) color2 = "255,255,255";

                String[] split = color.split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                Color c1 = Color.fromRGB(r, g, b);

                split = color2.split(",");
                r = Integer.parseInt(split[0]);
                g = Integer.parseInt(split[1]);
                b = Integer.parseInt(split[2]);
                Color c2 = Color.fromRGB(r, g, b);

                return new Particle.DustTransition(c1, c2, (size == null) ? 1.0f : size);
            }
        }
        return null;
    }

    public static List<String> keys(Particles particle) {
        if (particle.getData() == null) return List.of("speed");
        List<String> keys = new ArrayList<>();
        switch (particle.getData()) {
            case COLOR -> {
                keys.add("color");
            }
            case DUST -> {
                keys.add("color");
                keys.add("size");
            }
            case DUST_TRANSITION -> {
                keys.add("color");
                keys.add("color2");
                keys.add("size");
            }
        }
        return keys;
    }
}
