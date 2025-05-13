package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.ProtoolsCoordsProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
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
    public ProtoolsAction() {
        super(ActionEnum.PROTOOLS_ACTION,
                "Protools Action",
                "Executes a protools action.",
                Material.WOODEN_AXE,
                List.of()
        );

        getProperties().addAll(
                List.of(
                        new EnumProperty<>(
                                "mode",
                                "Mode",
                                "The protools action to execute.",
                                ProtoolsType.class,
                                Material.WOODEN_AXE
                        ).setValue(ProtoolsType.SELECT),
                        new ProtoolsCoordsProperty(
                                "posA",
                                "Position A",
                                "The first position to use for the action."
                        ).setValue("0,0,0").showIf(() -> getValue("mode", ProtoolsType.class) == ProtoolsType.SELECT ||
                                getValue("mode", ProtoolsType.class) == ProtoolsType.PASTE ||
                                getValue("mode", ProtoolsType.class) == ProtoolsType.INSERT
                        ),
                        new ProtoolsCoordsProperty(
                                "posB",
                                "Position B",
                                "The second position to use for the action."
                        ).setValue("0,0,0").showIf(() -> getValue("mode", ProtoolsType.class) == ProtoolsType.SELECT),
                        new StringProperty(
                                "blockList",
                                "Block List",
                                "The block list to use for the action."
                        ).setValue("grass_block").showIf(() -> getValue("mode", ProtoolsType.class) == ProtoolsType.SET ||
                                getValue("mode", ProtoolsType.class) == ProtoolsType.REPLACE
                        ),
                        new StringProperty(
                                "blockList2",
                                "Block List 2",
                                "The second block list to use for the action."
                        ).setValue("dirt").showIf(() -> getValue("mode", ProtoolsType.class) == ProtoolsType.REPLACE)
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        ProtoolsType mode = getValue("mode", ProtoolsType.class);
        ProtoolsManager protoolsManager = Main.getInstance().getProtoolsManager();
        switch (mode) {
            case SELECT -> {
                Location posA = getProperty("posA", ProtoolsCoordsProperty.class).getLocation(player, house);
                Location posB = getProperty("posB", ProtoolsCoordsProperty.class).getLocation(player, house);

                if (posA == null || posB == null) {
                    player.sendMessage("§cInvalid positions!");
                    return OutputType.ERROR;
                }

                protoolsManager.setPositions(house, null, posA, posB);
            }

            case SET -> {
                protoolsManager.setRegionTo(house, house.getHouseUUID(), BlockList.fromString(getValue("blockList", String.class)));
            }

            case REPLACE -> {
                BlockList from = BlockList.fromString(getValue("blockList", String.class));
                BlockList to = BlockList.fromString(getValue("blockList2", String.class));
                if (from == null || to == null) {
                    player.sendMessage("§cInvalid blocks!");
                    return OutputType.ERROR;
                }
                protoolsManager.setRegionTo(house, house.getHouseUUID(), from, to);
            }

            case COPY -> {
                protoolsManager.copyToClipboard(house, house.getHouseUUID());
            }

            case PASTE -> {
                Location posA = getProperty("posA", ProtoolsCoordsProperty.class).getLocation(player, house);
                if (posA == null) {
                    player.sendMessage("§cInvalid position!");
                    return OutputType.ERROR;
                }
                protoolsManager.pasteRegion(house, house.getHouseUUID(), posA, false);
            }

            case INSERT -> {
                Location posA = getProperty("posA", ProtoolsCoordsProperty.class).getLocation(player, house);
                if (posA == null) {
                    player.sendMessage("§cInvalid position!");
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
    public boolean requiresPlayer() {
        return false;
    }
}
