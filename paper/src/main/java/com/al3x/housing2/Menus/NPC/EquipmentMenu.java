package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.al3x.housing2.Utils.Color.colorize;

public class EquipmentMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public EquipmentMenu(Main main, Player player, HousingNPC housingNPC) {
        super(player, "&7Edit Equipment", 54);
        this.main = main;
        this.player = player;
        this.housesManager = main.getHousesManager();
        this.house = housesManager.getHouse(player.getWorld());
        this.housingNPC = housingNPC;
    }

    @Override
    public void setupItems() {
        String[] equipmentnames = new String[] {
                "Helmet", "Chestplate", "Leggings", "Boots", "Main Hand", "Off Hand"
        };
        int[] slots = new int[] { 11, 13, 15, 29, 31, 33 };

        for (int i = 0; i < equipmentnames.length; i++) {
            String name = equipmentnames[i];
            ItemStack npcItem = housingNPC.getEquipment(name);
            ItemStack equipment = new ItemStack(npcItem == null ? Material.BLUE_STAINED_GLASS_PANE : npcItem.getType());
            ItemMeta equipmentMeta = equipment.getItemMeta();
            equipmentMeta.setDisplayName(colorize("&a" + equipmentnames[i]));
            equipment.setItemMeta(equipmentMeta);
            addItem(slots[i], equipment, () -> {
                new ItemSelectMenu(player, this, (item) -> {
                    housingNPC.setEquipment(name, item);
                    open();
                }).open();
            });
        }

        ItemBuilder back = ItemBuilder.create(Material.ARROW).name("&aGo Back").description("To the NPC Menu");
        addItem(49, back.build(), () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }
}
