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
        this.main = main;
        this.player = player;
        this.housesManager = housesManager;
        this.housingNPC = housingNPC;
        setupItems();
    }

    private void addEntityItem(int slot, Material material, String displayName, EntityType entityType) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorize(displayName));
        item.setItemMeta(meta);
        addItem(slot, item, () -> housingNPC.setEntity(entityType));
    }

    @Override
    public void initItems() {
        addEntityItem(0, Material.PLAYER_HEAD, "&aPlayer", EntityType.PLAYER);
        addEntityItem(1, Material.ZOMBIE_SPAWN_EGG, "&aZombie", EntityType.ZOMBIE);
        addEntityItem(2, Material.SKELETON_SPAWN_EGG, "&aSkeleton", EntityType.SKELETON);
        addEntityItem(3, Material.SHEEP_SPAWN_EGG, "&aSheep", EntityType.SHEEP);
        addEntityItem(4, Material.COW_SPAWN_EGG, "&aCow", EntityType.COW);
        addEntityItem(5, Material.PIG_SPAWN_EGG, "&aPig", EntityType.PIG);
        addEntityItem(6, Material.CHICKEN_SPAWN_EGG, "&aChicken", EntityType.CHICKEN);
        addEntityItem(7, Material.HORSE_SPAWN_EGG, "&aHorse", EntityType.HORSE);
        addEntityItem(8, Material.VILLAGER_SPAWN_EGG, "&aVillager", EntityType.VILLAGER);
        addEntityItem(9, Material.CREEPER_SPAWN_EGG, "&aCreeper", EntityType.CREEPER);
        addEntityItem(10, Material.SPIDER_SPAWN_EGG, "&aSpider", EntityType.SPIDER);
        addEntityItem(11, Material.ENDERMAN_SPAWN_EGG, "&aEnderman", EntityType.ENDERMAN);
        addEntityItem(12, Material.WOLF_SPAWN_EGG, "&aWolf", EntityType.WOLF);
        addEntityItem(13, Material.CAT_SPAWN_EGG, "&aCat", EntityType.CAT);
        addEntityItem(14, Material.FOX_SPAWN_EGG, "&aFox", EntityType.FOX);
        addEntityItem(15, Material.PARROT_SPAWN_EGG, "&aParrot", EntityType.PARROT);
        addEntityItem(16, Material.LLAMA_SPAWN_EGG, "&aLlama", EntityType.LLAMA);
        addEntityItem(17, Material.PANDA_SPAWN_EGG, "&aPanda", EntityType.PANDA);
        addEntityItem(18, Material.BAT_SPAWN_EGG, "&aBat", EntityType.BAT);
        addEntityItem(19, Material.SQUID_SPAWN_EGG, "&aSquid", EntityType.SQUID);
        addEntityItem(20, Material.DOLPHIN_SPAWN_EGG, "&aDolphin", EntityType.DOLPHIN);
        addEntityItem(21, Material.BEE_SPAWN_EGG, "&aBee", EntityType.BEE);
        addEntityItem(22, Material.OCELOT_SPAWN_EGG, "&aOcelot", EntityType.OCELOT);
        addEntityItem(23, Material.POLAR_BEAR_SPAWN_EGG, "&aPolar Bear", EntityType.POLAR_BEAR);
        addEntityItem(24, Material.RABBIT_SPAWN_EGG, "&aRabbit", EntityType.RABBIT);
        addEntityItem(25, Material.WITCH_SPAWN_EGG, "&aWitch", EntityType.WITCH);
        addEntityItem(26, Material.GHAST_SPAWN_EGG, "&aGhast", EntityType.GHAST);
        addEntityItem(27, Material.WITHER_SKELETON_SPAWN_EGG, "&aWither Skeleton", EntityType.WITHER_SKELETON);
        addEntityItem(28, Material.BLAZE_SPAWN_EGG, "&aBlaze", EntityType.BLAZE);
        addEntityItem(29, Material.SLIME_SPAWN_EGG, "&aSlime", EntityType.SLIME);
        addEntityItem(30, Material.MAGMA_CUBE_SPAWN_EGG, "&aMagma Cube", EntityType.MAGMA_CUBE);
        addEntityItem(31, Material.GUARDIAN_SPAWN_EGG, "&aGuardian", EntityType.GUARDIAN);
        addEntityItem(32, Material.ELDER_GUARDIAN_SPAWN_EGG, "&aElder Guardian", EntityType.ELDER_GUARDIAN);
        addEntityItem(33, Material.PHANTOM_SPAWN_EGG, "&aPhantom", EntityType.PHANTOM);
        addEntityItem(34, Material.VEX_SPAWN_EGG, "&aVex", EntityType.VEX);
        addEntityItem(35, Material.EVOKER_SPAWN_EGG, "&aEvoker", EntityType.EVOKER);
        addEntityItem(36, Material.PILLAGER_SPAWN_EGG, "&aPillager", EntityType.PILLAGER);
        addEntityItem(37, Material.RAVAGER_SPAWN_EGG, "&aRavager", EntityType.RAVAGER);
        addEntityItem(38, Material.STRIDER_SPAWN_EGG, "&aStrider", EntityType.STRIDER);
        addEntityItem(39, Material.PIGLIN_SPAWN_EGG, "&aPiglin", EntityType.PIGLIN);
        addEntityItem(40, Material.HOGLIN_SPAWN_EGG, "&aHoglin", EntityType.HOGLIN);
        addEntityItem(41, Material.ZOGLIN_SPAWN_EGG, "&aZoglin", EntityType.ZOGLIN);
        addEntityItem(42, Material.STRIDER_SPAWN_EGG, "&aStrider", EntityType.STRIDER);
        addEntityItem(43, Material.SHULKER_SPAWN_EGG, "&aShulker", EntityType.SHULKER);
        addEntityItem(44, Material.WITHER_SPAWN_EGG, "&aWither", EntityType.WITHER);

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(49, backArrow, () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }
}
