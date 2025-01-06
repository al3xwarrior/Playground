package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class NPCMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public NPCMenu(Main main, Player player, HousingNPC housingNPC) {
        super(player, colorize("&7Edit NPC"), 54);
        this.main = main;
        this.player = player;
        this.housesManager = main.getHousesManager();
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
            // new HologramEditorMenu(main, player, housingNPC).open();
        });

        ItemStack clickActions = new ItemStack(Material.PAPER);
        ItemMeta clickActionsMeta = clickActions.getItemMeta();
        clickActionsMeta.setDisplayName(colorize("&aClick Actions"));
        clickActions.setItemMeta(clickActionsMeta);
        addItem(12, clickActions, () -> {
            new ActionsMenu(main, player, house, housingNPC).open();
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
        addItem(16, changeEquipment, () -> new EquipmentMenu(main, player, housingNPC).open());

        ItemStack lookAtPlayers = new ItemStack(Material.COMPASS);
        ItemMeta lookAtPlayersMeta = lookAtPlayers.getItemMeta();
        lookAtPlayersMeta.setDisplayName(colorize("&aLook at Players"));
        lookAtPlayers.setItemMeta(lookAtPlayersMeta);
        addItem(28, lookAtPlayers, () -> {
            NPC citizensNPC = CitizensAPI.getNPCRegistry().getById(housingNPC.getNpcID());
            boolean newStatus = citizensNPC.getOrAddTrait(LookClose.class).toggle();

            housingNPC.setLookAtPlayer(newStatus);
            player.sendMessage(colorize("&aLook at Players set to: " + newStatus));
            setupItems();
        });

        ItemStack entityType = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta entityTypeMeta = entityType.getItemMeta();
        entityTypeMeta.setDisplayName(colorize("&aEntity Type"));
        entityType.setItemMeta(entityTypeMeta);
        addItem(30, entityType, () -> {
            new EntityChooseMenu(main, player, housesManager, housingNPC).open();
        });

        ItemStack npcName = new ItemStack(Material.NAME_TAG);
        ItemMeta npcNameMeta = npcName.getItemMeta();
        npcNameMeta.setDisplayName(colorize("&aNPC Name"));
        npcName.setItemMeta(npcNameMeta);
        addItem(32, npcName, () -> {
            player.sendMessage(colorize("&7Enter the new name for the NPC:"));
            player.closeInventory();
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerChat(AsyncPlayerChatEvent e) {
                    e.setCancelled(true);
                    if (e.getPlayer().equals(player)) {
                        String newMessage = e.getMessage();
                        Bukkit.getScheduler().runTask(main, () -> {
                            housingNPC.setName(newMessage);
                            player.sendMessage(colorize("&aNpc name set to: " + newMessage));
                        });

                        // Unregister this listener after capturing the message
                        AsyncPlayerChatEvent.getHandlerList().unregister(this);

                        // Reopen the NPCMenu
                        Bukkit.getScheduler().runTaskLater(main, () -> new NPCMenu(main, player, housingNPC).open(), 1L); // Delay slightly to allow chat event to complete
                    }
                }
            }, main);
        });

        ItemBuilder navigation = ItemBuilder.create(Material.BLAZE_POWDER)
                .name("&aNavigation")
                .description("Add navigation to the NPC")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        addItem(34, navigation.build(), () -> {
            new NavigationMenu(main, player, housingNPC).open();
        });


        ItemStack removeNPC = new ItemStack(Material.TNT);
        ItemMeta removeNPCMeta = removeNPC.getItemMeta();
        removeNPCMeta.setDisplayName(colorize("&cRemove NPC"));
        removeNPC.setItemMeta(removeNPCMeta);
        addItem(53, removeNPC, () -> {
            house.removeNPC(housingNPC.getNpcID());
            player.closeInventory();
        });

        ItemBuilder npcInfo = ItemBuilder.create(Material.PAPER)
                .name("&aNPC Info")
                .description("Information about this NPC")
                .info("Name", housingNPC.getName())
                .info("ID", "&a" + housingNPC.getNpcID())
                .info("Entity Type", "&b" + housingNPC.getEntityType().name())
                .info("Navigation Type", "&6" + housingNPC.getNavigationType().name())
                .info("Look at Players", housingNPC.isLookAtPlayer() ? "&aYes" : "&cNo")
                .lClick(ItemBuilder.ActionType.SAVE_LOCATION);
        addItem(48, npcInfo.build(), () -> {
            housingNPC.setLocation(housingNPC.getLocation());
        });

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(colorize("&cClose"));
        close.setItemMeta(closeMeta);
        addItem(49, close, () -> player.closeInventory());
    }
}
