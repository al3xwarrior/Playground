package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Enums.EventType;
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
    private Material type;

    public BlockTypeCondition() {
        super("Block Type Requirement");
        this.type = Material.GRASS_BLOCK;
    }

    @Override
    public String toString() {
        return "BlockTypeCondition{" +
                "type=" + type +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRASS_BLOCK);
        builder.name("&eBlock Type Requirement");
        builder.description("Checks the block type is the same as the requirement.");
        builder.info("Type", type.name());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRASS_BLOCK);
        builder.name("&eBlock Type Requirement");
        builder.description("Checks the block type is the same as the requirement.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("type",
                        ItemBuilder.create(type)
                                .name("&eType")
                                .info("&7Current Value", "")
                                .info(null, "&a" + type.name())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                    ActionEditor.ActionItem.ActionType.ENUM, Material.values(), null
                )
        );
        return new ActionEditor(4, "Click Type Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        if (event instanceof BlockBreakEvent e) {
            return e.getBlock().getType() == type;
        }
        if (event instanceof BlockPlaceEvent e) {
            return e.getBlock().getType() == type;
        }
        return false;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_BLOCK_BREAK, EventType.PLAYER_BLOCK_PLACE);
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
        return "blockType";
    }
}
