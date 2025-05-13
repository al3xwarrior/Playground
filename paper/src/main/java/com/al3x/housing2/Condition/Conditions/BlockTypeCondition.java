package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class BlockTypeCondition extends CHTSLImpl {
    public BlockTypeCondition() {
        super(
                ConditionEnum.BLOCK_TYPE,
                "Block Type Requirement",
                "Checks if the block type is the same as the one specified.",
                Material.STONE,
                List.of("blockType")
        );
        getProperties().add(new EnumProperty<>(
                "type",
                "Block Type",
                "The type of block to check for.",
                Material.class
        ).setValue(Material.GRASS_BLOCK));
    }


    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Material eventType = null;
        if (event.cancellable() instanceof BlockBreakEvent breakEvent) {
            eventType = breakEvent.getBlock().getType();
        }

        if (event.cancellable() instanceof BlockPlaceEvent placeEvent) {
            eventType = placeEvent.getBlock().getType();
        }

        if (eventType == null) {
            return OutputType.FALSE;
        }

        Material blockTypeProperty = getValue("type", Material.class);
        if (eventType == blockTypeProperty) {
            return OutputType.TRUE;
        } else {
            return OutputType.FALSE;
        }
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_BLOCK_BREAK, EventType.PLAYER_BLOCK_PLACE);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
