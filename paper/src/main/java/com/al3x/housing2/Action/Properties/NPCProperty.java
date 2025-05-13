package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class NPCProperty extends ActionProperty<Integer> {
    public NPCProperty(String id, String name, String description) {
        super(id, name, description, Material.PLAYER_HEAD);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        List<Duple<HousingNPC, ItemBuilder>> npcs = new ArrayList<>();
        for (HousingNPC npc : house.getNPCs()) {
            double distance = npc.getLocation().distance(player.getLocation());
            npcs.add(new Duple<>(npc, ItemBuilder.create(Material.PLAYER_HEAD).name(npc.getName()).info("Distance", Math.toIntExact(Math.round(distance))).info("NPC ID", npc.getInternalID()).lClick(ItemBuilder.ActionType.SELECT_YELLOW)));
        }

        npcs.sort(Comparator.comparing(npc -> npc.getFirst().getLocation().distance(player.getLocation())));

        PaginationMenu<HousingNPC> paginationMenu = new PaginationMenu<>(main, "Select an NPC", npcs, player, house, menu, (npc) -> {
            menu.open();
            setValue(npc.getInternalID());
            player.sendMessage(colorize("&a" + getName() + " set to: " + npc.getName()));
        });
        paginationMenu.open();
    }

    public HousingNPC getHousingNPC(HousingWorld house) {
        return house.getNPC(getValue());
    }
}
