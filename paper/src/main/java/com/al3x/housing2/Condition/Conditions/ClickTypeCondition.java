package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ClickTypeCondition extends CHTSLImpl {

    public ClickTypeCondition() {
        super(
                ConditionEnum.CLICKTYPE_REQUIREMENT,
                "Click Type",
                "Checks if the click type is the same as the one specified.",
                Material.OAK_BUTTON,
                List.of("clickType")
        );

        getProperties().add(
                new EnumProperty<>(
                        "type",
                        "Click Type",
                        "The type of click to check for.",
                        ClickType.class
                ).setValue(ClickType.LEFT)
        );
    }
    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (event.cancellable() instanceof InventoryClickEvent e) {
            return e.getClick() == getValue("type", ClickType.class) ? OutputType.TRUE : OutputType.FALSE;
        }
        return OutputType.FALSE;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.INVENTORY_CLICK);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
