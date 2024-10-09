package com.al3x.housing2.Instances;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

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

    private NPC citizensNPC;
    private Main main;

    // Npc Properties
    private int npcUUID;
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

    public HousingNPC(Main main, Player player, Location location) {
        this.main = main;
        this.name = npcNames[new Random().nextInt(npcNames.length)];
        this.lookAtPlayer = true;
        this.location = location;
        this.creatorUUID = player.getUniqueId();

        this.actions = new ArrayList<>();

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, this.name);
        citizensNPC.spawn(location);
        citizensNPC.faceLocation(player.getLocation());

        // Update it because weird
        this.npcUUID = citizensNPC.getId();
    }

    public void setEntity(EntityType entityType) {
        citizensNPC.getEntity().setMetadata("NPC", new FixedMetadataValue(main, true));
        citizensNPC.setBukkitEntityType(entityType);
    }

    public void sendExecuteActions(HousingWorld house, Player player) {
        if (actions != null) {
            for (Action action : actions) {
                action.execute(player, house);
            }
        }
    }

    public void setHologramLines(List<String> lines) {
        HologramTrait hologram = citizensNPC.getOrAddTrait(HologramTrait.class);
        hologram.clear();

        for (String line : lines) {
            hologram.addLine(line);
        }
    }

    public List<String> getHolograms() {
        return citizensNPC.getOrAddTrait(HologramTrait.class).getLines();
    }

    public NPC getCitizensNPC() {
        return citizensNPC;
    }

    public int getNpcUUID() {
        return npcUUID;
    }

    public List<Action> getActions() {
        return actions;
    }
}
