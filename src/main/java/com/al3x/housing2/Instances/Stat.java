package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.StatOperation;

import java.util.*;

public class Stat {

    private UUID uuid;
    private String stat;
    private StatOperation statOperation;
    private Double num;

    public Stat(UUID uuid, String stat, double num) {
        this.uuid = uuid;
        this.statOperation = StatOperation.INCREASE;
        this.stat = stat;
        this.num = num;
    }

    public double modifyStat(double value) {
        switch (statOperation) {
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

    public void setStatOperation(StatOperation statOperation) { this.statOperation = statOperation;}
    public StatOperation getStatOperation() { return this.statOperation; }
    public double getStatNum() {
        return num;
    }
    public String getStatName() {
        return stat;
    }

    public UUID getUUID() {
        return uuid;
    }
    
}
