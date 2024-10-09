package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class EntityChooseMenu extends Menu {

    private Main main;
    private Player player;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public EntityChooseMenu(Main main, Player player, HousesManager housesManager, HousingNPC housingNPC) {
        super(player, colorize("&7Edit NPC"), 54);
        this.player = player;
        this.housesManager = housesManager;
        this.housingNPC = housingNPC;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack playerEntity = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerEntityMeta = playerEntity.getItemMeta();
        playerEntityMeta.setDisplayName(colorize("&aPlayer"));
        playerEntity.setItemMeta(playerEntityMeta);
        addItem(0, playerEntity, () -> {
            housingNPC.setEntity(EntityType.PLAYER);
        });

        ItemStack zombie = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
        ItemMeta zombieMeta = zombie.getItemMeta();
        zombieMeta.setDisplayName(colorize("&aZombie"));
        zombie.setItemMeta(zombieMeta);
        addItem(1, zombie, () -> {
            housingNPC.setEntity(EntityType.ZOMBIE);
        });

        ItemStack skeleton = new ItemStack(Material.SKELETON_SPAWN_EGG);
        ItemMeta skeletonMeta = skeleton.getItemMeta();
        skeletonMeta.setDisplayName(colorize("&aSkeleton"));
        skeleton.setItemMeta(skeletonMeta);
        addItem(2, skeleton, () -> {
            housingNPC.setEntity(EntityType.SKELETON);
        });

        ItemStack sheep = new ItemStack(Material.SHEEP_SPAWN_EGG);
        ItemMeta sheepMeta = sheep.getItemMeta();
        sheepMeta.setDisplayName(colorize("&aSheep"));
        sheep.setItemMeta(sheepMeta);
        addItem(3, sheep, () -> {
            housingNPC.setEntity(EntityType.SHEEP);
        });

        ItemStack cow = new ItemStack(Material.COW_SPAWN_EGG);
        ItemMeta cowMeta = cow.getItemMeta();
        cowMeta.setDisplayName(colorize("&aCow"));
        cow.setItemMeta(cowMeta);
        addItem(4, cow, () -> {
            housingNPC.setEntity(EntityType.COW);
        });

        ItemStack pig = new ItemStack(Material.PIG_SPAWN_EGG);
        ItemMeta pigMeta = pig.getItemMeta();
        pigMeta.setDisplayName(colorize("&aPig"));
        pig.setItemMeta(pigMeta);
        addItem(5, pig, () -> {
            housingNPC.setEntity(EntityType.PIG);
        });

        ItemStack chicken = new ItemStack(Material.CHICKEN_SPAWN_EGG);
        ItemMeta chickenMeta = chicken.getItemMeta();
        chickenMeta.setDisplayName(colorize("&aChicken"));
        chicken.setItemMeta(chickenMeta);
        addItem(6, chicken, () -> {
            housingNPC.setEntity(EntityType.CHICKEN);
        });

        ItemStack horse = new ItemStack(Material.HORSE_SPAWN_EGG);
        ItemMeta horseMeta = horse.getItemMeta();
        horseMeta.setDisplayName(colorize("&aHorse"));
        horse.setItemMeta(horseMeta);
        addItem(7, horse, () -> {
            housingNPC.setEntity(EntityType.HORSE);
        });

        ItemStack villager = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta villagerMeta = villager.getItemMeta();
        villagerMeta.setDisplayName(colorize("&aVillager"));
        villager.setItemMeta(villagerMeta);
        addItem(8, villager, () -> {
            housingNPC.setEntity(EntityType.VILLAGER);
        });

        ItemStack creeper = new ItemStack(Material.CREEPER_SPAWN_EGG);
        ItemMeta creeperMeta = creeper.getItemMeta();
        creeperMeta.setDisplayName(colorize("&aCreeper"));
        creeper.setItemMeta(creeperMeta);
        addItem(9, creeper, () -> {
            housingNPC.setEntity(EntityType.CREEPER);
        });

        ItemStack spider = new ItemStack(Material.SPIDER_SPAWN_EGG);
        ItemMeta spiderMeta = spider.getItemMeta();
        spiderMeta.setDisplayName(colorize("&aSpider"));
        spider.setItemMeta(spiderMeta);
        addItem(10, spider, () -> {
            housingNPC.setEntity(EntityType.SPIDER);
        });

        ItemStack enderman = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
        ItemMeta endermanMeta = enderman.getItemMeta();
        endermanMeta.setDisplayName(colorize("&aEnderman"));
        enderman.setItemMeta(endermanMeta);
        addItem(11, enderman, () -> {
            housingNPC.setEntity(EntityType.ENDERMAN);
        });

        ItemStack wolf = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta wolfMeta = wolf.getItemMeta();
        wolfMeta.setDisplayName(colorize("&aWolf"));
        wolf.setItemMeta(wolfMeta);
        addItem(12, wolf, () -> {
            housingNPC.setEntity(EntityType.WOLF);
        });

        ItemStack cat = new ItemStack(Material.CAT_SPAWN_EGG);
        ItemMeta catMeta = cat.getItemMeta();
        catMeta.setDisplayName(colorize("&aCat"));
        cat.setItemMeta(catMeta);
        addItem(13, cat, () -> {
            housingNPC.setEntity(EntityType.CAT);
        });

        ItemStack fox = new ItemStack(Material.FOX_SPAWN_EGG);
        ItemMeta foxMeta = fox.getItemMeta();
        foxMeta.setDisplayName(colorize("&aFox"));
        fox.setItemMeta(foxMeta);
        addItem(14, fox, () -> {
            housingNPC.setEntity(EntityType.FOX);
        });

        ItemStack parrot = new ItemStack(Material.PARROT_SPAWN_EGG);
        ItemMeta parrotMeta = parrot.getItemMeta();
        parrotMeta.setDisplayName(colorize("&aParrot"));
        parrot.setItemMeta(parrotMeta);
        addItem(15, parrot, () -> {
            housingNPC.setEntity(EntityType.PARROT);
        });

        ItemStack llama = new ItemStack(Material.LLAMA_SPAWN_EGG);
        ItemMeta llamaMeta = llama.getItemMeta();
        llamaMeta.setDisplayName(colorize("&aLlama"));
        llama.setItemMeta(llamaMeta);
        addItem(16, llama, () -> {
            housingNPC.setEntity(EntityType.LLAMA);
        });

        ItemStack panda = new ItemStack(Material.PANDA_SPAWN_EGG);
        ItemMeta pandaMeta = panda.getItemMeta();
        pandaMeta.setDisplayName(colorize("&aPanda"));
        panda.setItemMeta(pandaMeta);
        addItem(17, panda, () -> {
            housingNPC.setEntity(EntityType.PANDA);
        });

        ItemStack bat = new ItemStack(Material.BAT_SPAWN_EGG);
        ItemMeta batMeta = bat.getItemMeta();
        batMeta.setDisplayName(colorize("&aBat"));
        bat.setItemMeta(batMeta);
        addItem(18, bat, () -> {
            housingNPC.setEntity(EntityType.BAT);
        });

        ItemStack squid = new ItemStack(Material.SQUID_SPAWN_EGG);
        ItemMeta squidMeta = squid.getItemMeta();
        squidMeta.setDisplayName(colorize("&aSquid"));
        squid.setItemMeta(squidMeta);
        addItem(19, squid, () -> {
            housingNPC.setEntity(EntityType.SQUID);
        });

        ItemStack dolphin = new ItemStack(Material.DOLPHIN_SPAWN_EGG);
        ItemMeta dolphinMeta = dolphin.getItemMeta();
        dolphinMeta.setDisplayName(colorize("&aDolphin"));
        dolphin.setItemMeta(dolphinMeta);
        addItem(20, dolphin, () -> {
            housingNPC.setEntity(EntityType.DOLPHIN);
        });

        ItemStack bee = new ItemStack(Material.BEE_SPAWN_EGG);
        ItemMeta beeMeta = bee.getItemMeta();
        beeMeta.setDisplayName(colorize("&aBee"));
        bee.setItemMeta(beeMeta);
        addItem(21, bee, () -> {
            housingNPC.setEntity(EntityType.BEE);
        });

        ItemStack ocelot = new ItemStack(Material.OCELOT_SPAWN_EGG);
        ItemMeta ocelotMeta = ocelot.getItemMeta();
        ocelotMeta.setDisplayName(colorize("&aOcelot"));
        ocelot.setItemMeta(ocelotMeta);
        addItem(22, ocelot, () -> {
            housingNPC.setEntity(EntityType.OCELOT);
        });

        ItemStack polarBear = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG);
        ItemMeta polarBearMeta = polarBear.getItemMeta();
        polarBearMeta.setDisplayName(colorize("&aPolar Bear"));
        polarBear.setItemMeta(polarBearMeta);
        addItem(23, polarBear, () -> {
            housingNPC.setEntity(EntityType.POLAR_BEAR);
        });

        ItemStack rabbit = new ItemStack(Material.RABBIT_SPAWN_EGG);
        ItemMeta rabbitMeta = rabbit.getItemMeta();
        rabbitMeta.setDisplayName(colorize("&aRabbit"));
        rabbit.setItemMeta(rabbitMeta);
        addItem(24, rabbit, () -> {
            housingNPC.setEntity(EntityType.RABBIT);
        });

        ItemStack witch = new ItemStack(Material.WITCH_SPAWN_EGG);
        ItemMeta witchMeta = witch.getItemMeta();
        witchMeta.setDisplayName(colorize("&aWitch"));
        witch.setItemMeta(witchMeta);
        addItem(25, witch, () -> {
            housingNPC.setEntity(EntityType.WITCH);
        });

        ItemStack ghast = new ItemStack(Material.GHAST_SPAWN_EGG);
        ItemMeta ghastMeta = ghast.getItemMeta();
        ghastMeta.setDisplayName(colorize("&aGhast"));
        ghast.setItemMeta(ghastMeta);
        addItem(26, ghast, () -> {
            housingNPC.setEntity(EntityType.GHAST);
        });

        ItemStack witherSkeleton = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG);
        ItemMeta witherSkeletonMeta = witherSkeleton.getItemMeta();
        witherSkeletonMeta.setDisplayName(colorize("&aWither Skeleton"));
        witherSkeleton.setItemMeta(witherSkeletonMeta);
        addItem(27, witherSkeleton, () -> {
            housingNPC.setEntity(EntityType.WITHER_SKELETON);
        });

        ItemStack blaze = new ItemStack(Material.BLAZE_SPAWN_EGG);
        ItemMeta blazeMeta = blaze.getItemMeta();
        blazeMeta.setDisplayName(colorize("&aBlaze"));
        blaze.setItemMeta(blazeMeta);
        addItem(28, blaze, () -> {
            housingNPC.setEntity(EntityType.BLAZE);
        });

        ItemStack slime = new ItemStack(Material.SLIME_SPAWN_EGG);
        ItemMeta slimeMeta = slime.getItemMeta();
        slimeMeta.setDisplayName(colorize("&aSlime"));
        slime.setItemMeta(slimeMeta);
        addItem(29, slime, () -> {
            housingNPC.setEntity(EntityType.SLIME);
        });

        ItemStack magmaCube = new ItemStack(Material.MAGMA_CUBE_SPAWN_EGG);
        ItemMeta magmaCubeMeta = magmaCube.getItemMeta();
        magmaCubeMeta.setDisplayName(colorize("&aMagma Cube"));
        magmaCube.setItemMeta(magmaCubeMeta);
        addItem(30, magmaCube, () -> {
            housingNPC.setEntity(EntityType.MAGMA_CUBE);
        });

        ItemStack guardian = new ItemStack(Material.GUARDIAN_SPAWN_EGG);
        ItemMeta guardianMeta = guardian.getItemMeta();
        guardianMeta.setDisplayName(colorize("&aGuardian"));
        guardian.setItemMeta(guardianMeta);
        addItem(31, guardian, () -> {
            housingNPC.setEntity(EntityType.GUARDIAN);
        });

        ItemStack elderGuardian = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
        ItemMeta elderGuardianMeta = elderGuardian.getItemMeta();
        elderGuardianMeta.setDisplayName(colorize("&aElder Guardian"));
        elderGuardian.setItemMeta(elderGuardianMeta);
        addItem(32, elderGuardian, () -> {
            housingNPC.setEntity(EntityType.ELDER_GUARDIAN);
        });

        ItemStack phantom = new ItemStack(Material.PHANTOM_SPAWN_EGG);
        ItemMeta phantomMeta = phantom.getItemMeta();
        phantomMeta.setDisplayName(colorize("&aPhantom"));
        phantom.setItemMeta(phantomMeta);
        addItem(33, phantom, () -> {
            housingNPC.setEntity(EntityType.PHANTOM);
        });

        ItemStack vex = new ItemStack(Material.VEX_SPAWN_EGG);
        ItemMeta vexMeta = vex.getItemMeta();
        vexMeta.setDisplayName(colorize("&aVex"));
        vex.setItemMeta(vexMeta);
        addItem(34, vex, () -> {
            housingNPC.setEntity(EntityType.VEX);
        });

        ItemStack evoker = new ItemStack(Material.EVOKER_SPAWN_EGG);
        ItemMeta evokerMeta = evoker.getItemMeta();
        evokerMeta.setDisplayName(colorize("&aEvoker"));
        evoker.setItemMeta(evokerMeta);
        addItem(35, evoker, () -> {
            housingNPC.setEntity(EntityType.EVOKER);
        });

        ItemStack pillager = new ItemStack(Material.PILLAGER_SPAWN_EGG);
        ItemMeta pillagerMeta = pillager.getItemMeta();
        pillagerMeta.setDisplayName(colorize("&aPillager"));
        pillager.setItemMeta(pillagerMeta);
        addItem(36, pillager, () -> {
            housingNPC.setEntity(EntityType.PILLAGER);
        });

        ItemStack ravager = new ItemStack(Material.RAVAGER_SPAWN_EGG);
        ItemMeta ravagerMeta = ravager.getItemMeta();
        ravagerMeta.setDisplayName(colorize("&aRavager"));
        ravager.setItemMeta(ravagerMeta);
        addItem(37, ravager, () -> {
            housingNPC.setEntity(EntityType.RAVAGER);
        });

        ItemStack strider = new ItemStack(Material.STRIDER_SPAWN_EGG);
        ItemMeta striderMeta = strider.getItemMeta();
        striderMeta.setDisplayName(colorize("&aStrider"));
        strider.setItemMeta(striderMeta);
        addItem(38, strider, () -> {
            housingNPC.setEntity(EntityType.STRIDER);
        });

        ItemStack piglin = new ItemStack(Material.PIGLIN_SPAWN_EGG);
        ItemMeta piglinMeta = piglin.getItemMeta();
        piglinMeta.setDisplayName(colorize("&aPiglin"));
        piglin.setItemMeta(piglinMeta);
        addItem(39, piglin, () -> {
            housingNPC.setEntity(EntityType.PIGLIN);
        });

        ItemStack hoglin = new ItemStack(Material.HOGLIN_SPAWN_EGG);
        ItemMeta hoglinMeta = hoglin.getItemMeta();
        hoglinMeta.setDisplayName(colorize("&aHoglin"));
        hoglin.setItemMeta(hoglinMeta);
        addItem(40, hoglin, () -> {
            housingNPC.setEntity(EntityType.HOGLIN);
        });

        ItemStack zoglin = new ItemStack(Material.ZOGLIN_SPAWN_EGG);
        ItemMeta zoglinMeta = zoglin.getItemMeta();
        zoglinMeta.setDisplayName(colorize("&aZoglin"));
        zoglin.setItemMeta(zoglinMeta);
        addItem(41, zoglin, () -> {
            housingNPC.setEntity(EntityType.ZOGLIN);
        });

        ItemStack striderSpawn = new ItemStack(Material.STRIDER_SPAWN_EGG);
        ItemMeta striderSpawnMeta = striderSpawn.getItemMeta();
        striderSpawnMeta.setDisplayName(colorize("&aStrider"));
        striderSpawn.setItemMeta(striderSpawnMeta);
        addItem(42, striderSpawn, () -> {
            housingNPC.setEntity(EntityType.STRIDER);
        });

        ItemStack shulker = new ItemStack(Material.SHULKER_SPAWN_EGG);
        ItemMeta shulkerMeta = shulker.getItemMeta();
        shulkerMeta.setDisplayName(colorize("&aShulker"));
        shulker.setItemMeta(shulkerMeta);
        addItem(43, shulker, () -> {
            housingNPC.setEntity(EntityType.SHULKER);
        });

        ItemStack wither = new ItemStack(Material.WITHER_SPAWN_EGG);
        ItemMeta witherMeta = wither.getItemMeta();
        witherMeta.setDisplayName(colorize("&aWither"));
        wither.setItemMeta(witherMeta);
        addItem(44, wither, () -> {
            housingNPC.setEntity(EntityType.WITHER);
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }
}
