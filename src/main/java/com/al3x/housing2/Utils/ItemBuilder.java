package com.al3x.housing2.Utils;

import com.al3x.housing2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
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
    private String skullTexture;
    private int amount;
    private short data;
    private String name;
    private String description;
    private List<String> extraLore;
    private HashMap<ClickType, ActionType> actions;
    //Key: String, Value: Object (String, Int or Double)
    private List<Duple<String, Object>> info;
    private boolean glow;
    private boolean changeOrderLore;
    private boolean punctuation;
    private int textWidth;

    public ItemBuilder() {
        this.material = Material.AIR;
        this.amount = 1;
        this.data = 0;
        this.name = "";
        this.description = "";
        this.actions = new HashMap<>();
        this.info = new ArrayList<>();
        this.glow = false;
        this.changeOrderLore = false;
        this.punctuation = true;
        this.textWidth = 28;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder skullTexture(String skullTexture) {
        if (material != Material.PLAYER_HEAD) {
            throw new IllegalArgumentException("Skull texture can only be set for PLAYER_HEAD material.");
        }
        this.skullTexture = skullTexture;
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
        this.actions.put(ClickType.LEFT, leftClickAction);
        return this;
    }

    public ItemBuilder mClick(ActionType middleClickAction) {
        this.actions.put(ClickType.MIDDLE, middleClickAction);
        return this;
    }

    /**
     * Displays text with the following format:
     * <color>Right Click to <action>!
     */
    public ItemBuilder rClick(ActionType rightClickAction) {
        this.actions.put(ClickType.RIGHT, rightClickAction);
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

    public ItemBuilder shiftClick(boolean changeOrderLore) {
        this.changeOrderLore = changeOrderLore;
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

    /**
     * Enables or disables the punctuation at the end of each line of the description.
     *
     * @return the ItemBuilder with the added label
     */
    public ItemBuilder punctuation(boolean punctuation) {
        this.punctuation = punctuation;
        return this;
    }

    public ItemBuilder textWidth(int textWidth) {
        this.textWidth = textWidth;
        return this;
    }

    public ItemStack build() {
        //Make the skull or item stack
        if (skullTexture != null) {
            stack = SkullTextures.getCustomSkull(skullTexture);
        } else {
            stack = new ItemStack(material, amount);
        }

        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta == null) {
            // ItemMeta is null for AIR
            return stack;
        }

        //Set the item name and lore
        itemMeta.displayName(StringUtilsKt.housingStringFormatter(name));
        itemMeta.lore(getLore());

        // Hide all item flags cause they are annoying and useless
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey(Main.getInstance(), "dummy"), 0, AttributeModifier.Operation.ADD_NUMBER)); // needed to correctly hide attributes

        stack.setItemMeta(itemMeta);
        //Add glow effect
        if (glow) {
            stack.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static ClickType[] order = new ClickType[]{ClickType.LEFT, ClickType.MIDDLE, ClickType.RIGHT};

    private List<Component> getLore() {
        List<String> labels = new ArrayList<>();
        //Action labels
        //<color>Click to <action>!
        if (actions.containsKey(ClickType.LEFT) && actions.size() == 1) {
            labels.add(colorize( actions.get(ClickType.LEFT).color + "Click to " + actions.get(ClickType.LEFT).toString() + "!"));
        } else {
            for (ClickType clickType : order) {
                if (actions.containsKey(clickType)) {
                    labels.add(colorize(actions.get(clickType).color + StringUtilsKt.formatCapitalize(clickType.name().toLowerCase()) + " Click to " + actions.get(clickType).toString() + "!"));
                }
            }
        }
        //Lore formatting
        List<String> lore = HypixelLoreFormatter.hypixelLore(description, info, labels, punctuation, textWidth);

        //Action menu label
        if (changeOrderLore) {
            lore.add(colorize("&7Use shift and left/right click to change order."));
        }

        //Extra lore
        if (extraLore != null) {
            lore.addAll(extraLore);
        }

        ArrayList<Component> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(StringUtilsKt.housingStringFormatter(line));
        }

        return newLore;
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

    public List<String> getExtraLore() {
        return extraLore;
    }

    public HashMap<ClickType, ActionType> getActions() {
        return actions;
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
        VIEW_YELLOW("view", YELLOW),
        REMOVE_YELLOW("remove", YELLOW),
        ADD_YELLOW("add", YELLOW),
        SELECT_LEVEL("select level", YELLOW),
        CHANGE_YELLOW("change", YELLOW),
        EDIT_ACTIONS("edit actions", YELLOW),
        EDIT_FUNCTION("edit function", YELLOW),
        EDIT_COMMAND("edit command", YELLOW),
        EDIT_REGION("edit region", YELLOW),
        RENAME_YELLOW("rename", YELLOW),
        SELECT_YELLOW("select", YELLOW),
        DELETE_YELLOW("delete", YELLOW),
        TOGGLE_YELLOW("toggle", YELLOW),
        EXPORT_YELLOW("export", YELLOW),
        IMPORT_YELLOW("import", YELLOW),
        JOIN_YELLOW("join", YELLOW),
        TELEPORT("teleport", YELLOW),
        CLONE("clone", YELLOW),
        SELECT_REGION("select region", YELLOW),
        TOGGLE_EXPRESSION("toggle expression", YELLOW),
        TOGGLE_RANGE("toggle range", YELLOW),
        PLAY_SOUND("preview sound", YELLOW),
        GIVE_ITEM("give item", YELLOW),
        CLEAR_SEARCH("clear search", YELLOW),
        CLEAR("clear", YELLOW),
        EDIT_LEFT_CLICK_ACTIONS("edit left click actions", YELLOW),
        EDIT_RIGHT_CLICK_ACTIONS("edit right click actions", YELLOW),
        CYCLE_FORWARD("cycle forward", YELLOW),
        CYCLE_BACKWARD("cycle backwards", YELLOW),
        SAVE_LOCATION("save location", YELLOW),
        TOGGLE_VISIBILITY("toggle visibility", YELLOW),
        KICK("kick", YELLOW),
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
