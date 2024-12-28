package com.al3x.housing2.Utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class NbtItemBuilder {
    private static final String PLUGIN_ID = "housing2";


    private PersistentDataContainer nbt;
    private NbtItemBuilder parent;
    private List<NbtItemBuilder> children;
    private String key;


    public NbtItemBuilder(PersistentDataContainer nbt) {
        this.nbt = nbt;
        this.parent = null;
        this.key = null;
        children = fromNbt();
    }

    public NbtItemBuilder(PersistentDataContainer nbt, String key, NbtItemBuilder parent) {
        this.nbt = nbt;
        this.parent = parent;
        this.key = key;
        children = fromNbt();
    }

    public NbtItemBuilder(ItemStack stack) {
        this(stack.getItemMeta().getPersistentDataContainer());
    }

    public NbtItemBuilder setString(String key, String value) {
        nbt.set(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.STRING, value);
        return this;
    }

    public NbtItemBuilder setInt(String key, int value) {
        nbt.set(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.INTEGER, value);
        return this;
    }

    public NbtItemBuilder setDouble(String key, double value) {
        nbt.set(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.DOUBLE, value);
        return this;
    }

    public NbtItemBuilder setBoolean(String key, boolean value) {
        nbt.set(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.BYTE, (byte) (value ? 1 : 0));
        return this;
    }

    public NbtItemBuilder addChild(String key) {
        NbtItemBuilder child = new NbtItemBuilder(nbt.getAdapterContext().newPersistentDataContainer(), key, this);
        nbt.set(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.TAG_CONTAINER, child.build());
        children.add(child);
        return child;
    }

    public NbtItemBuilder back() {
        return parent;
    }

    public String getString(String key) {
        if (!nbt.has(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.STRING)) {
            return null;
        }
        return nbt.get(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.STRING);
    }

    public int getInt(String key) {
        if (!nbt.has(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.INTEGER)) {
            return 0;
        }
        return nbt.get(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.INTEGER);
    }

    public double getDouble(String key) {
        if (!nbt.has(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.DOUBLE)) {
            return 0;
        }
        return nbt.get(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.DOUBLE);
    }

    public boolean getBoolean(String key) {
        if (!nbt.has(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.BYTE)) {
            return false;
        }
        return nbt.get(new NamespacedKey(PLUGIN_ID, key), PersistentDataType.BYTE) == 1;
    }

    public NbtItemBuilder remove(String key) {
        nbt.remove(new NamespacedKey(PLUGIN_ID, key));
        return this;
    }

    public PersistentDataContainer build() {
        for (NbtItemBuilder child : children) {
            nbt.set(new NamespacedKey(PLUGIN_ID, child.key), PersistentDataType.TAG_CONTAINER, child.build());
        }
        return nbt;
    }

    public List<NbtItemBuilder> getChildren() {
        return children;
    }

    public NbtItemBuilder getChild(String key) {
        return children.stream().filter(child -> child.key.equals(key)).findFirst().orElse(null);
    }

    private List<NbtItemBuilder> fromNbt() {
        List<NbtItemBuilder> list = new ArrayList<>();
        for (NamespacedKey key : nbt.getKeys()) {
            PersistentDataContainer childNbt = nbt.get(new NamespacedKey(PLUGIN_ID, key.getKey()), PersistentDataType.TAG_CONTAINER);
            if (childNbt != null) {
                list.add(new NbtItemBuilder(childNbt, key.getKey(), this));
            }
        }
        return list;
    }

    public static NbtItemBuilder fromItemStack(ItemStack stack) {
        return new NbtItemBuilder(stack);
    }
}
