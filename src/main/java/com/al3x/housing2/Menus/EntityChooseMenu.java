package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
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

    private void setEntity(HousingNPC housingNPC, EntityType entityType) {
        Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpcById(housingNPC.getNpcUUID());
        npc.getData().setType(entityType);
        npc.updateForAll();
        npc.removeForAll();
        npc.spawnForAll();
    }

    @Override
    public void setupItems() {
        // Basic entities
        ItemStack zombie = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
        ItemMeta zombieMeta = zombie.getItemMeta();
        zombieMeta.setDisplayName(colorize("&aZombie"));
        zombie.setItemMeta(zombieMeta);
        addItem(0, zombie, () -> {
            setEntity(housingNPC, EntityType.ZOMBIE);
        });

        ItemStack skeleton = new ItemStack(Material.SKELETON_SPAWN_EGG);
        ItemMeta skeletonMeta = skeleton.getItemMeta();
        skeletonMeta.setDisplayName(colorize("&aSkeleton"));
        skeleton.setItemMeta(skeletonMeta);
        addItem(1, skeleton, () -> {
            setEntity(housingNPC, EntityType.SKELETON);
        });

        ItemStack sheep = new ItemStack(Material.SHEEP_SPAWN_EGG);
        ItemMeta sheepMeta = sheep.getItemMeta();
        sheepMeta.setDisplayName(colorize("&aSheep"));
        sheep.setItemMeta(sheepMeta);
        addItem(2, sheep, () -> {
            setEntity(housingNPC, EntityType.SHEEP);
        });

        ItemStack cow = new ItemStack(Material.COW_SPAWN_EGG);
        ItemMeta cowMeta = cow.getItemMeta();
        cowMeta.setDisplayName(colorize("&aCow"));
        cow.setItemMeta(cowMeta);
        addItem(3, cow, () -> {
            setEntity(housingNPC, EntityType.COW);
        });

        ItemStack pig = new ItemStack(Material.PIG_SPAWN_EGG);
        ItemMeta pigMeta = pig.getItemMeta();
        pigMeta.setDisplayName(colorize("&aPig"));
        pig.setItemMeta(pigMeta);
        addItem(4, pig, () -> {
            setEntity(housingNPC, EntityType.PIG);
        });

        ItemStack chicken = new ItemStack(Material.CHICKEN_SPAWN_EGG);
        ItemMeta chickenMeta = chicken.getItemMeta();
        chickenMeta.setDisplayName(colorize("&aChicken"));
        chicken.setItemMeta(chickenMeta);
        addItem(5, chicken, () -> {
            setEntity(housingNPC, EntityType.CHICKEN);
        });

        ItemStack horse = new ItemStack(Material.HORSE_SPAWN_EGG);
        ItemMeta horseMeta = horse.getItemMeta();
        horseMeta.setDisplayName(colorize("&aHorse"));
        horse.setItemMeta(horseMeta);
        addItem(6, horse, () -> {
            setEntity(housingNPC, EntityType.HORSE);
        });

        ItemStack villager = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta villagerMeta = villager.getItemMeta();
        villagerMeta.setDisplayName(colorize("&aVillager"));
        villager.setItemMeta(villagerMeta);
        addItem(7, villager, () -> {
            setEntity(housingNPC, EntityType.VILLAGER);
        });

        ItemStack creeper = new ItemStack(Material.CREEPER_SPAWN_EGG);
        ItemMeta creeperMeta = creeper.getItemMeta();
        creeperMeta.setDisplayName(colorize("&aCreeper"));
        creeper.setItemMeta(creeperMeta);
        addItem(8, creeper, () -> {
            setEntity(housingNPC, EntityType.CREEPER);
        });

        ItemStack spider = new ItemStack(Material.SPIDER_SPAWN_EGG);
        ItemMeta spiderMeta = spider.getItemMeta();
        spiderMeta.setDisplayName(colorize("&aSpider"));
        spider.setItemMeta(spiderMeta);
        addItem(9, spider, () -> {
            setEntity(housingNPC, EntityType.SPIDER);
        });

        ItemStack enderman = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
        ItemMeta endermanMeta = enderman.getItemMeta();
        endermanMeta.setDisplayName(colorize("&aEnderman"));
        enderman.setItemMeta(endermanMeta);
        addItem(10, enderman, () -> {
            setEntity(housingNPC, EntityType.ENDERMAN);
        });

        ItemStack wolf = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta wolfMeta = wolf.getItemMeta();
        wolfMeta.setDisplayName(colorize("&aWolf"));
        wolf.setItemMeta(wolfMeta);
        addItem(11, wolf, () -> {
            setEntity(housingNPC, EntityType.WOLF);
        });

        ItemStack cat = new ItemStack(Material.CAT_SPAWN_EGG);
        ItemMeta catMeta = cat.getItemMeta();
        catMeta.setDisplayName(colorize("&aCat"));
        cat.setItemMeta(catMeta);
        addItem(12, cat, () -> {
            setEntity(housingNPC, EntityType.CAT);
        });

        ItemStack fox = new ItemStack(Material.FOX_SPAWN_EGG);
        ItemMeta foxMeta = fox.getItemMeta();
        foxMeta.setDisplayName(colorize("&aFox"));
        fox.setItemMeta(foxMeta);
        addItem(13, fox, () -> {
            setEntity(housingNPC, EntityType.FOX);
        });

        ItemStack parrot = new ItemStack(Material.PARROT_SPAWN_EGG);
        ItemMeta parrotMeta = parrot.getItemMeta();
        parrotMeta.setDisplayName(colorize("&aParrot"));
        parrot.setItemMeta(parrotMeta);
        addItem(14, parrot, () -> {
            setEntity(housingNPC, EntityType.PARROT);
        });

        ItemStack llama = new ItemStack(Material.LLAMA_SPAWN_EGG);
        ItemMeta llamaMeta = llama.getItemMeta();
        llamaMeta.setDisplayName(colorize("&aLlama"));
        llama.setItemMeta(llamaMeta);
        addItem(15, llama, () -> {
            setEntity(housingNPC, EntityType.LLAMA);
        });

        ItemStack panda = new ItemStack(Material.PANDA_SPAWN_EGG);
        ItemMeta pandaMeta = panda.getItemMeta();
        pandaMeta.setDisplayName(colorize("&aPanda"));
        panda.setItemMeta(pandaMeta);
        addItem(16, panda, () -> {
            setEntity(housingNPC, EntityType.PANDA);
        });

        ItemStack bat = new ItemStack(Material.BAT_SPAWN_EGG);
        ItemMeta batMeta = bat.getItemMeta();
        batMeta.setDisplayName(colorize("&aBat"));
        bat.setItemMeta(batMeta);
        addItem(17, bat, () -> {
            setEntity(housingNPC, EntityType.BAT);
        });

        ItemStack squid = new ItemStack(Material.SQUID_SPAWN_EGG);
        ItemMeta squidMeta = squid.getItemMeta();
        squidMeta.setDisplayName(colorize("&aSquid"));
        squid.setItemMeta(squidMeta);
        addItem(18, squid, () -> {
            setEntity(housingNPC, EntityType.SQUID);
        });

        ItemStack dolphin = new ItemStack(Material.DOLPHIN_SPAWN_EGG);
        ItemMeta dolphinMeta = dolphin.getItemMeta();
        dolphinMeta.setDisplayName(colorize("&aDolphin"));
        dolphin.setItemMeta(dolphinMeta);
        addItem(19, dolphin, () -> {
            setEntity(housingNPC, EntityType.DOLPHIN);
        });

        ItemStack bee = new ItemStack(Material.BEE_SPAWN_EGG);
        ItemMeta beeMeta = bee.getItemMeta();
        beeMeta.setDisplayName(colorize("&aBee"));
        bee.setItemMeta(beeMeta);
        addItem(20, bee, () -> {
            setEntity(housingNPC, EntityType.BEE);
        });

        ItemStack ocelot = new ItemStack(Material.OCELOT_SPAWN_EGG);
        ItemMeta ocelotMeta = ocelot.getItemMeta();
        ocelotMeta.setDisplayName(colorize("&aOcelot"));
        ocelot.setItemMeta(ocelotMeta);
        addItem(21, ocelot, () -> {
            setEntity(housingNPC, EntityType.OCELOT);
        });

        ItemStack polarBear = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG);
        ItemMeta polarBearMeta = polarBear.getItemMeta();
        polarBearMeta.setDisplayName(colorize("&aPolar Bear"));
        polarBear.setItemMeta(polarBearMeta);
        addItem(22, polarBear, () -> {
            setEntity(housingNPC, EntityType.POLAR_BEAR);
        });

        ItemStack rabbit = new ItemStack(Material.RABBIT_SPAWN_EGG);
        ItemMeta rabbitMeta = rabbit.getItemMeta();
        rabbitMeta.setDisplayName(colorize("&aRabbit"));
        rabbit.setItemMeta(rabbitMeta);
        addItem(23, rabbit, () -> {
            setEntity(housingNPC, EntityType.RABBIT);
        });

        ItemStack turtle = new ItemStack(Material.TURTLE_SPAWN_EGG);
        ItemMeta turtleMeta = turtle.getItemMeta();
        turtleMeta.setDisplayName(colorize("&aTurtle"));
        turtle.setItemMeta(turtleMeta);
        addItem(24, turtle, () -> {
            setEntity(housingNPC, EntityType.TURTLE);
        });

        ItemStack witch = new ItemStack(Material.WITCH_SPAWN_EGG);
        ItemMeta witchMeta = witch.getItemMeta();
        witchMeta.setDisplayName(colorize("&aWitch"));
        witch.setItemMeta(witchMeta);
        addItem(25, witch, () -> {
            setEntity(housingNPC, EntityType.WITCH);
        });

        ItemStack ghast = new ItemStack(Material.GHAST_SPAWN_EGG);
        ItemMeta ghastMeta = ghast.getItemMeta();
        ghastMeta.setDisplayName(colorize("&aGhast"));
        ghast.setItemMeta(ghastMeta);
        addItem(26, ghast, () -> {
            setEntity(housingNPC, EntityType.GHAST);
        });

        ItemStack witherSkeleton = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG);
        ItemMeta witherSkeletonMeta = witherSkeleton.getItemMeta();
        witherSkeletonMeta.setDisplayName(colorize("&aWither Skeleton"));
        witherSkeleton.setItemMeta(witherSkeletonMeta);
        addItem(27, witherSkeleton, () -> {
            setEntity(housingNPC, EntityType.WITHER_SKELETON);
        });

        ItemStack blaze = new ItemStack(Material.BLAZE_SPAWN_EGG);
        ItemMeta blazeMeta = blaze.getItemMeta();
        blazeMeta.setDisplayName(colorize("&aBlaze"));
        blaze.setItemMeta(blazeMeta);
        addItem(28, blaze, () -> {
            setEntity(housingNPC, EntityType.BLAZE);
        });

        ItemStack slime = new ItemStack(Material.SLIME_SPAWN_EGG);
        ItemMeta slimeMeta = slime.getItemMeta();
        slimeMeta.setDisplayName(colorize("&aSlime"));
        slime.setItemMeta(slimeMeta);
        addItem(29, slime, () -> {
            setEntity(housingNPC, EntityType.SLIME);
        });

        ItemStack magmaCube = new ItemStack(Material.MAGMA_CUBE_SPAWN_EGG);
        ItemMeta magmaCubeMeta = magmaCube.getItemMeta();
        magmaCubeMeta.setDisplayName(colorize("&aMagma Cube"));
        magmaCube.setItemMeta(magmaCubeMeta);
        addItem(30, magmaCube, () -> {
            setEntity(housingNPC, EntityType.MAGMA_CUBE);
        });

        ItemStack guardian = new ItemStack(Material.GUARDIAN_SPAWN_EGG);
        ItemMeta guardianMeta = guardian.getItemMeta();
        guardianMeta.setDisplayName(colorize("&aGuardian"));
        guardian.setItemMeta(guardianMeta);
        addItem(31, guardian, () -> {
            setEntity(housingNPC, EntityType.GUARDIAN);
        });

        ItemStack elderGuardian = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
        ItemMeta elderGuardianMeta = elderGuardian.getItemMeta();
        elderGuardianMeta.setDisplayName(colorize("&aElder Guardian"));
        elderGuardian.setItemMeta(elderGuardianMeta);
        addItem(32, elderGuardian, () -> {
            setEntity(housingNPC, EntityType.ELDER_GUARDIAN);
        });

        ItemStack phantom = new ItemStack(Material.PHANTOM_SPAWN_EGG);
        ItemMeta phantomMeta = phantom.getItemMeta();
        phantomMeta.setDisplayName(colorize("&aPhantom"));
        phantom.setItemMeta(phantomMeta);
        addItem(33, phantom, () -> {
            setEntity(housingNPC, EntityType.PHANTOM);
        });

        ItemStack vex = new ItemStack(Material.VEX_SPAWN_EGG);
        ItemMeta vexMeta = vex.getItemMeta();
        vexMeta.setDisplayName(colorize("&aVex"));
        vex.setItemMeta(vexMeta);
        addItem(34, vex, () -> {
            setEntity(housingNPC, EntityType.VEX);
        });

        ItemStack evoker = new ItemStack(Material.EVOKER_SPAWN_EGG);
        ItemMeta evokerMeta = evoker.getItemMeta();
        evokerMeta.setDisplayName(colorize("&aEvoker"));
        evoker.setItemMeta(evokerMeta);
        addItem(35, evoker, () -> {
            setEntity(housingNPC, EntityType.EVOKER);
        });

        ItemStack pillager = new ItemStack(Material.PILLAGER_SPAWN_EGG);
        ItemMeta pillagerMeta = pillager.getItemMeta();
        pillagerMeta.setDisplayName(colorize("&aPillager"));
        pillager.setItemMeta(pillagerMeta);
        addItem(36, pillager, () -> {
            setEntity(housingNPC, EntityType.PILLAGER);
        });

        ItemStack ravager = new ItemStack(Material.RAVAGER_SPAWN_EGG);
        ItemMeta ravagerMeta = ravager.getItemMeta();
        ravagerMeta.setDisplayName(colorize("&aRavager"));
        ravager.setItemMeta(ravagerMeta);
        addItem(37, ravager, () -> {
            setEntity(housingNPC, EntityType.RAVAGER);
        });

        ItemStack strider = new ItemStack(Material.STRIDER_SPAWN_EGG);
        ItemMeta striderMeta = strider.getItemMeta();
        striderMeta.setDisplayName(colorize("&aStrider"));
        strider.setItemMeta(striderMeta);
        addItem(38, strider, () -> {
            setEntity(housingNPC, EntityType.STRIDER);
        });

        ItemStack piglin = new ItemStack(Material.PIGLIN_SPAWN_EGG);
        ItemMeta piglinMeta = piglin.getItemMeta();
        piglinMeta.setDisplayName(colorize("&aPiglin"));
        piglin.setItemMeta(piglinMeta);
        addItem(39, piglin, () -> {
            setEntity(housingNPC, EntityType.PIGLIN);
        });

        ItemStack hoglin = new ItemStack(Material.HOGLIN_SPAWN_EGG);
        ItemMeta hoglinMeta = hoglin.getItemMeta();
        hoglinMeta.setDisplayName(colorize("&aHoglin"));
        hoglin.setItemMeta(hoglinMeta);
        addItem(40, hoglin, () -> {
            setEntity(housingNPC, EntityType.HOGLIN);
        });

        ItemStack zoglin = new ItemStack(Material.ZOGLIN_SPAWN_EGG);
        ItemMeta zoglinMeta = zoglin.getItemMeta();
        zoglinMeta.setDisplayName(colorize("&aZoglin"));
        zoglin.setItemMeta(zoglinMeta);
        addItem(41, zoglin, () -> {
            setEntity(housingNPC, EntityType.ZOGLIN);
        });

        ItemStack striderSpawn = new ItemStack(Material.STRIDER_SPAWN_EGG);
        ItemMeta striderSpawnMeta = striderSpawn.getItemMeta();
        striderSpawnMeta.setDisplayName(colorize("&aStrider"));
        striderSpawn.setItemMeta(striderSpawnMeta);
        addItem(42, striderSpawn, () -> {
            setEntity(housingNPC, EntityType.STRIDER);
        });

        ItemStack shulker = new ItemStack(Material.SHULKER_SPAWN_EGG);
        ItemMeta shulkerMeta = shulker.getItemMeta();
        shulkerMeta.setDisplayName(colorize("&aShulker"));
        shulker.setItemMeta(shulkerMeta);
        addItem(43, shulker, () -> {
            setEntity(housingNPC, EntityType.SHULKER);
        });

        ItemStack wither = new ItemStack(Material.WITHER_SPAWN_EGG);
        ItemMeta witherMeta = wither.getItemMeta();
        witherMeta.setDisplayName(colorize("&aWither"));
        wither.setItemMeta(witherMeta);
        addItem(44, wither, () -> {
            setEntity(housingNPC, EntityType.WITHER);
        });

        ItemStack enderDragon = new ItemStack(Material.DRAGON_EGG);
        ItemMeta enderDragonMeta = enderDragon.getItemMeta();
        enderDragonMeta.setDisplayName(colorize("&aEnder Dragon"));
        enderDragon.setItemMeta(enderDragonMeta);
        addItem(45, enderDragon, () -> {
            setEntity(housingNPC, EntityType.ENDER_DRAGON);
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new NPCMenu(main, player, housesManager, housingNPC).open();
        });
    }
}
