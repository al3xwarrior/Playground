package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.trait.AttributeTrait;
import net.citizensnpcs.trait.ScaledMaxHealthTrait;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class EntitySettingsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public EntitySettingsMenu(Main main, Player player, HousingNPC housingNPC) {
        super(player, colorize("&7NPC Entity Settings"), 4 * 9);
        this.main = main;
        this.player = player;
        this.housesManager = main.getHousesManager();
        this.house = housesManager.getHouse(player.getWorld());
        this.housingNPC = housingNPC;
        setupItems();
    }

    @Override
    public void initItems() {
        addItem(10, ItemBuilder.create(Material.STONE_SWORD)
                .name("&eCan Be Damaged")
                .description("Toggle if the NPC can be damaged")
                .info(null, housingNPC.isCanBeDamaged() ? "&aTrue" : "&cFalse")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            housingNPC.setCanBeDamaged(!housingNPC.isCanBeDamaged());
            setupItems();
        });

        ScaledMaxHealthTrait maxHealthTrait = housingNPC.getCitizensNPC().getOrAddTrait(ScaledMaxHealthTrait.class);
        addItem(11, ItemBuilder.create(Material.GOLDEN_APPLE)
                        .name("&eChange Max Health")
                        .description("Change the max health of the NPC")
                        .info(null, maxHealthTrait.getMaxHealth() == null ? "null" : maxHealthTrait.getMaxHealth() + "")
                        .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                        .build(), () -> {
                    player.sendMessage(colorize("&eEnter the new max health value:"));
                    openChat(main, maxHealthTrait.getMaxHealth() + "", (value) -> {
                        try {
                            housingNPC.setMaxHealth(Double.parseDouble(value));
                            maxHealthTrait.setMaxHealth(Double.parseDouble(value));
                            Bukkit.getScheduler().runTask(main, this::open);
                        } catch (NumberFormatException e) {
                            player.sendMessage(colorize("&cInvalid number!"));
                        }
                    });
                }
        );

        addItem(12, ItemBuilder.create(Material.CLOCK)
                .name("&eRespawn Time")
                .description("Change the respawn time of the NPC")
                .info(null, housingNPC.getRespawnTime())
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            player.sendMessage(colorize("&eEnter the new respawn time value:"));
            openChat(main, housingNPC.getRespawnTime() + "", (value) -> {
                try {
                    housingNPC.setRespawnTime(Integer.parseInt(value));
                    Bukkit.getScheduler().runTask(main, this::open);
                } catch (NumberFormatException e) {
                    player.sendMessage(colorize("&cInvalid number!"));
                }
            });
        });

        addItem(13, ItemBuilder.create(Material.GRASS_BLOCK)
                .name("&eMinecraft AI")
                .description("Toggle if the NPC is using the Minecraft AI instead of just standing still")
                .info(null, housingNPC.getCitizensNPC().useMinecraftAI() ? "&aTrue" : "&cFalse")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            housingNPC.setMinecraftAI(!housingNPC.getCitizensNPC().useMinecraftAI());
            housingNPC.getCitizensNPC().setUseMinecraftAI(!housingNPC.getCitizensNPC().useMinecraftAI());
            setupItems();
        });

        addItem(14, ItemBuilder.create(Material.ZOMBIE_SPAWN_EGG)
                .name("&eIs Baby")
                .description("Toggle if the NPC is a baby")
                .info(null, housingNPC.isBaby() ? "&aTrue" : "&cFalse")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            housingNPC.setBaby(!housingNPC.isBaby());
            setupItems();
        });

        addItem(15, ItemBuilder.create(Material.PAPER)
                .name("&eEntity Events")
                .description("View and edit the entity events")
                .build(), () -> {
            new EntityEventsMenu(main, player, housingNPC).open();
        });

        //back
        ItemBuilder backBuilder = ItemBuilder.create(Material.ARROW)
                .name("&cBack")
                .description("Go back to the NPC menu");

        addItem(31, backBuilder.build(), () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }

    private class EntityEventsMenu extends Menu {
        public EntityEventsMenu(Main main, Player player, HousingNPC housingNPC) {
            super(player, colorize("&7Entity Events"), 4 * 9);
        }

        @Override
        public void initItems() {
            addEventActionItem(10, Material.SKELETON_SKULL, "&aNPC Death", "&7Executes when the NPC dies.", EventType.NPC_DEATH);
            addEventActionItem(11, Material.FIRE_CHARGE, "&aNPC Damage", "&7Executes when the NPC takes damage.", EventType.NPC_DAMAGE);
            addEventActionItem(12, Material.CHERRY_TRAPDOOR, "&aNPC Swim/Crawl", "&7Executes when the NPC swims or crawls.", EventType.ENTITY_SWIM_CRAWL);
        }

        private void addEventActionItem(int slot, Material material, String name, String description, EventType eventType) {
            addItem(slot, new ItemBuilder()
                            .material(material)
                            .name(name)
                            .description(description)
                            .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                            .build(),
                    () -> {
                        ActionsMenu menu = new ActionsMenu(main, player, house, eventType);
                        menu.setBackMenu(this);
                        menu.setActions(housingNPC.getActions(eventType));
                        menu.open();
                    });

            addItem(31, new ItemBuilder().material(Material.ARROW).name("&cBack").build(), () -> new EntitySettingsMenu(main, player, housingNPC).open());
        }

        private void addNotReadyItem(int slot, Material material, String name, String description) {
            addItem(slot, new ItemBuilder()
                            .material(material)
                            .name(name)
                            .description(description)
                            .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                            .build(),
                    () -> player.sendMessage(colorize("&cNot ready yet.")));
        }
    }
}
