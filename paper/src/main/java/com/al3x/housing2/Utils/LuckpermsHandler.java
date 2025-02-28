package com.al3x.housing2.Utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;

public class LuckpermsHandler {
    public static void addPermission(Player player, String permission) {
        LuckPerms api = LuckPermsProvider.get();
        api.getUserManager().modifyUser(player.getUniqueId(), user -> {
            user.data().add(PermissionNode.builder(permission).build());
        });
    }

    public static void removePermission(Player player, String permission) {
        LuckPerms api = LuckPermsProvider.get();
        api.getUserManager().modifyUser(player.getUniqueId(), user -> {
            user.data().remove(PermissionNode.builder(permission).build());
        });
    }
}
