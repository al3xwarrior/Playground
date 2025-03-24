package com.al3x.housing2.Data;

import com.al3x.housing2.Instances.Function;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FunctionData {
    private String name;
    private String id;
    private String description;
    private Integer ticks;
    private String material;
    private boolean global;
    private List<ActionData> actions;

    public FunctionData() {

    }

    public FunctionData(String name, String id, String description, Integer ticks, String material, boolean global, List<ActionData> actions) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.ticks = ticks;
        this.material = material;
        this.global = global;
        this.actions = actions;
    }

    public static List<FunctionData> fromList(List<Function> list) {
        List<FunctionData> functionData = new ArrayList<>();
        for (Function function : list) {
            functionData.add(new FunctionData(
                function.getName(),
                function.getId().toString(),
                function.getDescription(),
                function.getTicks(),
                function.getMaterial().name(),
                function.isGlobal(),
                ActionData.fromList(function.getActions())
            ));
        }
        return functionData;
    }

    public static List<Function> toList(List<FunctionData> list) {
        List<Function> functions = new ArrayList<>();
        for (FunctionData data : list) {
            functions.add(new Function(
                data.getName(),
                UUID.fromString(data.getId()),
                data.getTicks(),
                Material.valueOf(data.getMaterial()),
                data.getDescription(),
                ActionData.toList(data.getActions()),
                data.isGlobal()
            ));
        }
        return functions;
    }
}