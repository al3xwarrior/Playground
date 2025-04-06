package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.ProtoolsType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

@Getter
@Setter
@ToString
public class ProtoolsAction extends Action {
    String posA = "0,0,0";
    String posB = "0,0,0";
    ProtoolsType mode = ProtoolsType.SELECT;
    String blockList = "grass_block";
    String blockList2 = "dirt";

    public ProtoolsAction() {
        super("Protools Action");
    }

    @Override
    public String toString() {
        return "ProtoolsAction (" + "posA='" + posA + ", posB='" + posB + ", mode=" + mode + ", blockList='" + blockList + ", blockList2='" + blockList2 + ')';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        //Do nothing
    }

    @Override
    public void createDisplayItem(ItemBuilder builder, HousingWorld house) {
        builder.material(Material.WOODEN_AXE);
        builder.name("&eProtools");
        builder.info("&eSettings", "");
        builder.info("Mode", "&6" + mode.toString());
        if (mode == ProtoolsType.SELECT) {
            builder.info("Pos A", "&a" + posA);
            builder.info("Pos B", "&a" + posB);
        }
        if (mode == ProtoolsType.SET) {
            builder.info("Block", "&d" + blockList);
        }
        if (mode == ProtoolsType.REPLACE) {
            builder.info("Block", "&d" + blockList);
            builder.info("Block 2", "&d" + blockList2);
        }
        if (mode == ProtoolsType.PASTE) {
            builder.info("Paste Location", "&a" + posA);
        }
        if (mode == ProtoolsType.INSERT) {
            builder.info("Insert Location", "&a" + posA);
        }
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.WOODEN_AXE);
        builder.name("&aProtools");
        builder.description("Execute a protools action");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.add(new ActionEditor.ActionItem("mode",
                ItemBuilder.create(mode.getMaterial())
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + mode)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, ProtoolsType.values(), null));

        if (mode == ProtoolsType.SELECT) {
            items.add(new ActionEditor.ActionItem("posA",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&ePos A")
                            .info("&7Current Value", "")
                            .info(null, "&a" + posA)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                            .mClick(ItemBuilder.ActionType.USE_SELECTION),
                    getPosFunction(backMenu, player, true)));
            items.add(new ActionEditor.ActionItem("posB",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&ePos B")
                            .info("&7Current Value", "")
                            .info(null, "&a" + posB)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                            .mClick(ItemBuilder.ActionType.USE_SELECTION),
                    getPosFunction(backMenu, player, false)));
        }

        if (mode == ProtoolsType.SET) {
            items.add(new ActionEditor.ActionItem("blockList",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&eBlock")
                            .info("&7Current Value", "")
                            .info(null, "&a" + blockList)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.STRING));
        }

        if (mode == ProtoolsType.REPLACE) {
            items.add(new ActionEditor.ActionItem("blockList",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&eBlock 1")
                            .info("&7Current Value", "")
                            .info(null, "&a" + blockList)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.STRING));
            items.add(new ActionEditor.ActionItem("blockList2",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&eBlock 2")
                            .info("&7Current Value", "")
                            .info(null, "&a" + blockList2)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.STRING));
        }

        if (mode == ProtoolsType.PASTE) {
            //start location
            items.add(new ActionEditor.ActionItem("posA",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&ePaste Location")
                            .info("&7Current Value", "")
                            .info(null, "&a" + posA)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                            .mClick(ItemBuilder.ActionType.USE_SELECTION),
                    getPosFunction(backMenu, player, true)));
        }

        if (mode == ProtoolsType.INSERT) {
            //start location
            items.add(new ActionEditor.ActionItem("posA",
                    ItemBuilder.create(Material.GRASS_BLOCK)
                            .name("&eInsert Location")
                            .info("&7Current Value", "")
                            .info(null, "&a" + posA)
                            .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                            .mClick(ItemBuilder.ActionType.USE_SELECTION),
                    getPosFunction(backMenu, player, true)));
        }
        return new ActionEditor(4, "&eProtools Action Settings", items);
    }

    private BiFunction<InventoryClickEvent, Object, Boolean> getPosFunction(Menu backMenu, Player player, boolean isA) {
        String value = isA ? posA : posB;
        return (event, o) -> {
            if (event.isLeftClick()) {
                backMenu.openChat(Main.getInstance(), value, (s) -> {
                    if (isA) {
                        posA = s;
                    } else {
                        posB = s;
                    }
                    backMenu.open();
                });
            } else if (event.getClick() == ClickType.MIDDLE) {
                Duple<Location, Location> selection = Main.getInstance().getProtoolsManager().getSelection(player);
                if (selection == null || selection.getFirst() == null) {
                    player.sendMessage("&cYou must select a region first!");
                    return null;
                }
                if (isA) {
                    posA = String.format(
                            "%s,%s,%s",
                            selection.getFirst().getBlockX(),
                            selection.getFirst().getBlockY(),
                            selection.getFirst().getBlockZ()
                    );
                } else {
                    posB = String.format(
                            "%s,%s,%s",
                            selection.getSecond().getBlockX(),
                            selection.getSecond().getBlockY(),
                            selection.getSecond().getBlockZ()
                    );
                }
            }
            return true;
        };
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        ProtoolsManager protoolsManager = Main.getInstance().getProtoolsManager();
        switch (mode) {
            case SELECT -> {
                Location posA = getLocationFromString(player, house, this.posA);
                Location posB = getLocationFromString(player, house, this.posB);

                if (posA == null || posB == null) {
                    player.sendMessage("&cInvalid positions!");
                    return OutputType.ERROR;
                }

                protoolsManager.setPositions(house, null, posA, posB);
            }

            case SET -> {
                protoolsManager.setRegionTo(house, house.getHouseUUID(), BlockList.fromString(blockList));
            }

            case REPLACE -> {
                BlockList from = BlockList.fromString(blockList);
                BlockList to = BlockList.fromString(blockList2);
                if (from == null || to == null) {
                    player.sendMessage("&cInvalid blocks!");
                    return OutputType.ERROR;
                }
                protoolsManager.setRegionTo(house, house.getHouseUUID(), from, to);
            }

            case COPY -> {
                protoolsManager.copyToClipboard(house, house.getHouseUUID());
            }

            case PASTE -> {
                Location posA = getLocationFromString(player, house, this.posA);
                if (posA == null) {
                    player.sendMessage("&cInvalid position!");
                    return OutputType.ERROR;
                }
                protoolsManager.pasteRegion(house, house.getHouseUUID(), posA, false);
            }

            case INSERT -> {
                Location posA = getLocationFromString(player, house, this.posA);
                if (posA == null) {
                    player.sendMessage("&cInvalid position!");
                    return OutputType.ERROR;
                }
                protoolsManager.pasteRegion(house, house.getHouseUUID(), posA, true);
            }

            case UNDO -> {
                protoolsManager.undo(house.getHouseUUID());
            }
        }

        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("posA", posA);
        data.put("posB", posB);
        data.put("mode", mode.name());
        data.put("blockList", blockList);
        data.put("blockList2", blockList2);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
}
