package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

@ToString
public class ApplyInventoryLayoutAction extends HTSLImpl {
    String layout;
    public ApplyInventoryLayoutAction() {
        super(
                "apply_inventory_layout_action",
                "Apply Inventory Layout",
                "Applies an inventory layout to the player.",
                Material.IRON_AXE
        );

        getProperties().add(new ActionProperty(
                "layout",
                "Layout",
                "The layout to apply.",
                ActionProperty.PropertyType.LAYOUT
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (layout == null) {
            return OutputType.SUCCESS;
        }
        Layout layout = house.getLayout(this.layout);
        if (layout == null) {
            return OutputType.SUCCESS;
        }
        layout.apply(player);
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("layout", layout);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
