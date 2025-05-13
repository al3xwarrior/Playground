package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.GenericPagination.LayoutProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class ApplyInventoryLayoutAction extends HTSLImpl {
    String layout;
    public ApplyInventoryLayoutAction() {
        super(
                ActionEnum.APPLY_INVENTORY_LAYOUT,
                "Apply Inventory Layout",
                "Applies an inventory layout to the player.",
                Material.IRON_AXE,
                List.of("layout")
        );

        getProperties().add(new LayoutProperty(
                "layout",
                "Layout",
                "The layout to apply."
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (layout == null) {
            return OutputType.SUCCESS;
        }
        Layout layout = getValue("layout", Layout.class);
        if (layout == null) {
            return OutputType.SUCCESS;
        }
        layout.apply(player);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
