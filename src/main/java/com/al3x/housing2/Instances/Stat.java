package com.al3x.housing2.Instances;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Actions.SendTitleAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Enums.StatOperation;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class Stat {

    private UUID uuid;
    private String stat;
    private Double num;

    public Stat(UUID uuid, String stat, double num) {
        this.uuid = uuid;
        this.stat = stat;
        this.num = num;
    }

    public double modifyStat(StatOperation operation, double value) {
        switch (operation) {
            case StatOperation.INCREASE: return num += value;
            case StatOperation.DECREASE: return num -= value;
            case StatOperation.MULTIPLY: return num *= value;
            case StatOperation.DIVIDE: return num /= value;
            case StatOperation.MOD: return num %= value;
            case StatOperation.FLOOR: return num = Math.floor(num);
            case StatOperation.ROUND: return num = (double) Math.round(num);
            default: return num;
        }
    }

    public double getStatNum() {
        return num;
    }
    public String getStatName() {
        return stat;
    }
    
}
