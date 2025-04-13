package com.al3x.housing2.Data;

import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Action.StatInstance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//public class StatActionData {
//    public static MoreStatData fromStatValue(StatValue statValue) {
//        return new MoreStatData(
////            statValue.getLiteralValue(),
////            statValue.getStatInstances(),
////            statValue.getValue() != null ? fromStatValue(statValue.getValue()) : null,
////            statValue.isExpression(),
////            statValue.getStatType()
//        );
//    }
//
//    @Getter
//    @Setter
//    public static class MoreStatData {
//        private String literal;
//        private List<StatInstance> statInstances;
//        private MoreStatData value;
//        private boolean isExpression;
//        private String statType;
//        private Boolean isGlobal;
//
//        public MoreStatData() {
//        }
//
//        public StatValue toStatValue() {
//            return new StatValue(
//                    isGlobal != null ? ((isGlobal) ? "global" : "player") : statType,
//                    isExpression,
//                    literal,
//                    value != null ? value.toStatValue() : null,
//                    statInstances
//            );
//        }
//    }
//}