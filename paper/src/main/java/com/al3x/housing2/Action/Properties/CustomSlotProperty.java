package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Setter
@Getter
public abstract class CustomSlotProperty<V> extends ActionProperty<V> {
    protected int customSlot = -1;
    public CustomSlotProperty(String id, String name, String description, Material icon, int customSlot) {
        super(id, name, description, icon);
        this.customSlot = customSlot;
    }
}
