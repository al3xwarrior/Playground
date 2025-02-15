package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import me.arcaniax.hdb.object.head.Head;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class HeadsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    public HeadsMenu(Main main, Player player, HousingWorld house) {
        super(player, "&6Heads Menu", 36);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }

    @Override
    public void setupItems() {
        HeadDatabaseAPI headDatabaseAPI = main.getHeadDatabaseAPI();

        // Alphabet
        ItemStack alphabet = headDatabaseAPI.getItemHead("164");
        SkullMeta alphabetMeta = (SkullMeta) alphabet.getItemMeta();
        alphabetMeta.setDisplayName(colorize("&aAlphabet"));
        alphabet.setItemMeta(alphabetMeta);
        addItem(10, alphabet, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.ALPHABET);
            new IndivisualHeadsMenu(main, player, "Alphabet", heads, house).open();
        });

        ItemStack animals = headDatabaseAPI.getItemHead("90787");
        SkullMeta animalsMeta = (SkullMeta) animals.getItemMeta();
        animalsMeta.setDisplayName(colorize("&aAnimals"));
        animals.setItemMeta(animalsMeta);
        addItem(11, animals, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.ANIMALS);
            new IndivisualHeadsMenu(main, player, "Animals", heads, house).open();
        });

        // Blocks
        ItemStack blocks = headDatabaseAPI.getItemHead("1653");
        SkullMeta blocksMeta = (SkullMeta) blocks.getItemMeta();
        blocksMeta.setDisplayName(colorize("&aBlocks"));
        blocks.setItemMeta(blocksMeta);
        addItem(12, blocks, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.BLOCKS);
            new IndivisualHeadsMenu(main, player, "Blocks", heads, house).open();
        });

        ItemStack decoration = headDatabaseAPI.getItemHead("24830");
        SkullMeta decorationMeta = (SkullMeta) decoration.getItemMeta();
        decorationMeta.setDisplayName(colorize("&aDecoration"));
        decoration.setItemMeta(decorationMeta);
        addItem(13, decoration, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.DECORATION);
            new IndivisualHeadsMenu(main, player, "Decoration", heads, house).open();
        });

        ItemStack foodDrinks = headDatabaseAPI.getItemHead("61666");
        SkullMeta foodDrinksMeta = (SkullMeta) foodDrinks.getItemMeta();
        foodDrinksMeta.setDisplayName(colorize("&aFood & Drinks"));
        foodDrinks.setItemMeta(foodDrinksMeta);
        addItem(14, foodDrinks, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.FOOD_DRINKS);
            new IndivisualHeadsMenu(main, player, "Food & Drinks", heads, house).open();
        });

        ItemStack humans = headDatabaseAPI.getItemHead("16150");
        SkullMeta humansMeta = (SkullMeta) humans.getItemMeta();
        humansMeta.setDisplayName(colorize("&aHumans"));
        humans.setItemMeta(humansMeta);
        addItem(15, humans, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.HUMANS);
            new IndivisualHeadsMenu(main, player, "Humans", heads, house).open();
        });

        ItemStack humanoid = headDatabaseAPI.getItemHead("47718");
        SkullMeta humanoidMeta = (SkullMeta) humanoid.getItemMeta();
        humanoidMeta.setDisplayName(colorize("&aHumanoid"));
        humanoid.setItemMeta(humanoidMeta);
        addItem(16, humanoid, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.HUMANOID);
            new IndivisualHeadsMenu(main, player, "Humanoid", heads, house).open();
        });

        ItemStack misc = headDatabaseAPI.getItemHead("112072");
        SkullMeta miscMeta = (SkullMeta) misc.getItemMeta();
        miscMeta.setDisplayName(colorize("&aMisc"));
        misc.setItemMeta(miscMeta);
        addItem(19, misc, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.MISCELLANEOUS);
            new IndivisualHeadsMenu(main, player, "Misc", heads, house).open();
        });

        ItemStack monsters = headDatabaseAPI.getItemHead("78465");
        SkullMeta monstersMeta = (SkullMeta) monsters.getItemMeta();
        monstersMeta.setDisplayName(colorize("&aMonsters"));
        monsters.setItemMeta(monstersMeta);
        addItem(20, monsters, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.MONSTERS);
            new IndivisualHeadsMenu(main, player, "Monsters", heads, house).open();
        });

        ItemStack plants = headDatabaseAPI.getItemHead("29431");
        SkullMeta plantsMeta = (SkullMeta) plants.getItemMeta();
        plantsMeta.setDisplayName(colorize("&aPlants"));
        plants.setItemMeta(plantsMeta);
        addItem(21, plants, () -> {
            List<Head> heads = headDatabaseAPI.getHeads(CategoryEnum.PLANTS);
            new IndivisualHeadsMenu(main, player, "Plants", heads, house).open();
        });

        // Go Back Arrow
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cGo Back"))
                .description("Go back to the house menu")
                .build(), () -> {
            new MyHousesMenu(main, player, player).open();
        });

    }
}
