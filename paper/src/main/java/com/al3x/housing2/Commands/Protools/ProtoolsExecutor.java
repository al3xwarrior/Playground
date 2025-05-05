package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.Color;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public interface ProtoolsExecutor {
    default ProtoolsManager getProtoolsManager() {
        return Main.getInstance().getProtoolsManager();
    }

    default int set(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String blocks = context.getArgument("blocks", String.class);
        BlockList blockList = BlockList.fromString(player, blocks);
        getProtoolsManager().setRegionTo(player, blockList);
        player.sendMessage(Color.colorize("&aSetting region set..."));

        return 1;
    }

    default int wand(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        player.getInventory().addItem(ProtoolsManager.getWand());
        return 1;
    }

    default int replace(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String blocks1 = context.getArgument("blocks1", String.class);
        String blocks2 = context.getArgument("blocks2", String.class);
        BlockList from = BlockList.fromString(player, blocks1);
        BlockList to = BlockList.fromString(player, blocks2);
        if (from == null || to == null) {
            player.sendMessage(Color.colorize("&cUsage: //replace <from> <to>"));
            return 1;
        }
        getProtoolsManager().setRegionTo(player, from, to);
        player.sendMessage(Color.colorize("&aReplacing region set..."));
        return 1;
    }

    default int removeSelection(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().clearSelection(player);
        player.sendMessage(Color.colorize("&aSelection removed..."));
        return 1;
    }

    default int sphere(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String block = context.getArgument("block", String.class);
        int radius = context.getArgument("radius", Integer.class);
        BlockList blockList = BlockList.fromString(player, block);
        getProtoolsManager().createSphere(player, radius, blockList);
        player.sendMessage(Color.colorize("&aSetting sphere set..."));
        return 1;
    }

    default int undo(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().undo(player);
        player.sendMessage(Color.colorize("&aUndoing last action..."));
        return 1;
    }

    default int copy(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().copyToClipboard(player);
        player.sendMessage(Color.colorize("&aRegion copied..."));
        return 1;
    }

    default int paste(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().pasteRegion(player, false);
        player.sendMessage(Color.colorize("&aRegion pasted..."));
        return 1;
    }

    default int insert(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().pasteRegion(player, true);
        player.sendMessage(Color.colorize("&aRegion inserted..."));
        return 1;
    }

    default int protools(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();

        player.sendMessage(colorize("&7&m---------------------------------------"));
        player.sendMessage(colorize("&6&lProtools Commands:"));
        player.sendMessage(colorize("&7- &f/wand &7- &eGet the Protools wand."));
        player.sendMessage(colorize("&7- &f/set <blocks> &7- &eSet the region to the specified blocks."));
        player.sendMessage(colorize("&7- &f/replace <blocks1> <blocks2> &7- &eReplace blocks in the region."));
        player.sendMessage(colorize("&7- &f/sphere <block> <radius> &7- &eCreate a sphere of blocks."));
        player.sendMessage(colorize("&7- &f/undo &7- &eUndo the last action."));
        player.sendMessage(colorize("&7- &f/copy &7- &eCopy the region to the clipboard."));
        player.sendMessage(colorize("&7- &f/paste &7- &ePaste the region from the clipboard."));
        player.sendMessage(colorize("&7- &f/removeselection &7- &eRemove the current selection."));
        player.sendMessage(colorize("&7&m---------------------------------------"));

        return 1;
    }

    default int pos1(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().setPos1(player, player.getLocation());
        player.sendMessage(Color.colorize("&aPosition 1 set to " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ()));
        return 1;
    }

    default int pos2(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        getProtoolsManager().setPos2(player, player.getLocation());
        player.sendMessage(Color.colorize("&aPosition 2 set to " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ()));
        return 1;
    }
}
