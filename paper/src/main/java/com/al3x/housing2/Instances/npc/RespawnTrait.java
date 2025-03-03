package com.al3x.housing2.Instances.npc;

import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class RespawnTrait extends Trait {
    boolean started = false;
    boolean respawning = false;
    HousingNPC hNPC;

    public RespawnTrait(HousingNPC hNPC) {
        super("respawn");
        this.hNPC = hNPC;
    }

    @Override
    public void onSpawn() {
        started = true;
    }

    @Override
    public void run() {
        if (!npc.isSpawned() && !respawning && started) {
            respawning = true;
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if (npc.getEntity() != null) npc.getEntity().spawnAt(hNPC.getLocation());
                else npc.spawn(hNPC.getLocation());
                respawning = false;
            }, hNPC.getRespawnTime());
        }
    }
}
