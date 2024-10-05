package com.al3x.housing2.Instances;

import com.al3x.housing2.Actions.Action;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.utils.SkinFetcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

public class HousingNPC {

    public static ItemStack getNPCItem() {
        ItemStack npc = getCustomSkull("a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        ItemMeta meta = npc.getItemMeta();
        meta.setItemName(colorize("&aNPC"));
        npc.setItemMeta(meta);
        return npc;
    }

    // Npc Properties
    private String npcUUID;
    private UUID creatorUUID;
    private String name;
    private boolean lookAtPlayer;
    private Location location;
    
    // Equipment
    private ItemStack hand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private List<Action> actions;

    private String[] npcNames = {"&aAlex", "&2Baldrick", "&cD&6i&ed&ad&by", "&5Ben Dover", "&7Loading...", "&eUpdog", "&cConnorLinfoot", "&bCookie Monster", "&c❤"};

    public HousingNPC(Player player, Location location) {
        this.name = npcNames[new Random().nextInt(npcNames.length)];
        this.lookAtPlayer = true;
        this.location = location;
        this.creatorUUID = player.getUniqueId();

        this.actions = new ArrayList<>();

        NpcData data = new NpcData(UUID.randomUUID().toString(), creatorUUID, location);
        SkinFetcher skin = new SkinFetcher("http://textures.minecraft.net/texture/a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        data.setSkin(skin);
        data.setDisplayName(colorize(this.name));

        Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
        FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
        npc.create();
        npc.spawnForAll();

        // Update it because weird
        this.npcUUID = npc.getData().getId();
    }

    public String getNpcUUID() {
        return npcUUID;
    }
}
