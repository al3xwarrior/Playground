package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Instances.HousingNPC.getNPCItem;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

public class NPCMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public NPCMenu(Main main, Player player, HousesManager housesManager, HousingNPC housingNPC) {
        super(player, colorize("&7Edit NPC"), 54);
        this.player = player;
        this.housesManager = housesManager;
        this.house = housesManager.getHouse(player.getWorld());
        this.housingNPC = housingNPC;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack editHologram = new ItemStack(Material.ANVIL);
        ItemMeta editHologramMeta = editHologram.getItemMeta();
        editHologramMeta.setDisplayName(colorize("&aEdit Hologram"));
        editHologram.setItemMeta(editHologramMeta);
        addItem(10, editHologram, () -> {
            player.sendMessage("Edit Hologram selected");
        });

        ItemStack clickActions = new ItemStack(Material.PAPER);
        ItemMeta clickActionsMeta = clickActions.getItemMeta();
        clickActionsMeta.setDisplayName(colorize("&aClick Actions"));
        clickActions.setItemMeta(clickActionsMeta);
        addItem(12, clickActions, () -> {
            player.sendMessage("Click Actions selected");
        });

        ItemStack changeSkin = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta changeSkinMeta = changeSkin.getItemMeta();
        changeSkinMeta.setDisplayName(colorize("&aChange Skin"));
        changeSkin.setItemMeta(changeSkinMeta);
        addItem(14, changeSkin, () -> {
            new SkinSelectorMenu(main, player, housesManager, housingNPC).open();
        });

        ItemStack changeEquipment = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta changeEquipmentMeta = changeEquipment.getItemMeta();
        changeEquipmentMeta.setDisplayName(colorize("&aChange Equipment"));
        changeEquipment.setItemMeta(changeEquipmentMeta);
        addItem(16, changeEquipment, () -> {
            player.sendMessage("Change Equipment selected");
        });

        ItemStack lookAtPlayers = new ItemStack(Material.COMPASS);
        ItemMeta lookAtPlayersMeta = lookAtPlayers.getItemMeta();
        lookAtPlayersMeta.setDisplayName(colorize("&aLook at Players"));
        lookAtPlayers.setItemMeta(lookAtPlayersMeta);
        addItem(28, lookAtPlayers, () -> {
            Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpcById(housingNPC.getNpcUUID());
            boolean newStatus = !npc.getData().isTurnToPlayer();
            npc.getData().setTurnToPlayer(newStatus);
            npc.updateForAll();
            npc.removeForAll();
            npc.spawnForAll();
            if (newStatus) player.sendMessage(colorize("&aNPC will now look towards nearby players."));
            else player.sendMessage(colorize("&aNPC will no longer look towards nearby players."));
        });

        ItemStack entityType = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta entityTypeMeta = entityType.getItemMeta();
        entityTypeMeta.setDisplayName(colorize("&aEntity Type"));
        entityType.setItemMeta(entityTypeMeta);
        addItem(30, entityType, () -> {
            player.sendMessage("entity type");
        });

        ItemStack removeNPC = new ItemStack(Material.TNT);
        ItemMeta removeNPCMeta = removeNPC.getItemMeta();
        removeNPCMeta.setDisplayName(colorize("&cRemove NPC"));
        removeNPC.setItemMeta(removeNPCMeta);
        addItem(44, removeNPC, () -> {
            house.removeNPC(housingNPC.getNpcUUID());
            player.closeInventory();
        });

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(colorize("&cClose"));
        close.setItemMeta(closeMeta);
        addItem(49, close, () -> player.closeInventory());
    }
}
