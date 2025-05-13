package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.GenericPagination.RegionProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class WithinRegionCondition extends CHTSLImpl {
    public WithinRegionCondition() {
        super(ConditionEnum.WITHIN_REGION,
                "Within Region",
                "Requires the user to be in the specified region.",
                Material.GRASS_BLOCK,
                List.of("inRegion"));

        getProperties().add(new RegionProperty(
                "region",
                "Region",
                "The region to check."
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Region region = getProperty("region", RegionProperty.class).getValue();
        return region.getPlayersInRegion().contains(player.getUniqueId()) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
