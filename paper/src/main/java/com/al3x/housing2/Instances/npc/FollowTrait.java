package com.al3x.housing2.Instances.npc;

import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FollowTrait extends Trait {
    HousingNPC hNPC;

    public FollowTrait(HousingNPC hNPC) {
        super("follow");
        this.hNPC = hNPC;
    }

    @Override
    public void run() {
        if (npc.isSpawned()) {
            if (npc.getEntity().getLocation().getY() < -64) {
                npc.despawn();
                npc.spawn(hNPC.getLocation());
            }

            net.citizensnpcs.trait.FollowTrait followTrait = npc.getOrAddTrait(net.citizensnpcs.trait.FollowTrait.class);
            followTrait.setProtect(false);
            if (hNPC.getNavigationType() == NavigationType.PLAYER) {
                Player closest = getClosestPlayer();
                if (closest != null) {
                    followTrait.follow(closest);
                }
            } else {
                followTrait.follow(null);
            }
        }
    }

    private Player getClosestPlayer() {
        Player closest = null;
        double closestDistance = 20;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(npc.getEntity().getWorld())) {
                double distance = player.getLocation().distance(npc.getEntity().getLocation());
                if (distance < closestDistance) {
                    closest = player;
                    closestDistance = distance;
                }
            }
        }
        return closest;
    }
}
