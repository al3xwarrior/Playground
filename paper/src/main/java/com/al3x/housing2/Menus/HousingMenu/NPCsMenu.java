package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.FunctionSettingsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class NPCsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public NPCsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7NPCs"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<HousingNPC> paginationList = new PaginationList<>(house.getNPCs(), slots.length);
        List<HousingNPC> npcs = paginationList.getPage(currentPage);
        if (npcs != null) {
            for (int i = 0; i < npcs.size(); i++) {
                HousingNPC npc = npcs.get(i);
                ItemBuilder item = ItemBuilder.create(Material.PLAYER_HEAD);
                item.name(colorize("&a" + npc.getName()));
                item.skullTexture(npc.getSkinUUID());
                item.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
                addItem(slots[i], item.build(), (e) -> {
                    new NPCMenu(main, player, npc).open();
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build(), () -> {
            });
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), () -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), () -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new SystemsMenu(main, player, house).open();
        });

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate NPC")).build(), () -> {
            house.createNPC(player, player.getLocation().toCenterLocation());
            setupItems();
        });
    }
}