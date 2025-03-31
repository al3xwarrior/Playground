package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

@Setter
@Getter
public class ActionbarAction extends HTSLImpl {
    private String message = "Hello world!";

    public ActionbarAction() {
        super(
                "actionbar_action",
                "Display Actionbar",
                "Displays a message in the action bar.",
                Material.WRITABLE_BOOK
        );
    }

    @Override
    public String toString() {
        return "ActionBar (Message: " + message + ")";
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.sendActionBar(StringUtilsKt.housingStringFormatter(message, house, player));
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("message", message);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("message")) {
//            return;
//        }
//        message = (String) data.get("message");
//    }
}
