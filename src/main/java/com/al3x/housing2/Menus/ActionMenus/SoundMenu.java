package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Actions.PlaySoundAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class SoundMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PlaySoundAction action;
    private EventType event;

    public SoundMenu(Main main, Player player, HousingWorld house, PlaySoundAction action, EventType event) {
        super(player, colorize("&7Select Option"), 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
    }

    @Override
    public void setupItems() {
        // Anvil Break
        ItemStack anvilBreakItem = new ItemStack(Material.ANVIL);
        ItemMeta anvilBreakItemMeta = anvilBreakItem.getItemMeta();
        anvilBreakItemMeta.setDisplayName(colorize("&aAnvil Break"));
        anvilBreakItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_ANVIL_BREAK)) anvilBreakItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        anvilBreakItem.setItemMeta(anvilBreakItemMeta);
        addItem(10, anvilBreakItem, () -> {
            action.setSound(Sound.BLOCK_ANVIL_BREAK);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Anvil Land
        ItemStack anvilLandItem = new ItemStack(Material.ANVIL);
        ItemMeta anvilLandItemMeta = anvilLandItem.getItemMeta();
        anvilLandItemMeta.setDisplayName(colorize("&aAnvil Land"));
        anvilLandItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_ANVIL_LAND)) anvilLandItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        anvilLandItem.setItemMeta(anvilLandItemMeta);
        addItem(11, anvilLandItem, () -> {
            action.setSound(Sound.BLOCK_ANVIL_LAND);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Anvil Use
        ItemStack anvilUseItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta anvilUseItemMeta = anvilUseItem.getItemMeta();
        anvilUseItemMeta.setDisplayName(colorize("&aAnvil Use"));
        anvilUseItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_ANVIL_USE)) anvilUseItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        anvilUseItem.setItemMeta(anvilUseItemMeta);
        addItem(12, anvilUseItem, () -> {
            action.setSound(Sound.BLOCK_ANVIL_USE);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Click
        ItemStack clickItem = new ItemStack(Material.STONE_BUTTON);
        ItemMeta clickItemMeta = clickItem.getItemMeta();
        clickItemMeta.setDisplayName(colorize("&aClick"));
        clickItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.UI_BUTTON_CLICK)) clickItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        clickItem.setItemMeta(clickItemMeta);
        addItem(13, clickItem, () -> {
            action.setSound(Sound.UI_BUTTON_CLICK);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Drink
        ItemStack drinkItem = new ItemStack(Material.POTION);
        ItemMeta drinkItemMeta = drinkItem.getItemMeta();
        drinkItemMeta.setDisplayName(colorize("&aDrink"));
        drinkItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.ENTITY_GENERIC_DRINK)) drinkItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        drinkItem.setItemMeta(drinkItemMeta);
        addItem(14, drinkItem, () -> {
            action.setSound(Sound.ENTITY_GENERIC_DRINK);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Eat
        ItemStack eatItem = new ItemStack(Material.COOKED_BEEF);
        ItemMeta eatItemMeta = eatItem.getItemMeta();
        eatItemMeta.setDisplayName(colorize("&aEat"));
        eatItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.ENTITY_GENERIC_EAT)) eatItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        eatItem.setItemMeta(eatItemMeta);
        addItem(15, eatItem, () -> {
            action.setSound(Sound.ENTITY_GENERIC_EAT);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Explode
        ItemStack explodeItem = new ItemStack(Material.TNT);
        ItemMeta explodeItemMeta = explodeItem.getItemMeta();
        explodeItemMeta.setDisplayName(colorize("&aExplode"));
        explodeItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.ENTITY_GENERIC_EXPLODE)) explodeItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        explodeItem.setItemMeta(explodeItemMeta);
        addItem(16, explodeItem, () -> {
            action.setSound(Sound.ENTITY_GENERIC_EXPLODE);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Item Pickup
        ItemStack itemPickupItem = new ItemStack(Material.IRON_NUGGET);
        ItemMeta itemPickupItemMeta = itemPickupItem.getItemMeta();
        itemPickupItemMeta.setDisplayName(colorize("&aItem Pickup"));
        itemPickupItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.ENTITY_ITEM_PICKUP)) itemPickupItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        itemPickupItem.setItemMeta(itemPickupItemMeta);
        addItem(19, itemPickupItem, () -> {
            action.setSound(Sound.ENTITY_ITEM_PICKUP);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Level Up
        ItemStack levelUpItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta levelUpItemMeta = levelUpItem.getItemMeta();
        levelUpItemMeta.setDisplayName(colorize("&aLevel Up"));
        levelUpItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.ENTITY_PLAYER_LEVELUP)) levelUpItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        levelUpItem.setItemMeta(levelUpItemMeta);
        addItem(20, levelUpItem, () -> {
            action.setSound(Sound.ENTITY_PLAYER_LEVELUP);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Note Bass
        ItemStack noteBassItem = new ItemStack(Material.MUSIC_DISC_BLOCKS);
        ItemMeta noteBassItemMeta = noteBassItem.getItemMeta();
        noteBassItemMeta.setDisplayName(colorize("&aNote Bass"));
        noteBassItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_NOTE_BLOCK_BASS)) noteBassItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        noteBassItem.setItemMeta(noteBassItemMeta);
        addItem(21, noteBassItem, () -> {
            action.setSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Note Piano
        ItemStack notePianoItem = new ItemStack(Material.MUSIC_DISC_FAR);
        ItemMeta notePianoItemMeta = notePianoItem.getItemMeta();
        notePianoItemMeta.setDisplayName(colorize("&aNote Piano"));
        notePianoItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_NOTE_BLOCK_HARP)) notePianoItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        notePianoItem.setItemMeta(notePianoItemMeta);
        addItem(22, notePianoItem, () -> {
            action.setSound(Sound.BLOCK_NOTE_BLOCK_HARP);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Note Bass Drum
        ItemStack noteBassDrumItem = new ItemStack(Material.MUSIC_DISC_MELLOHI);
        ItemMeta noteBassDrumItemMeta = noteBassDrumItem.getItemMeta();
        noteBassDrumItemMeta.setDisplayName(colorize("&aNote Bass Drum"));
        noteBassDrumItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_NOTE_BLOCK_BASEDRUM)) noteBassDrumItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        noteBassDrumItem.setItemMeta(noteBassDrumItemMeta);
        addItem(23, noteBassDrumItem, () -> {
            action.setSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Note Sticks
        ItemStack noteSticksItem = new ItemStack(Material.STICK);
        ItemMeta noteSticksItemMeta = noteSticksItem.getItemMeta();
        noteSticksItemMeta.setDisplayName(colorize("&aNote Sticks"));
        noteSticksItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_NOTE_BLOCK_SNARE)) noteSticksItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        noteSticksItem.setItemMeta(noteSticksItemMeta);
        addItem(24, noteSticksItem, () -> {
            action.setSound(Sound.BLOCK_NOTE_BLOCK_SNARE);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        // Note Pling
        ItemStack notePlingItem = new ItemStack(Material.MUSIC_DISC_PIGSTEP);
        ItemMeta notePlingItemMeta = notePlingItem.getItemMeta();
        notePlingItemMeta.setDisplayName(colorize("&aNote Pling"));
        notePlingItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getSound().equals(Sound.BLOCK_NOTE_BLOCK_PLING)) notePlingItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        notePlingItem.setItemMeta(notePlingItemMeta);
        addItem(25, notePlingItem, () -> {
            action.setSound(Sound.BLOCK_NOTE_BLOCK_PLING);
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });

        ItemStack customSoundItem = new ItemStack(Material.COMMAND_BLOCK_MINECART);
        ItemMeta customSoundItemMeta = customSoundItem.getItemMeta();
        customSoundItemMeta.setDisplayName(colorize("&aCustom Sound"));
        customSoundItemMeta.setLore(Arrays.asList(
                colorize("&7I am too lazy to add all"),
                colorize("&7the sounds."),
                "",
                colorize("&eClick to input!")
        ));
        customSoundItem.setItemMeta(customSoundItemMeta);
        addItem(31, customSoundItem, () -> {
            player.closeInventory();
            player.sendMessage(colorize("&eEnter a valid sound ID to use."));
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerChat(AsyncPlayerChatEvent e) {
                    e.setCancelled(true);
                    if (e.getPlayer().equals(player)) {
                        String newMessage = e.getMessage();
                        action.setSound(newMessage);

                        if (action.getSound() == null) {
                            action.setSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                            player.sendMessage(colorize("&cSorry but that sound was rejected. Take a look at this list for sound ids. https://www.digminecraft.com/lists/sound_list_pc.php"));
                        } else {
                            player.sendMessage(colorize("&eSound set to " + action.getSound()));
                        }

                        // Unregister this listener after capturing the message
                        AsyncPlayerChatEvent.getHandlerList().unregister(this);

                        // Reopen the ChatActionMenu
                        Bukkit.getScheduler().runTaskLater(main, () -> new PlaySoundActionMenu(main, player, house, action, event).open(), 1L); // Delay slightly to allow chat event to complete
                    }
                }
            }, main);
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            new PlaySoundActionMenu(main, player, house, action, event).open();
        });
    }
}
