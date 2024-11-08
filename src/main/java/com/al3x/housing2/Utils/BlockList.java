package com.al3x.housing2.Utils;

import com.al3x.housing2.Utils.Duple;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class BlockList {
    List<Duple<Integer, Material>> blockList = new ArrayList<Duple<Integer, Material>>();

    public void addBlock(int percent, Material block) {
        this.blockList.add(new Duple<Integer, Material>(percent, block));
    }

    public List<Material> generateBlocks() {
        ArrayList<Material> blocks = new ArrayList<Material>();
        for (Duple<Integer, Material> duple : this.blockList) {
            for (int i = 0; i < duple.getFirst(); ++i) {
                blocks.add(duple.getSecond());
            }
        }
        return blocks;
    }

    public List<Duple<Integer, Material>> getBlockList() {
        return this.blockList;
    }

    public static BlockList fromString(Player player, String blockData) {
        BlockList blockList = new BlockList();
        String[] blockDataSplit = blockData.split(",");
        for (String block : blockDataSplit) {
            if (block.contains("%")) {
                String[] blockOptions = block.split("%");
                int percentage = Integer.parseInt(blockOptions[0]);
                Material material = Material.matchMaterial(blockOptions[1].toUpperCase());
                if (material == null) {
                    player.sendMessage(colorize("&cInvalid block type \"" + blockOptions[1].toUpperCase() + "\"."));
                    return null;
                }
                blockList.addBlock(percentage, material);
            } else {
                Material material = Material.matchMaterial(block);
                if (material == null) {
                    player.sendMessage(colorize("&cInvalid block type \"" + block.toUpperCase() + "\"."));
                    return null;
                }
                blockList.addBlock(1, material);
            }
        }
        return blockList;
    }
}