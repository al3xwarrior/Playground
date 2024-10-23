package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingData.NPCData;
import com.al3x.housing2.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;
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
    private HousingWorld house;
    private Main main;

    // Npc Properties
    private int npcID;
    private UUID npcUUID;
    private UUID creatorUUID;
    private String name;
    private boolean lookAtPlayer;
    private Location location;
    private EntityType entityType;
    
    // Equipment
    private ItemStack hand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private List<Action> actions;

    private String[] npcNames = {"&aAlex", "&2Baldrick", "&cD&6i&ed&ad&by", "&5Ben Dover", "&7Loading...", "&eUpdog", "&cConnorLinfoot", "&bCookie Monster", "&c❤"};

    public HousingNPC(Main main, OfflinePlayer player, Location location, HousingWorld house, NPCData data) {
        this.main = main;
        this.house = house;
        this.name = data.getNpcName();
        this.lookAtPlayer = data.getLookAtPlayer();
        this.location = location;
        this.npcID = data.getNpcID();
        this.npcUUID = UUID.fromString(data.getNpcUUID());
        this.entityType = EntityType.valueOf(data.getNpcType());

        this.creatorUUID = player.getUniqueId();
        this.actions = Companion.toList(data.getActions());

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, npcUUID, npcID, this.name);
        citizensNPC.spawn(location);
    }

    public HousingNPC(Main main, Player player, Location location, HousingWorld house) {
        this.main = main;
        this.house = house;
        this.name = npcNames[new Random().nextInt(npcNames.length)];
        this.lookAtPlayer = true;
        this.location = location;
        this.creatorUUID = player.getUniqueId();
        this.entityType = EntityType.PLAYER;

        this.actions = new ArrayList<>();

        citizensNPC = CitizensAPI.getNPCRegistry().createNPC(entityType, this.name);
        citizensNPC.spawn(location);
        citizensNPC.faceLocation(player.getLocation());

        main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> {
            this.location = citizensNPC.getEntity().getLocation();
        }, 80L); // 4 seconds

        // Update it because weird
        this.npcID = citizensNPC.getId();
        this.npcUUID = citizensNPC.getUniqueId();
    }

    public void setEntity(EntityType entityType) {
        this.entityType = entityType;
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

        citizensNPC.addTrait(hologram);
    }

    public List<String> getHologramLines() {
        return citizensNPC.getOrAddTrait(HologramTrait.class).getLines();
    }

    public List<String> getHolograms() {
        return citizensNPC.getOrAddTrait(HologramTrait.class).getLines();
    }

    public NPC getCitizensNPC() {
        return citizensNPC;
    }

    public int getNpcID() {
        return npcID;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public List<Action> getActions() {
        return actions;
    }

    public HousingWorld getHouse() {
        return house;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    public void setName(String newMessage) {
        this.name = newMessage;
        citizensNPC.setName(newMessage);
    }
}
