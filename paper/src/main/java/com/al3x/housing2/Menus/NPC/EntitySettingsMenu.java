package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.trait.AttributeTrait;
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
    public void setupItems() {
        addItem(10, ItemBuilder.create(Material.STONE_SWORD)
                .name("&eCan Be Damaged")
                .description("Toggle if the NPC can be damaged")
                .info(null, housingNPC.isCanBeDamaged() ? "&aTrue" : "&cFalse")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            housingNPC.setCanBeDamaged(!housingNPC.isCanBeDamaged());
            setupItems();
        });

        AttributeTrait attributeTrait = housingNPC.getCitizensNPC().getOrAddTrait(AttributeTrait.class);
        addItem(11, ItemBuilder.create(Material.GOLDEN_APPLE)
                        .name("&eChange Max Health")
                        .description("Change the max health of the NPC")
                        .info(null, attributeTrait.getAttributeValue(Attribute.MAX_HEALTH))
                        .info(null, "")
                        .info(null, "&cI dont know why this isn't working.")
                        .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                        .build(), () -> {
                    player.sendMessage(colorize("&eEnter the new max health value:"));
                    openChat(main, attributeTrait.getAttributeValue(Attribute.MAX_HEALTH) + "", (value) -> {
                        try {
                            attributeTrait.setAttributeValue(Attribute.MAX_HEALTH, Double.parseDouble(value));
                            if (housingNPC.getCitizensNPC().getEntity() instanceof LivingEntity entity) {
                                entity.setMaxHealth(entity.getHealth());
                            }
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
            housingNPC.getCitizensNPC().setUseMinecraftAI(!housingNPC.getCitizensNPC().useMinecraftAI());
            setupItems();
        });

        //back
        ItemBuilder backBuilder = ItemBuilder.create(Material.ARROW)
                .name("&cBack")
                .description("Go back to the NPC menu");

        addItem(31, backBuilder.build(), () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }
}
