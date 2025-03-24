package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
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
    private ClickType type;

    public ClickTypeCondition() {
        super("Click Type Requirement");
        this.type = ClickType.LEFT;
    }

    @Override
    public String toString() {
        return "ClickTypeCondition{" +
                "type=" + type +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.DETECTOR_RAIL);
        builder.name("&eClick Type Requirement");
        builder.description("Requires the users click type to match the provided condition.");
        builder.info("Type", type.name());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DETECTOR_RAIL);
        builder.name("&eClick Type Requirement");
        builder.description("Requires the users click type to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("type",
                        ItemBuilder.create(Material.DETECTOR_RAIL)
                                .name("&eType")
                                .info("&7Current Value", "")
                                .info(null, "&a" + type.name())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, ClickType.values(), Material.STONE_BUTTON
                )
        );
        return new ActionEditor(4, "Click Type Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, CancellableEvent event) {
        if (event.cancellable() instanceof InventoryClickEvent e) {
            return e.getClick() == type;
        }
        return false;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.INVENTORY_CLICK);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("type", type.name());
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "clickType";
    }
}
