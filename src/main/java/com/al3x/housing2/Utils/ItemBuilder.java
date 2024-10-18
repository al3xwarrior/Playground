package com.al3x.housing2.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.ChatColor.*;


/**
 * ItemBuilder class for creating ItemStack objects with customizable properties such as name, description, actions,
 * and additional information.
 *
 * <p>Example usage: taken from src/main/java/com/al3x/housing2/Actions/ActionbarAction.java
 * <pre>
 *     ItemBuilder builder = new ItemBuilder(Material.WRITTEN_BOOK)
 *     .name("&eActionbar Action")
 *     .description("Sends a actionbar message")
 *     .info("Settings:", "")
 *     .info("Message: " + message, "")
 *     .lClick(ActionType.EDIT_YELLOW)
 *     .rClick(ActionType.REMOVE_YELLOW)
 *     .changeOrderLore(true);
 *     return builder.build();
 * </pre>
 */
public class ItemBuilder {
    private ItemStack stack;
    private Material material;
    private int amount;
    private short data;
    private String name;
    private String description;
    private List<String> extraLore;
    private ActionType leftClickAction;
    private ActionType rightClickAction;
    //Key: String, Value: Object (String, Int or Double)
    private List<Duple<String, Object>> info;
    private boolean glow;
    private boolean changeOrderLore;

    public ItemBuilder() {
        this.material = Material.AIR;
        this.amount = 1;
        this.data = 0;
        this.name = "";
        this.description = "";
        this.leftClickAction = null;
        this.rightClickAction = null;
        this.info = new ArrayList<>();
        this.glow = false;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(short data) {
        this.data = data;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Displays text with the following formats:
     * <color>Left Click to <action>!
     * <color>Click to <action>! (if only left click action is present)
     */
    public ItemBuilder lClick(ActionType leftClickAction) {
        this.leftClickAction = leftClickAction;
        return this;
    }

    /**
     * Displays text with the following format:
     * <color>Right Click to <action>!
     */
    public ItemBuilder rClick(ActionType rightClickAction) {
        this.rightClickAction = rightClickAction;
        return this;
    }

    /**
     * Adds information with a key and an integer value to the item builder.
     * <p>
     * Displays text with the following format:
     * &f<key>: <value>
     * &f<value> (if key is null)
     * &f<key> (if value is null)
     *
     * @param key   the key for the information
     * @param value the integer value associated with the key
     * @return the ItemBuilder with the added information
     */
    public ItemBuilder info(String key, Integer value) {
        this.info.add(new Duple<>(key, value));
        return this;
    }

    /**
     * Adds information with a key and an integer value to the item builder.
     * <p>
     * Displays text with the following format:
     * &f<key>: <value>
     * &f<value> (if key is null)
     * &f<key> (if value is null)
     *
     * @param key   the key for the information
     * @param value the integer value associated with the key
     * @return the ItemBuilder with the added information
     */
    public ItemBuilder info(String key, Double value) {
        this.info.add(new Duple<>(key, value));
        return this;
    }

    /**
     * Adds information with a key and an integer value to the item builder.
     * <p>
     * Displays text with the following format:
     * &f<key>: <value>
     * &f<value> (if key is null)
     * &f<key> (if value is null)
     *
     * @param key   the key for the information
     * @param value the integer value associated with the key
     * @return the ItemBuilder with the added information
     */
    public ItemBuilder info(String key, String value) {
        this.info.add(new Duple<>(key, value));
        return this;
    }

    /**
     * Adds lore to the item at the bottom of everything else.
     *
     * @param lore the lore to add to the item
     * @return the ItemBuilder with the added lore
     */
    public ItemBuilder extraLore(String... lore) {
        extraLore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    /**
     * Adds a label to the item builder that allows the user to change the order of the actions by shift clicking.
     * <p>
     * Displays text with the following format:
     * &7Use shift and left/right click to change order.
     *
     * @return the ItemBuilder with the added label
     */
    public ItemBuilder shiftClick() {
        this.changeOrderLore = true;
        return this;
    }

    /**
     * Enables or disables the label that allows the user to change the order of the actions by shift clicking.
     * <p>
     * Displays text with the following format:
     * &7Use shift and left/right click to change order.
     *
     * @return the ItemBuilder with the added label
     */
    public ItemBuilder changeOrderLore(boolean changeOrderLore) {
        this.changeOrderLore = changeOrderLore;
        return this;
    }

    public ItemStack build() {
        stack = new ItemStack(material, amount);
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta == null) {
            // ItemMeta is null for AIR
            return stack;
        }
        itemMeta.setDisplayName(colorize(name));
        itemMeta.setLore(colorize(getLore()));
        // Hide all item flags cause they are annoying and useless
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        stack.setItemMeta(itemMeta);
        if (glow) {
            stack.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(meta);
        }
        return stack;
    }


    private List<String> getLore() {
        List<String> labels = new ArrayList<>();
        //Action labels
        //<color>Click to <action>!
        if (leftClickAction != null && rightClickAction == null) {
            labels.add(colorize(leftClickAction.color + "Click to " + leftClickAction + "!"));
        } else {
            //<color>Left Click to <action>!
            if (leftClickAction != null)
                labels.add(colorize(leftClickAction.color + "Left Click to " + leftClickAction));
            //<color>Right Click to <action>!
            if (rightClickAction != null)
                labels.add(colorize(rightClickAction.color + "Right Click to " + rightClickAction));
        }
        //Lore formatting
        List<String> lore = HypixelLoreFormatter.hypixelLore(description, info, labels);

        //Action menu label
        if (changeOrderLore) {
            lore.add(colorize("&7Use shift and left/right click to change order."));
        }

        //Extra lore
        if (extraLore != null) {
            lore.addAll(extraLore);
        }

        return lore;
    }

    public static ItemBuilder create(Material material) {
        return new ItemBuilder().material(material);
    }

    public ItemStack getStack() {
        return stack;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public short getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ActionType getLeftClickAction() {
        return leftClickAction;
    }

    public ActionType getRightClickAction() {
        return rightClickAction;
    }

    public List<Duple<String, Object>> getInfo() {
        return info;
    }

    public boolean isGlow() {
        return glow;
    }

    public boolean isChangeOrderLore() {
        return changeOrderLore;
    }

    /**
     * Enum representing different types of actions with associated colors.
     */
    public static enum ActionType {
        EDIT_YELLOW("edit", YELLOW),
        REMOVE_YELLOW("remove", YELLOW),
        ADD_YELLOW("add", YELLOW),
        CHANGE_YELLOW("change", YELLOW),
        EDIT_ACTIONS("edit actions", YELLOW),
        EDIT_FUNCTION("edit function", YELLOW),
        RENAME_YELLOW("rename", YELLOW),
        SELECT_YELLOW("select", YELLOW),
        DELETE_YELLOW("delete", YELLOW),
        ;

        private String action;
        private ChatColor color;

        ActionType(String action, ChatColor color) {
            this.action = action;
            this.color = color;
        }

        public String toString() {
            return action;
        }
    }
}
