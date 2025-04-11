package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ExpandableProperty<V> extends ActionProperty<V> {
    private final List<ActionProperty<?>> properties = new ArrayList<>();
    public ExpandableProperty(String id, String name, String description, Material icon) {
        super(id, name, description, icon);
    }
}
