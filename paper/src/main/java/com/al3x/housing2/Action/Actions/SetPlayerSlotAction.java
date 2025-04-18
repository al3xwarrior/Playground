package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class SetPlayerSlotAction extends HTSLImpl {
    private int slot;

    public SetPlayerSlotAction() {
        super("Set Player Slot Action");
        this.slot = 1;
    }

    public SetPlayerSlotAction(int slot) {
        super("Set Player Slot Action");
        this.slot = slot;
    }

    public String toString() {
        return "SetPlayerSlotAction (Slot: " + this.slot + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST_MINECART);
        builder.name("&eSet Player Slot");
        builder.info("&eSettings", "");
        builder.info("Slot", "&a" + slot);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST_MINECART);
        builder.name("&aSet Player Slot");
        builder.description("Set the player's active hotbar slot.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("slot",
                        ItemBuilder.create(Material.CHEST)
                                .name("&aSlot")
                                .info("&7Current Value", "")
                                .info(null, slot)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT, 1, 9
                )
        );
        return new ActionEditor(4, "&ePlayer Slot Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.getInventory().setHeldItemSlot(slot - 1);
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("slot", slot);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "playerSlot";
    }
}
