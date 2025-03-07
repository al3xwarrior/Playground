package com.al3x.housing2.Events;

import com.al3x.housing2.Menus.Menu;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuClickEvent extends InventoryInteractEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Menu menu;
    private final ClickType click;
    private final InventoryAction action;
    private InventoryType.SlotType slot_type;
    private int whichSlot;
    private int rawSlot;
    private ItemStack current = null;
    private int hotbarKey = -1;

    private boolean stopped = false;

    public MenuClickEvent(@NotNull Menu menu, @NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        super(view);
        this.menu = menu;
        this.slot_type = type;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        this.click = click;
        this.action = action;
    }

    public MenuClickEvent(@NotNull Menu menu, @NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, int key) {
        this(menu, view, type, slot, click, action);
        this.hotbarKey = key;
    }

    /**
     * Gets the type of slot that was clicked.
     *
     * @return the slot type
     */
    @NotNull
    public InventoryType.SlotType getSlotType() {
        return slot_type;
    }

    /**
     * Gets the current ItemStack on the cursor.
     *
     * @return the cursor ItemStack
     */
    @NotNull // Paper - fix nullability
    public ItemStack getCursor() {
        return getView().getCursor();
    }

    /**
     * Gets the ItemStack currently in the clicked slot.
     *
     * @return the item in the clicked slot
     */
    @Nullable
    public ItemStack getCurrentItem() {
        if (slot_type == InventoryType.SlotType.OUTSIDE) {
            return current;
        }
        return getView().getItem(rawSlot);
    }

    /**
     * Gets whether or not the ClickType for this event represents a right
     * click.
     *
     * @return true if the ClickType uses the right mouse button.
     * @see ClickType#isRightClick()
     */
    public boolean isRightClick() {
        return click.isRightClick();
    }

    /**
     * Gets whether or not the ClickType for this event represents a left
     * click.
     *
     * @return true if the ClickType uses the left mouse button.
     * @see ClickType#isLeftClick()
     */
    public boolean isLeftClick() {
        return click.isLeftClick();
    }

    /**
     * Gets whether the ClickType for this event indicates that the key was
     * pressed down when the click was made.
     *
     * @return true if the ClickType uses Shift or Ctrl.
     * @see ClickType#isShiftClick()
     */
    public boolean isShiftClick() {
        return click.isShiftClick();
    }

    /**
     * Sets the item on the cursor.
     *
     * @param stack the new cursor item
     * @deprecated This changes the ItemStack in their hand before any
     *     calculations are applied to the Inventory, which has a tendency to
     *     create inconsistencies between the Player and the server, and to
     *     make unexpected changes in the behavior of the clicked Inventory.
     */
    @Deprecated(since = "1.5.2")
    public void setCursor(@Nullable ItemStack stack) {
        getView().setCursor(stack);
    }

    /**
     * Sets the ItemStack currently in the clicked slot.
     *
     * @param stack the item to be placed in the current slot
     */
    public void setCurrentItem(@Nullable ItemStack stack) {
        if (slot_type == InventoryType.SlotType.OUTSIDE) {
            current = stack;
        } else {
            getView().setItem(rawSlot, stack);
        }
    }

    /**
     * Gets the inventory corresponding to the clicked slot.
     *
     * @return inventory, or null if clicked outside
     * @see InventoryView#getInventory(int)
     */
    @Nullable
    public Inventory getClickedInventory() {
        return getView().getInventory(rawSlot);
    }

    /**
     * The slot number that was clicked, ready for passing to
     * {@link Inventory#getItem(int)}. Note that there may be two slots with
     * the same slot number, since a view links two different inventories.
     *
     * @return The slot number.
     */
    public int getSlot() {
        return whichSlot;
    }

    /**
     * The raw slot number clicked, ready for passing to {@link InventoryView
     * #getItem(int)} This slot number is unique for the view.
     *
     * @return the slot number
     */
    public int getRawSlot() {
        return rawSlot;
    }

    /**
     * If the ClickType is NUMBER_KEY, this method will return the index of
     * the pressed key (0-8).
     *
     * @return the number on the key minus 1 (range 0-8); or -1 if not
     *     a NUMBER_KEY action
     */
    public int getHotbarButton() {
        return hotbarKey;
    }

    /**
     * Gets the InventoryAction that triggered this event.
     * <p>
     * This action cannot be changed, and represents what the normal outcome
     * of the event will be. To change the behavior of this
     * InventoryClickEvent, changes must be manually applied.
     *
     * @return the InventoryAction that triggered this event.
     */
    @NotNull
    public InventoryAction getAction() {
        return action;
    }

    /**
     * Gets the ClickType for this event.
     * <p>
     * This is insulated against changes to the inventory by other plugins.
     *
     * @return the type of inventory click
     */
    @NotNull
    public ClickType getClick() {
        return click;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public Menu getMenu() {
        return menu;
    }

    public static MenuClickEvent fromInventoryClickEvent(Menu menu, InventoryClickEvent event) {
        return new MenuClickEvent(menu, event.getView(), event.getSlotType(), event.getRawSlot(), event.getClick(), event.getAction());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}