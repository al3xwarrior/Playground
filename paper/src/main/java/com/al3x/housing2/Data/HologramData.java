package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousingWorld;
import com.github.retrooper.packetevents.util.Quaternion4f;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HologramData {
    private List<String> text;
    private LocationData location;
    private String startingY;
    private double spacing;
    private String scale;
    private TextDisplay.TextAlignment alignment;
    private Display.Billboard billboard;
    private Quaternion4f rotation;
    private boolean shadow;
    private boolean seeThroughBlocks;
    private int backgroundColor;

    public HologramData() {

    }

    public HologramData(List<String> text, LocationData location, String startingY, double spacing, String scale, TextDisplay.TextAlignment alignment, Display.Billboard billboard, Quaternion4f rotation, boolean shadow, boolean seeThroughBlocks, int backgroundColor) {
        this.text = text;
        this.location = location;
        this.startingY = startingY;
        this.spacing = spacing;
        this.scale = scale;
        this.alignment = alignment;
        this.billboard = billboard;
        this.rotation = rotation;
        this.shadow = shadow;
        this.seeThroughBlocks = seeThroughBlocks;
        this.backgroundColor = backgroundColor;
    }

    public static List<HologramData> fromList(List<Hologram> holograms) {
        List<HologramData> hologramDatas = new ArrayList<>();
        for (Hologram hologram : holograms) {
            if (hologram.isDestroyed()) continue;
            hologramDatas.add(new HologramData(
                hologram.getText(),
                LocationData.fromLocation(hologram.getLocation()),
                hologram.getStartingY(),
                hologram.getSpacing(),
                hologram.getScale(),
                hologram.getAlignment(),
                hologram.getBillboard(),
                hologram.getRotation(),
                hologram.isShadow(),
                hologram.isSeeThroughBlocks(),
                hologram.getBackgroundColor()
            ));
        }
        return hologramDatas;
    }

    public static HologramData fromData(Hologram hologram) {
        return new HologramData(
            hologram.getText(),
            LocationData.fromLocation(hologram.getLocation()),
            hologram.getStartingY(),
            hologram.getSpacing(),
            hologram.getScale(),
            hologram.getAlignment(),
            hologram.getBillboard(),
            hologram.getRotation(),
            hologram.isShadow(),
            hologram.isSeeThroughBlocks(),
            hologram.getBackgroundColor()
        );
    }

    public static Hologram toData(HologramData hologramData) {
        return new Hologram(
            null,
            hologramData.getText(),
            hologramData.getLocation().toLocation(),
            hologramData.getStartingY(),
            hologramData.getSpacing(),
            hologramData.getScale(),
            hologramData.getAlignment(),
            hologramData.getBillboard(),
            hologramData.getRotation(),
            hologramData.isShadow(),
            hologramData.isSeeThroughBlocks(),
            hologramData.getBackgroundColor()
        );
    }

    public static List<Hologram> toList(List<HologramData> hologramDatas, HousingWorld house) {
        List<Hologram> holograms = new ArrayList<>();
        for (HologramData hologramData : hologramDatas) {
            holograms.add(new Hologram(
                house,
                hologramData.getText(),
                hologramData.getLocation().toLocation(),
                hologramData.getStartingY(),
                hologramData.getSpacing(),
                hologramData.getScale(),
                hologramData.getAlignment(),
                hologramData.getBillboard(),
                hologramData.getRotation(),
                hologramData.isShadow(),
                hologramData.isSeeThroughBlocks(),
                hologramData.getBackgroundColor()
            ));
        }
        return holograms;
    }
}