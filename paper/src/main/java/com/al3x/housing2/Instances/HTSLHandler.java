package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTSLHandler {
    public static List<Action> importActions(String content, String indent, HousingWorld house) {
        List<HTSLImpl> defaultActions = List.of(Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter(a -> a instanceof HTSLImpl).map(a -> (HTSLImpl) a).toArray(HTSLImpl[]::new));

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(content.split("\n")));

        List<Action> actions = new ArrayList<>();
        while (!lines.isEmpty()) {
            String line = lines.removeFirst().replaceFirst(indent, "");
            for (HTSLImpl action : defaultActions) {
                if (line.startsWith(action.getScriptingKeywords().getFirst())) {
                    HTSLImpl a = (HTSLImpl) action.clone(house);
                    lines = a.importAction(StringUtilsKt.substringAfter(line, action.getScriptingKeywords().getFirst() + (line.contains(" ") ? " " : "")), indent, new ArrayList<>(lines));
                    actions.add(a);
                    break;
                }
            }
        }

        return actions;
    }
}
