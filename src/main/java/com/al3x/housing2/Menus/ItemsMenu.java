package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Instances.HousingNPC.getNPCItem;
import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.SkullTextures.getCustomSkull;

public class ItemsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public ItemsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Items"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack mailbox = getCustomSkull("b4bd9dd128c94c10c945eadaa342fc6d9765f37b3df2e38f7b056dc7c927ed");
        ItemMeta mailboxMeta = mailbox.getItemMeta();
        mailboxMeta.setDisplayName(colorize("&aMailbox"));
        mailbox.setItemMeta(mailboxMeta);
        addItem(0, mailbox, () -> {
            player.sendMessage("Mailbox selected");
        });

        ItemStack eggHuntBlock = getCustomSkull("8f3f4d3a2f48466918fa05cd60c9830cc18fc74390cfaeaad1a117786aba90cd");
        ItemMeta eggHuntBlockMeta = eggHuntBlock.getItemMeta();
        eggHuntBlockMeta.setDisplayName(colorize("&aEgg Hunt Block"));
        eggHuntBlock.setItemMeta(eggHuntBlockMeta);
        addItem(1, eggHuntBlock, () -> {
            player.sendMessage("Egg Hunt Block selected");
        });

        ItemStack teleportPad = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta teleportPadMeta = teleportPad.getItemMeta();
        teleportPadMeta.setDisplayName(colorize("&aTeleport Pad"));
        teleportPad.setItemMeta(teleportPadMeta);
        addItem(2, teleportPad, () -> {
            player.sendMessage("Teleport Pad selected");
        });

        ItemStack launchPad = new ItemStack(Material.SLIME_BLOCK);
        ItemMeta launchPadMeta = launchPad.getItemMeta();
        launchPadMeta.setDisplayName(colorize("&aLaunch Pad"));
        launchPad.setItemMeta(launchPadMeta);
        addItem(3, launchPad, () -> {
            player.sendMessage("Launch Pad selected");
        });

        ItemStack parkourBlock = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta parkourBlockMeta = parkourBlock.getItemMeta();
        parkourBlockMeta.setDisplayName(colorize("&aParkour Block"));
        parkourBlock.setItemMeta(parkourBlockMeta);
        addItem(4, parkourBlock, () -> {
            player.sendMessage("Parkour Block selected");
        });

        ItemStack actionPad = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        ItemMeta actionPadMeta = actionPad.getItemMeta();
        actionPadMeta.setDisplayName(colorize("&aAction Pad"));
        actionPad.setItemMeta(actionPadMeta);
        addItem(5, actionPad, () -> {
            player.sendMessage("Action Pad selected");
        });

        ItemStack hologram = new ItemStack(Material.NAME_TAG);
        ItemMeta hologramMeta = hologram.getItemMeta();
        hologramMeta.setDisplayName(colorize("&aHologram"));
        hologram.setItemMeta(hologramMeta);
        addItem(6, hologram, () -> {
            player.sendMessage("Hologram selected");
        });

        ItemStack npc = getCustomSkull("a055eb0f86dcece53be47214871b3153ac9be329fb8b4211536931fcb45a7952");
        ItemMeta npcMeta = npc.getItemMeta();
        npcMeta.setDisplayName(colorize("&aNPC"));
        npc.setItemMeta(npcMeta);
        addItem(7, npc, () -> {
            player.getInventory().addItem(getNPCItem());
        });

        ItemStack actionButton = new ItemStack(Material.STONE_BUTTON);
        ItemMeta actionButtonMeta = actionButton.getItemMeta();
        actionButtonMeta.setDisplayName(colorize("&aAction Button"));
        actionButton.setItemMeta(actionButtonMeta);
        addItem(8, actionButton, () -> {
            player.sendMessage("Action Button selected");
        });

        ItemStack statLeaderboard = new ItemStack(Material.BOOK);
        ItemMeta statLeaderboardMeta = statLeaderboard.getItemMeta();
        statLeaderboardMeta.setDisplayName(colorize("&aStat Leaderboard"));
        statLeaderboard.setItemMeta(statLeaderboardMeta);
        addItem(9, statLeaderboard, () -> {
            player.sendMessage("Stat Leaderboard selected");
        });

        ItemStack parkourLeaderboard = new ItemStack(Material.BOOK);
        ItemMeta parkourLeaderboardMeta = parkourLeaderboard.getItemMeta();
        parkourLeaderboardMeta.setDisplayName(colorize("&aParkour Leaderboard"));
        parkourLeaderboard.setItemMeta(parkourLeaderboardMeta);
        addItem(10, parkourLeaderboard, () -> {
            player.sendMessage("Parkour Leaderboard selected");
        });

        ItemStack trashCan = new ItemStack(Material.CAULDRON);
        ItemMeta trashCanMeta = trashCan.getItemMeta();
        trashCanMeta.setDisplayName(colorize("&aTrash Can"));
        trashCan.setItemMeta(trashCanMeta);
        addItem(11, trashCan, () -> {
            player.sendMessage("Trash Can selected");
        });

        ItemStack biomeStick = new ItemStack(Material.STICK);
        ItemMeta biomeStickMeta = biomeStick.getItemMeta();
        biomeStickMeta.setDisplayName(colorize("&aBiome Stick"));
        biomeStick.setItemMeta(biomeStickMeta);
        addItem(12, biomeStick, () -> {
            player.sendMessage("Biome Stick selected");
        });

        ItemStack xSocialMediaHead = getCustomSkull("91b7a0c210e6cdf5a35fd8197e6e24a038315bbe3bdcd1bcc3630bf26f59ec5c");
        ItemMeta xSocialMediaHeadMeta = xSocialMediaHead.getItemMeta();
        xSocialMediaHeadMeta.setDisplayName(colorize("&bX"));
        xSocialMediaHead.setItemMeta(xSocialMediaHeadMeta);
        addItem(18, xSocialMediaHead, () -> {
            player.sendMessage("X Social Media selected");
        });

        ItemStack youtubeHead = getCustomSkull("3042c7bc2db85347e6a36928e9a146b4fc3e1def4febf279391be15de7f46e");
        ItemMeta youtubeHeadMeta = youtubeHead.getItemMeta();
        youtubeHeadMeta.setDisplayName(colorize("&cYoutube"));
        youtubeHead.setItemMeta(youtubeHeadMeta);
        addItem(19, youtubeHead, () -> {
            player.sendMessage("Youtube selected");
        });

        ItemStack instagramHead = getCustomSkull("ac88d6163fabe7c5e62450eb37a074e2e2c88611c998536dbd8429faa0819453");
        ItemMeta instagramHeadMeta = instagramHead.getItemMeta();
        instagramHeadMeta.setDisplayName(colorize("&7Instagram"));
        instagramHead.setItemMeta(instagramHeadMeta);
        addItem(20, instagramHead, () -> {
            player.sendMessage("Instagram selected");
        });

        ItemStack twitchHead = getCustomSkull("46be65f44cd21014c8cddd0158bf75227adcb1fd179f4c1acd158c88871a13f");
        ItemMeta twitchHeadMeta = twitchHead.getItemMeta();
        twitchHeadMeta.setDisplayName(colorize("&5Twitch"));
        twitchHead.setItemMeta(twitchHeadMeta);
        addItem(21, twitchHead, () -> {
            player.sendMessage("Twitch selected");
        });

        ItemStack discordHead = getCustomSkull("7873c12bffb5251a0b88d5ae75c7247cb39a75ff1a81cbe4c8a39b311ddeda");
        ItemMeta discordHeadMeta = discordHead.getItemMeta();
        discordHeadMeta.setDisplayName(colorize("&9Discord"));
        discordHead.setItemMeta(discordHeadMeta);
        addItem(22, discordHead, () -> {
            player.sendMessage("Discord selected");
        });

        ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
        ItemMeta dragonEggMeta = dragonEgg.getItemMeta();
        dragonEggMeta.setDisplayName(colorize("&fDragon Egg"));
        dragonEgg.setItemMeta(dragonEggMeta);
        addItem(26, dragonEgg, () -> {
            player.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new OwnerHousingMenu(main, player, house).open();
        });
    }
}
