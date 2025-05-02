package com.al3x.housing2.Utils;

import com.al3x.housing2.Utils.Duple;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.al3x.housing2.Utils.Color.colorize;

public class BlockList {
    List<Duple<Integer, Material>> blockList = new ArrayList<Duple<Integer, Material>>();

    public BlockList addBlock(int percent, Material block) {
        this.blockList.add(new Duple<Integer, Material>(percent, block));
        return this;
    }

    public List<Material> generateBlocks() {
        ArrayList<Material> blocks = new ArrayList<Material>();
        for (Duple<Integer, Material> duple : blockList) {
            for (int i = 0; i < duple.getFirst(); ++i) {
                blocks.add(duple.getSecond());
            }
        }
        return blocks;
    }

    public boolean includesMaterial(Material material) {
        for (Duple<Integer, Material> duple : blockList) {
            if (duple.getSecond() == material) {
                return true;
            }
        }
        return false;
    }

    public List<Duple<Integer, Material>> getBlockList() {
        return this.blockList;
    }

    public static BlockList fromString(String blockData) {
        BlockList blockList = new BlockList();
        String[] blockDataSplit = blockData.split(",");
        for (String block : blockDataSplit) {
            if (block.contains("%")) {
                String[] blockOptions = block.split("%");
                int percentage = Integer.parseInt(blockOptions[0]);
                Material material = Material.matchMaterial(blockOptions[1].toUpperCase());
                if (material == null) {
                    return null;
                }
                blockList.addBlock(percentage, material);
            } else {
                Material material = Material.matchMaterial(block);
                if (material == null) {
                    return null;
                }
                blockList.addBlock(1, material);
            }
        }
        return blockList;
    }

    public static BlockList fromString(Player player, String blockData) {
        BlockList blockList = new BlockList();
        String[] blockDataSplit = blockData.split(",");
        for (String block : blockDataSplit) {
            if (block.contains("%")) {
                String[] blockOptions = block.split("%");
                int percentage = Integer.parseInt(blockOptions[0]);
                Material material;
                if (NumberUtilsKt.isInt(blockOptions[1])) {
                    material = Material.values()[Integer.parseInt(blockOptions[1])];
                } else {
                    material = Material.matchMaterial(blockOptions[1].toUpperCase());
                }
                if (material == null) {
                    player.sendMessage(colorize("&cInvalid block type \"" + blockOptions[1].toUpperCase() + "\"."));
                    return null;
                }
                blockList.addBlock(percentage, material);
            } else {
                Material material;
                if (NumberUtilsKt.isInt(block)) {
                    material = Material.values()[Integer.parseInt(block)];
                } else {
                    material = Material.matchMaterial(block);
                }
                if (material == null) {
                    player.sendMessage(colorize("&cInvalid block type \"" + block.toUpperCase() + "\"."));
                    return null;
                }
                blockList.addBlock(1, material);
            }
        }
        return blockList;
    }

    public static List<String> completionsForArgument(String arg) {
        List<String> completions = new ArrayList<String>();
        String[] commas = arg.split(",");
        for (String block : commas) {
            if (block.contains("%")) {
                String[] blockOptions = block.split("%");
                if (blockOptions.length >= 1) {
                    for (Material material : Material.values()) {
                        if (!material.isBlock()) continue;
                        completions.add(blockOptions[0] + "%" + material.name().toLowerCase());
                    }
                }
            } else {
                if (block.matches(".*\\d.*")) {
                    for (Material material : Material.values()) {
                        if (!material.isBlock()) continue;
                        completions.add(block + "%" + material.name().toLowerCase());
                    }
                } else {
                    //Percentage from 1 to 100
                    for (Material material : Material.values()) {
                        if (!material.isBlock()) continue;
                        completions.add(material.name().toLowerCase());
                    }
                }
            }
        }
        return completions;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String[] args) {
            List<String> completions = new ArrayList<String>();
            if (args.length == 1) {
                completions.addAll(completionsForArgument(args[0]));
            }

            completions = completions.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).toList();
            return completions;
        }
    }

    public static class DuoTabCompleter implements org.bukkit.command.TabCompleter {
        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String[] args) {
            List<String> completions = new ArrayList<String>();
            if (args.length == 1) {
                completions.addAll(completionsForArgument(args[0]));
            } else if (args.length == 2) {
                completions.addAll(completionsForArgument(args[1]));
            }
            completions = completions.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).toList();
            return completions;
        }
    }
}