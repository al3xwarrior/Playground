package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Enums.StatOperation;

import static com.al3x.housing2.Action.Properties.StatValueProperty.*;

public class StatInstance {
    public StatOperation mode;
    public StatValueInstance value;

    public StatInstance() {
        this.mode = StatOperation.INCREASE;
        this.value = new StatValueInstance(
                false,
                "1.0",
                null
        );
    }

    public static class StatInstanceData {
        public StatOperation mode;
        public StatValueData value;

        public StatInstanceData(StatInstance instance) {
            this.mode = instance.mode;
            this.value = new StatValueData(
                    instance.value.isExpression(),
                    instance.value.getLiteralValue(),
                    ActionData.toData(instance.value.getExpressionValue())
            );
        }
    }

}
